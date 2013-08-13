package schmoller.unifier.vanilla;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class VillagerTradeProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Villager Trading";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int count = 0;
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			if(FMLClientHandler.instance().getClient().theWorld != null)
			{
				List<Entity> list = FMLClientHandler.instance().getClient().theWorld.loadedEntityList;
				count += apply(mappings, list);
			}
		}
		
		if(FMLCommonHandler.instance().getMinecraftServerInstance() != null)
		{
			for(WorldServer world : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers)
			{
				List<Entity> list = world.loadedEntityList;
				count += apply(mappings, list);
			}
		}

		return count;
	}
	
	private int apply(Mappings mappings, List<Entity> entities)
	{
		int count = 0;
		for(Entity entity : entities)
		{
			if(entity instanceof EntityVillager)
			{
				MerchantRecipeList recipes = ((EntityVillager)entity).getRecipes(null);
				
				for(MerchantRecipe recipe : (List<MerchantRecipe>)recipes)
				{
					if(mappings.applyMapping(recipe.getItemToSell()))
						++count;
					
					if(mappings.applyMapping(recipe.getItemToBuy()))
						++count;
					
					if(recipe.getSecondItemToBuy() != null)
					{
						if(mappings.applyMapping(recipe.getSecondItemToBuy()))
							++count;
					}
				}
			}
		}
		
		return count;
	}

}
