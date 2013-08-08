package schmoller.unifier.mods.forestry;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import forestry.api.recipes.RecipeManagers;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class SqueezerProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Squeezer";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		Map<ItemStack[], Object[]> recipes = RecipeManagers.squeezerManager.getRecipes();
		
		int count = 0;
		
		for(Entry<ItemStack[], Object[]> entry : recipes.entrySet())
		{
			ItemStack output = (ItemStack)entry.getValue()[0];
			if(output != null && mappings.applyMapping(output))
				++count;
		}

		return count;
	}

}
