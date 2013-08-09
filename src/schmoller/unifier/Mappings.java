package schmoller.unifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cpw.mods.fml.common.ModContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import net.minecraftforge.oredict.OreDictionary;

public class Mappings
{
	private HashMap<String, ItemStack> mMappings = new HashMap<String, ItemStack>();
	
	// All entries never allowed to be simplified
	private static List<String> mBlackList = Arrays.asList(new String[] {"logWood","plankWood","slabWood","stairWood","stickWood","treeSapling","treeLeaves"});
	
	private ItemStack parseItem(String id) throws Exception
	{
		int typeId;
		int meta;
		
		if (id.contains(":"))
		{
			String typeIdStr = id.split(":")[0];
			String metaStr = id.split(":")[1];
			
			try
			{
				typeId = Integer.parseInt(typeIdStr);
				meta = Integer.parseInt(metaStr);
			}
			catch(NumberFormatException e)
			{
				throw new Exception("Invalid item id '" + id + "'");
			}
			
			return new ItemStack(typeId, 1, meta);
		}
		else
		{
			try
			{
				typeId = Integer.parseInt(id);
			}
			catch(NumberFormatException e)
			{
				throw new Exception("Invalid item id '" + id + "'");
			}
			
			return new ItemStack(typeId, 1, 0);
		}
	}
	
	private boolean containsStack(List<ItemStack> list, ItemStack stack)
	{
		for(ItemStack item : list)
		{
			if(item.isItemEqual(stack))
				return true;
		}
		
		return false;
	}
	
	
	private String getFriendlyId(ItemStack item)
	{
		String modId = "Unknown";
		String itemId = "";
		
		ModContainer mod = Utilities.findOwningMod(item);
		
		if(mod != null)
			modId = mod.getName();
		
		itemId = Utilities.getSafeDisplayName(item);
		
		return String.format("%s - %s[%d:%d]", modId, itemId, item.itemID, item.getItemDamage());
	}
	
