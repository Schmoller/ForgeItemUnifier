package schmoller.unifier.vanilla;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import schmoller.unifier.Utilities;

public class SmeltingProcessor implements IProcessor
{
	@Override
	public int applyMappings( Mappings mappings )
	{
		int count = 0;
		Map<Integer, ItemStack> idList = FurnaceRecipes.smelting().getSmeltingList();
		HashMap<List<Integer>, ItemStack> extendedList;
		
		try
		{
			extendedList = (HashMap<List<Integer>, ItemStack>)Utilities.getPrivateValue(FurnaceRecipes.smelting(), "metaSmeltingList");
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e);
		}
		
		// Remap basic smelting recipes
		for(Entry<Integer, ItemStack> entry : idList.entrySet())
		{
			if(mappings.applyMapping(entry.getValue()))
				++count;
		}
		
		// Remap extended recipes
		for(Entry<List<Integer>, ItemStack> entry : extendedList.entrySet())
		{
			if(mappings.applyMapping(entry.getValue()))
				++count;
		}
		
		return count;
	}
	
	@Override
	public String getName()
	{
		return "Smelting";
	}
}
