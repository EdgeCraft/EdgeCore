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

	@Override
	public String[] getNames() {
		String[] names = { "teleport", "tp" };
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.TEAM;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 3 || args.length == 5 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		if( sender instanceof Player ) {
			User u = EdgeCoreAPI.userAPI().getUser( ((Player)sender).getName() );
			
			if( !Level.canUse(u, Level.TEAM) ) return;
		}
		
		sender.sendMessage(EdgeCore.usageColor + "/teleport <player> <target>");
		sender.sendMessage(EdgeCore.usageColor + "/teleport <player> <x> <y> <z>");
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		Player from = Bukkit.getPlayerExact( args[1] );

		if( args.length == 3 ) {
			
			Location to = Bukkit.getPlayerExact( args[2] ).getLocation();
			
			from.getLocation().setX( to.getX() );
			from.getLocation().setY( to.getY() );
			from.getLocation().setZ( to.getZ() );
			return true;
		}
		
		if( args.length == 5 ) {
			
			from.getLocation().setX( Double.valueOf( args[2] ) );
			from.getLocation().setY( Double.valueOf( args[3] ) );
			from.getLocation().setZ( Double.valueOf( args[4] ) );
			
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
