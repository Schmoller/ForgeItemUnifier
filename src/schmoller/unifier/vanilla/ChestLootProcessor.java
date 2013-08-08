package schmoller.unifier.vanilla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.ReflectionHelper;

import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;
import schmoller.unifier.Utilities;

public class ChestLootProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "Chest Loot";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		int remapCount = 0;
		
		// Get the list of all chest types
		HashMap<String, ChestGenHooks> chestInfo;
		chestInfo = Utilities.getPrivateValue(ChestGenHooks.class, null, "chestInfo");
		
		for(Entry<String, ChestGenHooks> chest : chestInfo.entrySet())
		{
			ArrayList<WeightedRandomChestContent> contents = ReflectionHelper.getPrivateValue(ChestGenHooks.class, chest.getValue(), "contents");
			
			for(WeightedRandomChestContent item : contents)
			{
				if(mappings.applyMapping(item.theItemId))
					++remapCount;
			}
		}
		
		return remapCount;
	}

}
