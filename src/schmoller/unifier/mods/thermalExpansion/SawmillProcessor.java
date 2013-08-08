package schmoller.unifier.mods.thermalExpansion;

import net.minecraft.item.ItemStack;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import schmoller.unifier.Utilities;
import thermalexpansion.api.crafting.ISawmillRecipe;
import thermalexpansion.util.crafting.SawmillManager;

public class SawmillProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Sawmill";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		ISawmillRecipe[] recipes = SawmillManager.getInstance().getRecipeList();
		
		int count = 0;
		for(ISawmillRecipe recipe : recipes)
		{
			ItemStack output1 = Utilities.getPrivateValue(recipe, "primaryOutput");
			ItemStack output2 = Utilities.getPrivateValue(recipe, "secondaryOutput");
			
			if(mappings.applyMapping(output1))
				++count;
			
			if(output2 != null && mappings.applyMapping(output2))
				++count;
		}
		
		return count;
	}

}
