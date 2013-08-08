package schmoller.unifier.mods.thermalExpansion;

import net.minecraft.item.ItemStack;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import schmoller.unifier.Utilities;
import thermalexpansion.api.crafting.ISmelterRecipe;
import thermalexpansion.util.crafting.SmelterManager;

public class InductionSmelterProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Induction Furnace";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		ISmelterRecipe[] recipes = SmelterManager.getInstance().getRecipeList();
		
		int count = 0;
		for(ISmelterRecipe recipe : recipes)
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