	public Set<String> getMappableOres()
	{
		HashSet<String> ores = new HashSet<String>();
		
		String[] allOres = OreDictionary.getOreNames();
		for(String oreName : allOres)
		{
			if(mBlackList.contains(oreName))
				continue;
		
			// Dont use special entries
			if(oreName.contains("$") || oreName.contains(".") || oreName.contains(":"))
				continue;
			
			ores.add(oreName);
		}
		
		return ores;
	}
	public Set<String> getMappableOres(OreCategory category)
	{
		HashSet<String> ores = new HashSet<String>();
		
		String[] allOres = OreDictionary.getOreNames();
		for(String oreName : allOres)
		{
			if(mBlackList.contains(oreName))
				continue;
		
			// Dont use special entries
			if(oreName.contains("$") || oreName.contains(".") || oreName.contains(":"))
				continue;
			
			if(OreCategory.getCategory(oreName) == category)
				ores.add(oreName);
		}
		
		return ores;
	}
	
	
	public void loadMappings(Configuration config)
	{
		mMappings.clear();
		
		// Find all the ores that need to be simplified
		String[] allOres = OreDictionary.getOreNames();
		
		for(String oreName : allOres)
		{
			if(mBlackList.contains(oreName))
				continue;
			
			List<ItemStack> stacks = OreDictionary.getOres(oreName);
			
			String out = "";
			
			for(ItemStack stack : stacks)
			{
				if(!out.isEmpty())
					out += "\n";
				out += "  " + getFriendlyId(stack);
				//out += stack.getItemName() + "(" + stack.itemID + ":" + stack.getItemDamage() + ")";
			}
			
			if(stacks.isEmpty())
				continue;

			String categoryName = "misc";
			
			if(oreName.startsWith("ore") || oreName.endsWith("ore"))
				categoryName = "ore";
			else if(oreName.startsWith("dust") || oreName.endsWith("dust"))
				categoryName = "dust";
			else if(oreName.startsWith("ingot") || oreName.endsWith("ingot"))
				categoryName = "ingot";
			else if(oreName.startsWith("block") || oreName.endsWith("block"))
				categoryName = "block";
			else if(oreName.startsWith("item") || oreName.endsWith("item"))
				categoryName = "item";
			else if(oreName.startsWith("dye"))
				categoryName = "dye";
			
			// Dont show it if there is only 1 option unless it is already set 
			if(stacks.size() == 1 && !config.hasKey(categoryName, oreName))
				continue;
			
			// Dont use special entries
			if(oreName.contains("$") || oreName.contains("."))
				continue;
			
			Property prop = config.get(categoryName, oreName, "");
			prop.comment = "Possible values for " + oreName + " are:\n" + out;
			
			// Check for raw copy and pastes
			if(prop.getString().contains("[") && prop.getString().contains("]"))
			{
				String textId = prop.getString().substring(0, prop.getString().indexOf("["));
				String otherId = prop.getString().substring(prop.getString().indexOf("[") + 1, prop.getString().indexOf("]"));
				
				ItemStack item = null;
				
				try
				{
					item = parseItem(otherId);
				}
				catch(Exception e)
				{
					throw new RuntimeException(String.format("Bad mapping listed as '%s' in %s. The specified item id [%s] is not a valid item id", prop.getName() + "=" + prop.getString(), categoryName, otherId));
				}
				
				if(!containsStack(stacks, item))
					throw new RuntimeException(String.format("Bad mapping listed as '%s' in %s. The specified item [%s] is not a valid item to use for %s.", prop.getName() + "=" + prop.getString(), categoryName, textId, otherId, oreName));

				mMappings.put(oreName, item);
			}
			// Check for numeric id only
			else if(prop.getString().contains(":"))
			{
				ItemStack item = null;
				
				try
				{
					item = parseItem(prop.getString());
				}
				catch(Exception e)
				{
					throw new RuntimeException(String.format("Bad mapping listed as '%s' in %s. The specified item id %s is not a valid item id", prop.getName() + "=" + prop.getString(), categoryName, prop.getString()));
				}
				
				if(!containsStack(stacks,item))
					throw new RuntimeException(String.format("Bad mapping listed as '%s' in %s. The specified item %s is not a valid item to use for %s.", prop.getName() + "=" + prop.getString(), categoryName, prop.getString(), oreName));
				
				mMappings.put(oreName, item);
			}
			// Check for text id only
			else if(!prop.getString().isEmpty())
			{
				throw new RuntimeException(String.format("Bad mapping listed as '%s' in %s. %s is not a valid item definition. Did you forget the item id?", prop.getName() + "=" + prop.getString(), categoryName, prop.getString()));
			}
			// Otherwise it is not set
		}
		
		config.getCategory("ore").setComment("Here you can define what types are used for ores.\nEvery registered ore and every possible choice are listed here.\nLeaving one blank means that it will not be simplified in any recipes.\n\nThe possible values for any of them are in the comment above them. You may select only one (1) of them for any registered ore.\nChanging the mapping will not change what ores are present in existing worlds, it will only change recipes" );
		config.getCategory("dust").setComment("Here you can define what types are used for dusts.\nEvery registered dust and every possible choice are listed here.\nLeaving one blank means that it will not be simplified in any recipes.\n\nThe possible values for any of them are in the comment above them. You may select only one (1) of them for any registered dust.\nChanging the mapping will not change what dusts are present in existing worlds, it will only change recipes" );
		config.getCategory("item").setComment("Here you can define what types are used for items.\nEvery registered item and every possible choice are listed here.\nLeaving one blank means that it will not be simplified in any recipes.\n\nThe possible values for any of them are in the comment above them. You may select only one (1) of them for any registered item.\nChanging the mapping will not change what items are present in existing worlds, it will only change recipes" );
		config.getCategory("ingot").setComment("Here you can define what types are used for ingots.\nEvery registered ingot and every possible choice are listed here.\nLeaving one blank means that it will not be simplified in any recipes.\n\nThe possible values for any of them are in the comment above them. You may select only one (1) of them for any registered ingot.\nChanging the mapping will not change what ingots are present in existing worlds, it will only change recipes" );
		config.getCategory("block").setComment("Here you can define what types are used for blocks.\nEvery registered block and every possible choice are listed here.\nLeaving one blank means that it will not be simplified in any recipes.\n\nThe possible values for any of them are in the comment above them. You may select only one (1) of them for any registered block.\nChanging the mapping will not change what blocks are present in existing worlds, it will only change recipes" );
		config.getCategory("dye").setComment("Here you can define what types are used for dyes.\nEvery registered dye and every possible choice are listed here.\nLeaving one blank means that it will not be simplified in any recipes.\n\nThe possible values for any of them are in the comment above them. You may select only one (1) of them for any registered dye.\nChanging the mapping will not change what dyes are present in existing worlds, it will only change recipes" );
		config.getCategory("misc").setComment("Here you can define what types are used for everything else.\nEvery registered item and every possible choice are listed here.\nLeaving one blank means that it will not be simplified in any recipes.\n\nThe possible values for any of them are in the comment above them. You may select only one (1) of them for any registered item.\nChanging the mapping will not change what items are present in existing worlds, it will only change recipes" );
		
		config.save();
	}
	
	public Map<String, ItemStack> getMappings()
	{
		return Collections.unmodifiableMap(mMappings);
	}
	
	public boolean applyMapping(ItemStack input)
	{
		int oreId = OreDictionary.getOreID(input);
		
		if(oreId != -1)
		{
			ItemStack mapping = mMappings.get(OreDictionary.getOreName(oreId));
			if(mapping == null)
				return false;
			
			// Apply it directly
			input.itemID = mapping.itemID;
			input.stackTagCompound = mapping.stackTagCompound;
			input.setItemDamage(mapping.getItemDamage());

			return true;
		}
		
		return false;
	}
	
	public boolean hasMapping(ItemStack input)
	{
		int oreId = OreDictionary.getOreID(input);
		
		if(oreId != -1)
			return mMappings.containsKey(OreDictionary.getOreName(oreId));
		
		return false;
	}
}
