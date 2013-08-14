package schmoller.unifier.mods.gregtech;

import gregtechmod.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import schmoller.unifier.IProcessor;
import schmoller.unifier.Mappings;

public class GTUnificatorProcessor implements IProcessor
{

	@Override
	public String getName()
	{
		return "GT OreDict Unificator";
	}

	@Override
	public int applyMappings( Mappings mappings )
	{
		for(String oreName : mappings.getMappableOres())
		{
			ItemStack mapping = mappings.getMapping(oreName);
			if(mapping == null && mappings.getParent() != null)
				mapping = mappings.getParent().getMapping(oreName);
			
			if(mapping != null && mapping.itemID > 0)
				GT_OreDictUnificator.override(oreName, mapping);
		}
		
		return 0;
	}

}
