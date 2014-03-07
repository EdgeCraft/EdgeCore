package net.edgecraft.edgecore.mod;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class KillCommand extends AbstractCommand {

	@Override
	public String[] getNames() {
		return new String[]{ "kill" };
	}

	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		if( sender instanceof Player ) {
			User u = EdgeCoreAPI.userAPI().getUser( ((Player)sender).getName() );
			
			if( u == null || !Level.canUse(u, getLevel()) ) return;
		}
		sender.sendMessage( EdgeCore.usageColor + "/kill <player>" );
		return;
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		Bukkit.getPlayerExact( args[1] ).setHealth( 0 );
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}

}
