package net.edgecraft.edgecore.mod;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.CommandHandler;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class AliasesCommand extends AbstractCommand {

	private final CommandHandler commands = EdgeCoreAPI.commandsAPI();
	
	@Override
	public String[] getNames() {
		String[] aliases = { "aliases" };
		return aliases;
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
	public void sendUsage(CommandSender sender) {
		sender.sendMessage( EdgeCore.usageColor + "/aliases COMMAND" );
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args){
		
		AbstractCommand cmd = commands.getCommand( args[1] );
		
		if( cmd == null ) {
			player.sendMessage( EdgeCore.errorColor + "Command " + args[1] + " not found!" );
			return false;
		}
		
		StringBuilder aliases = new StringBuilder();
		
		for( String alias : cmd.getNames() ) {
			if( aliases.length() == 0 ) aliases.append( "[ " + alias );
			else aliases.append( ", " + alias );
		}
		aliases.append(" ]");
		
		player.sendMessage( "Aliases of command '" + args[1] + "': " + aliases.toString() );
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}

}
