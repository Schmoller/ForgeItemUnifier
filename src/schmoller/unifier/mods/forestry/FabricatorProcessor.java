package schmoller.unifier.mods.forestry;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import forestry.api.recipes.RecipeManagers;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class FabricatorProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Fabricator";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		Map<Object[], Object[]> recipes = RecipeManagers.fabricatorManager.getRecipes();
		
		int count = 0;
		
		for(Entry<Object[], Object[]> entry : recipes.entrySet())
		{
			ItemStack output = (ItemStack)entry.getValue()[0];
			if(mappings.applyMapping(output))
				++count;
		}

		return count;
	}

}
