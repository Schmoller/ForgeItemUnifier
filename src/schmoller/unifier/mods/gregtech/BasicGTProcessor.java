package schmoller.unifier.mods.gregtech;

import java.util.List;

import gregtechmod.api.util.GT_Recipe;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public abstract class BasicGTProcessor implements IProcessor
{
	protected int apply(Mappings mappings, List<GT_Recipe> recipes)
	{
		int count = 0;
		for(GT_Recipe recipe : recipes)
		{
			if(recipe.mOutput1 != null && mappings.applyMapping(recipe.mOutput1))
				++count;
			if(recipe.mOutput2 != null && mappings.applyMapping(recipe.mOutput2))
				++count;
			if(recipe.mOutput3 != null && mappings.applyMapping(recipe.mOutput3))
				++count;
			if(recipe.mOutput4 != null && mappings.applyMapping(recipe.mOutput4))
				++count;
		}
		
		return count;
	}

}
