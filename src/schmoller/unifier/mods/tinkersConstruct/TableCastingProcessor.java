package schmoller.unifier.mods.tinkersConstruct;

import java.util.List;

import tconstruct.TConstruct;
import tconstruct.library.crafting.CastingRecipe;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class TableCastingProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Table Casting";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		List<CastingRecipe> recipes = TConstruct.tableCasting.getCastingRecipes();
		
		int count = 0;
		for(CastingRecipe recipe : recipes)
		{
			if(mappings.applyMapping(recipe.output))
				++count;
		}

		return count;
	}

}
