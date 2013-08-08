package schmoller.unifier.mods.forestry;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import forestry.api.recipes.RecipeManagers;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class CarpenterProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Carpenter";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		Map<Object[], ItemStack> recipes = RecipeManagers.carpenterManager.getRecipes();
		
		int count = 0;
		
		for(Entry<Object[], ItemStack> entry : recipes.entrySet())
		{
			if(mappings.applyMapping(entry.getValue()))
				++count;
		}

		return count;
	}

}
