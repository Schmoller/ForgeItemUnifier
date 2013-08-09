package schmoller.unifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import schmoller.unifier.packets.ModPacketChangeMapping;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

public class Mappings
{
	private IdentityHashMap<ItemStack, ItemStack> mOriginals = new IdentityHashMap<ItemStack, ItemStack>();
	
	private HashMap<String, ItemStack> mMappings = new HashMap<String, ItemStack>();
	private HashMap<String, ItemStack> mPendingMappings = null;
	
	// All entries never allowed to be simplified
	public static List<String> mBlackList = Arrays.asList(new String[] {"logWood","plankWood","slabWood","stairWood","stickWood","treeSapling","treeLeaves", "itemRecord"});
	
	public static Set<String> getMappableOres()
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
	public static Set<String> getMappableOres(OreCategory category)
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
	
	/**
	 * Gets the current mapping of an ore name to item.
	 */
	public ItemStack getMapping(String name)
	{
		if(mPendingMappings != null && mPendingMappings.containsKey(name))
		{
			ItemStack item = mPendingMappings.get(name);
			if(item.itemID == 0)
				return null;
			return item;
		}
		
		return mMappings.get(name);
	}

	/**
	 * Modifies a mapping
	 * @param item The new item to use. Set to type 0:0 to remove a mapping
	 */
	public void changeMapping(String oreName, ItemStack item)
	{
		// Check that it is allowed
		int oreId = OreDictionary.getOreID(item);
		
		if(oreId != -1)
		{
			String name = OreDictionary.getOreName(oreId);
			if(!oreName.equals(name))
				throw new IllegalArgumentException("Item is not of the correct category");
		}
		
		if(mPendingMappings != null)
			mPendingMappings.put(oreName, item);
		else
			mMappings.put(oreName, item);
	}
	
	/**
	 * Modifies an itemstack so it now matches the mapped type
	 * @param input
	 * @return
	 */
	public boolean applyMapping(ItemStack input)
	{
		int oreId = OreDictionary.getOreID(input);
		
		if(oreId == -1)
			return false;
		
		ItemStack mapping = mMappings.get(OreDictionary.getOreName(oreId));
		if(mapping == null)
		{
			mapping = mOriginals.remove(input);
			if(mapping != null)
			{
				// Restore the original

				input.itemID = mapping.itemID;
				input.stackTagCompound = mapping.stackTagCompound;
				input.setItemDamage(mapping.getItemDamage());
			}
			return false;
		}
		
		if(input.isItemEqual(mapping) && ItemStack.areItemStackTagsEqual(input, mapping))
			return false;
		
		// Keep a copy of the original so we can revert it if needed
		if(!mOriginals.containsKey(input))
			mOriginals.put(input, input.copy());
		
		// Apply it directly
		input.itemID = mapping.itemID;
		input.stackTagCompound = mapping.stackTagCompound;
		input.setItemDamage(mapping.getItemDamage());

		return true;
	}
	
	public boolean hasMapping(ItemStack input)
	{
		int oreId = OreDictionary.getOreID(input);
		
		if(oreId != -1)
			return mMappings.containsKey(OreDictionary.getOreName(oreId));
		
		return false;
	}
	
	public void write(NBTTagCompound root)
	{
		NBTTagList map = new NBTTagList();
		for(Entry<String, ItemStack> entry : mMappings.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("name", entry.getKey());
			tag.setInteger("id", entry.getValue().itemID);
			tag.setInteger("data", entry.getValue().getItemDamage());
			map.appendTag(tag);
		}
		
		root.setTag("mappings", map);
	}
	
	public void read(NBTTagCompound root)
	{
		mMappings.clear();
		
		NBTTagList map = root.getTagList("mappings");
		
		for(int i = 0; i < map.tagCount(); ++i)
		{
			NBTTagCompound tag = (NBTTagCompound)map.tagAt(i);
			
			String name = tag.getString("name");
			
			int id = tag.getInteger("id");
			int data = tag.getInteger("data");
			
			ItemStack item = new ItemStack(id, 1, data);
			
			if(item.getItem() == null)
			{
				ModForgeUnifier.log.severe(String.format("Unknown item [%d:%d] used for %s. Ignoring", id, data, name));
				continue;
			}
			
			int oreId = OreDictionary.getOreID(item);
			
			if(oreId == -1)
			{
				ModForgeUnifier.log.severe(String.format("Item [%d:%d] used for %s is not in the ore dictionary. Ignoring", id, data, name));
				continue;
			}
			
			String oreName = OreDictionary.getOreName(oreId);
			if(!name.equalsIgnoreCase(oreName))
			{
				ModForgeUnifier.log.severe(String.format("Item [%d:%d] used for %s has the ore type %s. Ignoring", id, data, name, oreName));
				continue;
			}
			
			mMappings.put(name, item);
		}
	}
	
	public void beingModify()
	{
		mPendingMappings = new HashMap<String, ItemStack>();
	}
	
	public void endModify()
	{
		for(Entry<String, ItemStack> entry : mPendingMappings.entrySet())
		{
			if(entry.getValue().itemID == 0)
				mMappings.remove(entry.getKey());
			else
				mMappings.put(entry.getKey(), entry.getValue());
		}
		
		if(!FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer() && FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			ModPacketChangeMapping mappings = new ModPacketChangeMapping();
			mappings.newMappings = (HashMap<String, ItemStack>) mPendingMappings.clone();
			
			ModForgeUnifier.packetHandler.sendPacketToServer(mappings);
		}
		else
		{
			ModForgeUnifier.saveMappings();
		}
		
		ModForgeUnifier.manager.execute(this);
		
		mPendingMappings = null;
	}
}
