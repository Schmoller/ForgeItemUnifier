package schmoller.unifier.mods.thaumcraft;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import schmoller.unifier.Utilities;
import thaumcraft.api.ThaumcraftApi;

public class SmelterBonusProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Infernal Furnace";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int remapCount = 0;
		
		Map<List, ItemStack> smeltingBonus = Utilities.getPrivateValue(ThaumcraftApi.class, null, "smeltingBonus");
		
		for(Entry<List,ItemStack> bonus : smeltingBonus.entrySet())
		{
			if(mappings.applyMapping(bonus.getValue()))
				++remapCount;
		}
		
		return remapCount;
	}

}
