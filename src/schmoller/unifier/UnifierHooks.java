package schmoller.unifier;

import java.util.List;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.FMLLog;

public class UnifierHooks
{
	public static void testHook()
	{
		FMLLog.severe("Test hook has been triggered. YAY!");
	}
	
	/**
	 * Called upon initial spawn and load of villagers
	 * @param list
	 */
	public static void onMerchantLoad(MerchantRecipeList list)
	{
		for(MerchantRecipe recipe : (List<MerchantRecipe>)list)
			ModForgeUnifier.mappings.applyMapping(recipe.getItemToSell());

		FMLLog.severe("Merchant Load " + list.toString());
	}
	
	public static void onMainMenuPopulate(GuiMainMenu menu)
	{
		
	}
}
