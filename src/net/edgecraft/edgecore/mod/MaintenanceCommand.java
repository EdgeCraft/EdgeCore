package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaintenanceCommand extends AbstractCommand {

	private static MaintenanceCommand instance = new MaintenanceCommand();
	
	private MaintenanceCommand() { /* ... */ }
	
	public static final MaintenanceCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "maintenance" };
	}

	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return (args.length == 1);
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		sender.sendMessage(EdgeCore.usageColor + "/maintenance");
		return;
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		String userLang = user.getLanguage();
		
		maintenance();
		player.sendMessage(ChatColor.GREEN + "All users have been kicked due maintenance!");
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		
		maintenance();
		sender.sendMessage(EdgeCore.sysColor + "All users have been kicked due maintenance!");
		
		return true;
	}	
	
	private void maintenance() {
		
		for ( Player p : Bukkit.getServer().getOnlinePlayers() ) {			
			User u = EdgeCore.getUsers().getUser( p.getName() );
			
			if ( u != null && !Level.canUse( u, Level.ARCHITECT ) ) {
				u.getPlayer().kickPlayer("Server is in maintenance!");
			}
		}
		return;
	}
}
