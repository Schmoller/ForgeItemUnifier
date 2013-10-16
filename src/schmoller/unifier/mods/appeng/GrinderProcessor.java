package schmoller.unifier.mods.appeng;

import java.util.List;

import appeng.api.IAppEngGrinderRecipe;
import appeng.common.AppEngApi;
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
		List<IAppEngGrinderRecipe> recipes = AppEngApi.getInstance().getGrinderRecipeManage().getRecipes();
		
		int count = 0;
		for(IAppEngGrinderRecipe recipe : recipes)
		{
			if(mappings.applyMapping(recipe.getOutput()))
				++count;
		}

		return count;
	}

}
