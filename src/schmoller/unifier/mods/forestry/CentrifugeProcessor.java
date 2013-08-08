package schmoller.unifier.mods.forestry;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import forestry.api.recipes.RecipeManagers;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class CentrifugeProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Centrifuge";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		Map<Object[], ItemStack[]> recipes = RecipeManagers.centrifugeManager.getRecipes();
		
		int count = 0;
		
		for(Entry<Object[], ItemStack[]> entry : recipes.entrySet())
		{
			for(ItemStack output : entry.getValue())
			{
				if(mappings.applyMapping(output))
					++count;
			}
		}

		return count;
	}

}
