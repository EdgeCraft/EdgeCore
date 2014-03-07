package net.edgecraft.edgecore.mod;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class GameModeCommand extends AbstractCommand {

	@Override
	public String[] getNames() {
		return new String[]{ "gamemode", "gm" };
	}

	@Override
	public Level getLevel() {
		return Level.MODERATOR;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		
		return ( args.length == 2 || args.length == 3 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		if( sender instanceof Player ) {
			User u = EdgeCoreAPI.userAPI().getUser( ((Player)sender).getName() );
			
			if( u != null && !Level.canUse(u, getLevel() ) ) return;
		}
		
		sender.sendMessage( EdgeCore.usageColor + "/gamemode <player> [mode]");
		return;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		Player p = Bukkit.getPlayerExact( args[1] );
		
		if( p == null ) {
			player.sendMessage( args[1] + " not found!" );
		}
		
		if( args.length == 2 ) {
			player.sendMessage( p.getGameMode().toString() );
			return true;
		}
		
		if( args[2].equalsIgnoreCase( "creative") || args[2].equals( "1" ) || args[2].equalsIgnoreCase( "c" )) {
			p.setGameMode( GameMode.CREATIVE );
			return true;
		}
		
		if( args[2].equalsIgnoreCase( "adventure") || args[2].equals( "2" ) || args[2].equalsIgnoreCase( "a" )) {
			p.setGameMode( GameMode.ADVENTURE );
			return true;
		}
		
		if( args[2].equalsIgnoreCase( "survival") || args[2].equals( "0" ) || args[2].equalsIgnoreCase( "s" )) {
			p.setGameMode( GameMode.SURVIVAL );
			return true;
		}
		
		sendUsage( player );
		return true;
		
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		sendUsage(sender);
		return true;
	}

}
