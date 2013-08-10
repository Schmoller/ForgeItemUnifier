package schmoller.unifier;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandUnifier extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "unifier";
	}

	@Override
	public boolean canCommandSenderUseCommand( ICommandSender sender )
	{
		if(sender instanceof EntityPlayer)
			return true;
		
		return false;
	}
	
	@Override
	public String getCommandUsage( ICommandSender par1iCommandSender )
	{
		return "/unifier";
	}
	
	
	@Override
	public void processCommand( ICommandSender sender, String[] args )
	{
		EntityPlayer player = (EntityPlayer)sender;
		
		ModForgeUnifier.openGui(player, ModForgeUnifier.canPlayerEdit(player));
	}
	
	

}
