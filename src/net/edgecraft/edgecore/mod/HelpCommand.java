package net.edgecraft.edgecore.mod;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.CommandContainer;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class HelpCommand extends AbstractCommand {

	private static final HelpCommand instance = new HelpCommand();
	
	private HelpCommand() { /* ... */ }
	
	public static final HelpCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "help", "usage" };
	}

	@Override
	public Level getLevel() {
		return Level.USER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 );
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage(EdgeCore.usageColor + "/usage <command>");
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		

		AbstractCommand cmd = commands.getCommand( args[1] );

		
		if( cmd instanceof CommandContainer ) {
			cmd = ((CommandContainer) cmd).getCommand( args[1] );
		}
		
		if( cmd == null ) {
			player.sendMessage( lang.getColoredMessage( user.getLang(), "cmd_not_found" ) );
			return false;
		}
		
		if( !Level.canUse( user, cmd.getLevel() ) )
		{
			player.sendMessage( lang.getColoredMessage( user.getLang(), "nopermission" ) );
		}
		
		player.sendMessage( "Usage-Instructions for command " + args[1] + ":" );
		cmd.sendUsage( player );
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}
}
