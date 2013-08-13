package schmoller.unifier;

import java.io.IOException;
import java.util.Map;

import net.minecraft.item.ItemStack;
import schmoller.unifier.packets.ModPacketChangeMapping;

public class NetworkedMappings extends Mappings
{
	@Override
	public void endModify()
	{
		super.endModify();
		
		if(!Utilities.isServer())
		{
			ModPacketChangeMapping mappings = new ModPacketChangeMapping();
			mappings.newMappings = (Map<String, ItemStack>) mPendingMappings.clone();
			
			ModForgeUnifier.packetHandler.sendPacketToServer(mappings);
		}
		else
		{
			ModPacketChangeMapping mappings = new ModPacketChangeMapping();
			mappings.newMappings = (Map<String, ItemStack>) mPendingMappings.clone();
			ModForgeUnifier.packetHandler.sendPacketToAllClients(mappings);
			
			try
			{
				save();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
