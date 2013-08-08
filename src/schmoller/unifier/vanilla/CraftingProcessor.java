package schmoller.unifier.vanilla;

import java.util.List;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class CraftingProcessor implements IProcessor
{
	@Override
	public int applyMappings(Mappings mappings)
	{
		int count = 0;
		for(IRecipe recipe : (List<IRecipe>)CraftingManager.getInstance().getRecipeList())
		{
			if(mappings.applyMapping(recipe.getRecipeOutput()))
				++count;
		}
		
		return count;
	}

	@Override
	public String getName()
	{
		return "Crafting";
	}

}
