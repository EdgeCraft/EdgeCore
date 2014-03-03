package net.edgecraft.edgecore.mod;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class KickCommand extends AbstractCommand {

	@Override
	public String[] getNames() {
		String[] names = { "kick" };
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.SUPPORTER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 || args.length == 3 );
	}

	@Override
	public void sendUsage(CommandSender sender) {	
		
		if (sender instanceof Player) {
			
			User u = EdgeCoreAPI.userAPI().getUser(sender.getName());
			
			if (u != null) {
				
				if (!Level.canUse(u, getLevel())) return;
			}
		}
		
		sender.sendMessage( EdgeCore.usageColor + "/kick <name>");
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
        
		Bukkit.getPlayerExact( args[1] ).kickPlayer("You were kicked.");
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}

}
