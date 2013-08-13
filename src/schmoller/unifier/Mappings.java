package schmoller.unifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import cpw.mods.fml.common.network.Player;

import schmoller.unifier.packets.ModPacketChangeMapping;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Mappings
{
	private IdentityHashMap<ItemStack, ItemStack> mOriginals = new IdentityHashMap<ItemStack, ItemStack>();
	
	protected HashMap<String, ItemStack> mMappings = new HashMap<String, ItemStack>();
	protected HashMap<String, ItemStack> mPendingMappings = null;
	
	private Mappings mParent = null;
	protected boolean mShouldExecute = true;
	private boolean mEditing = false;
	
	// All entries never allowed to be simplified
	public static List<String> mBlackList = Arrays.asList(new String[] {"logWood","plankWood","slabWood","stairWood","stickWood","treeSapling","treeLeaves", "itemRecord"});
	
	private static boolean mHasSafeguarded = false;
	
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
		if(mEditing && mPendingMappings.containsKey(name))
		{
			ItemStack item = mPendingMappings.get(name);
			if(item.itemID == -1)
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
		if(mEditing)
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
		
		String oreName = OreDictionary.getOreName(oreId);
		ItemStack mapping = mMappings.get(oreName);
		if(mParent != null && mapping == null)
			mapping = mParent.mMappings.get(oreName);
		
		if(mapping == null || mapping.itemID == 0)
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
	
	public void save() throws IOException {}
	
	public void load() throws IOException {}
	
	public void beingModify()
	{
		mPendingMappings = new HashMap<String, ItemStack>();
		mEditing = true;
	}
	
	public void endModify()
	{
		mEditing = false;
		
		for(Entry<String, ItemStack> entry : mPendingMappings.entrySet())
		{
			if(entry.getValue().itemID == -1 || (entry.getValue().itemID == 0 && mParent == null))
				mMappings.remove(entry.getKey());
			else
				mMappings.put(entry.getKey(), entry.getValue());
		}
		
		if(mShouldExecute)
			ModForgeUnifier.manager.execute(this);
	}
	
	public void applyChanges(ModPacketChangeMapping changes, Player sender)
	{
		for(Entry<String, ItemStack> entry : changes.newMappings.entrySet())
		{
			if(entry.getValue().itemID == 0)
				mMappings.remove(entry.getKey());
			else
				mMappings.put(entry.getKey(), entry.getValue());
		}
		
		if(mShouldExecute)
			ModForgeUnifier.manager.execute(this);
	}
	
	public void restoreOriginals()
	{
		ItemStack blank = new ItemStack(0,0,0);
		for(Entry<String, ItemStack> mapping : mMappings.entrySet())
			mapping.setValue(blank);

		if(mShouldExecute)
			ModForgeUnifier.manager.execute(this);
		
		mMappings.clear();
	}
	
	public ModPacketChangeMapping asPacket()
	{
		ModPacketChangeMapping packet = new ModPacketChangeMapping();
		packet.newMappings = mMappings;
		
		return packet;
	}
	
	/**
	 * Some mods use references of oredict objects directly, this method protects against that
	 */
	public static void safeGuardOreDict()
	{
		if(mHasSafeguarded)
			return;
		
		String[] oreNames = OreDictionary.getOreNames();
		for(String oreName : oreNames)
		{
			ArrayList<ItemStack> ores = OreDictionary.getOres(oreName);
			ArrayList<ItemStack> copy = new ArrayList<ItemStack>(ores.size());

			for(ItemStack ore : ores)
				copy.add(ore.copy());
			
			ores.clear();
			for(ItemStack ore : copy)
				ores.add(ore);
		}
		
		mHasSafeguarded = true;
	}
	
	public Mappings getParent()
	{
		return mParent;
	}
	
	public void setParent(Mappings parent)
	{
		mParent = parent;
	}
}
