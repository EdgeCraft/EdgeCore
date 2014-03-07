package net.edgecraft.edgecore.mod;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class TeleportCommand extends AbstractCommand {

	private static final TeleportCommand instance = new TeleportCommand();
	
	private TeleportCommand() { /* ... */ }
	
	public static final TeleportCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "teleport", "tp" };
	}

	@Override
	public Level getLevel() {
		return Level.SUPPORTER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length >= 2 && args.length <= 5 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		if( sender instanceof Player ) {
			User u = EdgeCoreAPI.userAPI().getUser( ((Player)sender).getName() );
			
			if( u == null || !Level.canUse(u, getLevel()) ) return;
		}
		
		sender.sendMessage( EdgeCore.usageColor + "/teleport <target>" );
		sender.sendMessage( EdgeCore.usageColor + "/teleport <x> <y> <z>");
		sender.sendMessage(EdgeCore.usageColor + "/teleport <player> <target>");
		sender.sendMessage(EdgeCore.usageColor + "/teleport <player> <x> <y> <z>");
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {

		String userLang = user.getLanguage();
		
		if (!Level.canUse(user, getLevel())) {
			player.sendMessage(lang.getColoredMessage(userLang, "nopermission"));
			return true;
		}
		
		if( args.length == 4 ) {
			player.teleport( new Location( player.getWorld(), Double.valueOf( args[1]), Double.valueOf( args[2] ), Double.valueOf(args[3]) ) );
		}
		
		Player from = Bukkit.getPlayer( args[1] );
		
		if( from == null ) {
			player.sendMessage( EdgeCoreAPI.languageAPI().getColoredMessage( user.getLanguage(), "notfound") );
		}
		
		if( args.length == 2 ) {
			
			player.teleport( from.getLocation() );
			return true;
		}
		
		if( args.length == 3 ) {
			
			Player to = Bukkit.getPlayer(args[2]);
			
			if (to == null) {
				player.sendMessage(EdgeCoreAPI.languageAPI().getColoredMessage(user.getLanguage(), "notfound"));
				return false;
			}
			
			from.teleport( to.getLocation() );
			
			return true;
		}
		

		
		if( args.length == 5 ) {
			
			from.teleport( new Location( from.getWorld(), Double.valueOf( args[2] ), Double.valueOf( args[3] ), Double.valueOf( args[4] )));
			
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
