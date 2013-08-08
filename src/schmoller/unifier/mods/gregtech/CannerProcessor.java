package schmoller.unifier.mods.gregtech;

import gregtechmod.api.util.GT_Recipe;

import java.util.List;

import schmoller.unifier.Mappings;

public class CannerProcessor extends BasicGTProcessor
{
	@Override
	public String getName()
	{
		return "Canner";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		return apply(mappings, (List<GT_Recipe>)GT_Recipe.sCannerRecipes);
	}

}
