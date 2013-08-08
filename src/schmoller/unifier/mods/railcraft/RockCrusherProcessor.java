package schmoller.unifier.mods.railcraft;

import java.util.List;
import java.util.Map.Entry;

import mods.railcraft.api.crafting.IRockCrusherRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class RockCrusherProcessor implements IProcessor
{
	@Override
	public String getName()
	{
		return "Rock Crusher";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int remapCount = 0;
		
		List<IRockCrusherRecipe> recipeList = RailcraftCraftingManager.rockCrusher.getRecipes();
		
		for(IRockCrusherRecipe recipe : recipeList)
		{
			List<Entry<ItemStack, Float>> outputs = recipe.getOutputs();
			
			for(Entry<ItemStack, Float> output : outputs)
			{
				if(mappings.applyMapping(output.getKey()))
					++remapCount;
			}
		}

		return remapCount;
	}

}
