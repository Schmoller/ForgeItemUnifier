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
		if(!canSendPacket(packet))
			return;
		PacketDispatcher.sendPacketToServer(toPacket(packet));
	}
	
	@Override
	public void sendPacketToAllClients( ModPacket packet )
	{
		if(!canSendPacket(packet))
			return;
		// Integrated server
		if(Utilities.canAccessServerSide())
			PacketDispatcher.sendPacketToAllPlayers(toPacket(packet));
	}
	
	@Override
	public void sendPacketToClient( ModPacket packet, EntityPlayer player )
	{
		if(!canSendPacket(packet))
			return;
		// Integrated server
		if(Utilities.canAccessServerSide())
			PacketDispatcher.sendPacketToPlayer(toPacket(packet), (Player)player);
	}
	
	@Override
	public void sendPacketToWorld( ModPacket packet, World world )
	{
		if(!canSendPacket(packet))
			return;
		// Integrated server
		if(Utilities.canAccessServerSide())
			PacketDispatcher.sendPacketToAllInDimension(toPacket(packet), world.getWorldInfo().getDimension());
	}
}
