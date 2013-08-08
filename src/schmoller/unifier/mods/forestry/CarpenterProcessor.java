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
		Map<Object[], Object[]> recipes = RecipeManagers.carpenterManager.getRecipes();
		
		int count = 0;
		
		for(Entry<Object[], Object[]> entry : recipes.entrySet())
		{
			for(Object output : entry.getValue())
			{
				if(output instanceof ItemStack)
				{
					if(mappings.applyMapping((ItemStack)output))
						++count;
				}
			}
		}

		return count;
	}

}
