package schmoller.unifier.mods.appeng;

import java.util.List;

import appeng.common.AppEngApi;
import appeng.common.registries.entries.AppEngGrinderRecipe;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class GrinderProcessor implements IProcessor
{
	@Override
	public String getName()
	{
		return "Grinder";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		List<AppEngGrinderRecipe> recipes = AppEngApi.GrinderRecipes.getRecipes();
		
		int count = 0;
		for(AppEngGrinderRecipe recipe : recipes)
		{
			if(mappings.applyMapping(recipe.getOutput()))
				++count;
		}

		return count;
	}

}
