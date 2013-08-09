package schmoller.unifier.packets;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import schmoller.network.ModPacket;

public class ModPacketOpenGui extends ModPacket
{
	public boolean allowEdit;
	
	public ModPacketOpenGui()
	{
		
	}
	
	public ModPacketOpenGui(boolean allowEdit)
	{
		this.allowEdit = allowEdit;
	}
	
	@Override
	public void write( DataOutput output ) throws IOException
	{
		output.writeBoolean(allowEdit);
	}

	@Override
	public void read( DataInput input ) throws IOException
	{
		allowEdit = input.readBoolean();
	}
}
