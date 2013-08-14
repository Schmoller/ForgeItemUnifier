package schmoller.unifier.mods.mekanism;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import mekanism.api.InfusionOutput;
import mekanism.common.RecipeHandler.Recipe;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public abstract class MekanismBaseProcessor implements IProcessor
{
	protected int apply(Mappings mappings, Recipe recipeType)
	{
		int count = 0;
		for(Entry<Object, Object> entry : ((Map<Object, Object>)recipeType.get()).entrySet())
		{
			if(entry.getValue() instanceof ItemStack)
			{
				if(mappings.applyMapping((ItemStack)entry.getValue()))
					++count;
			}
			else if(entry.getValue() instanceof InfusionOutput)
			{
				if(mappings.applyMapping(((InfusionOutput)entry.getValue()).resource))
					++count;
			}
		}
		
		return count;
	}
}
