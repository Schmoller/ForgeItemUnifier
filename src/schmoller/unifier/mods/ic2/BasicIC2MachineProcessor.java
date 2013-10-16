package schmoller.unifier.mods.ic2;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public abstract class BasicIC2MachineProcessor implements IProcessor 
{
	protected int apply(Mappings mappings, Map<IRecipeInput, RecipeOutput> recipes)
	{
		int remapCount = 0;
		for(Entry<IRecipeInput, RecipeOutput> recipe : recipes.entrySet())
		{
			for(ItemStack item : (List<ItemStack>)recipe.getValue().items)
			{
				if(mappings.applyMapping(item))
					++remapCount;
			}
		}
		
		return remapCount;
	}
}
