package schmoller.network;

import schmoller.unifier.Utilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ClientPacketManager extends PacketManager
{
	@Override
	public void sendPacketToServer(ModPacket packet)
	{
		PacketDispatcher.sendPacketToServer(toPacket(packet));
	}
	
	@Override
	public void sendPacketToAllClients( ModPacket packet )
	{
		// Integrated server
		if(Utilities.isServer())
			PacketDispatcher.sendPacketToAllPlayers(toPacket(packet));
	}
	
	@Override
	public void sendPacketToClient( ModPacket packet, EntityPlayer player )
	{
		// Integrated server
		if(Utilities.isServer())
			PacketDispatcher.sendPacketToPlayer(toPacket(packet), (Player)player);
	}
	
	@Override
	public void sendPacketToWorld( ModPacket packet, World world )
	{
		// Integrated server
		if(Utilities.isServer())
			PacketDispatcher.sendPacketToAllInDimension(toPacket(packet), world.provider.dimensionId);
	}
}
