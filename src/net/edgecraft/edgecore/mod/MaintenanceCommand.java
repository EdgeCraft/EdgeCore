package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaintenanceCommand extends AbstractCommand {

	private static MaintenanceCommand instance = new MaintenanceCommand();
	
	private MaintenanceCommand() { super(); }
	
	public static final MaintenanceCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "maintenance" , "invite" };
	}

	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return (args.length == 1 || args.length == 2);
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage(EdgeCore.usageColor + "/maintenance");
		sender.sendMessage(EdgeCore.usageColor + "/invite <player>");
		
		return;		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		if (args[0].equalsIgnoreCase("maintenance")) {
			if (args.length != 1) {
				sendUsage(player);
				return true;
			}
			
			if (EdgeCore.isMaintenance()) {
				
				EdgeCore.getInstance().setMaintenance(false);
				player.sendMessage(EdgeCore.sysColor + "Die Serverwartung wurde beendet!");
				
			} else {
				
				maintenance();
				
				player.sendMessage(EdgeCore.sysColor + "Der Server befindet sich nun im Wartungsmodus!");
				player.sendMessage(EdgeCore.sysColor + "Um einzelne Spieler reinzulassen: " + ChatColor.GOLD + "/invite <player>");
				
			}
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("invite")) {
			if (args.length != 2) {
				sendUsage(player);
				return true;
			}
			
			if (!users.exists(args[1])) {
				player.sendMessage(lang.getColoredMessage(user.getLang(), "notfound"));
				return true;
			}
			
			if (EdgeCore.getInvitedPlayers().contains(args[1])) {
				
				EdgeCore.getInstance().invite(args[1], true);
				player.sendMessage(EdgeCore.sysColor + "Der Spieler " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " wurde eingeladen!");
				
			} else {
				
				EdgeCore.getInstance().invite(args[1], false);
				player.sendMessage(EdgeCore.sysColor + "Der Spieler " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " wurde ausgeladen!");
				
				if (Bukkit.getPlayer(args[1]) != null)
					Bukkit.getPlayer(args[1]).kickPlayer(ChatColor.RED + "Du wurdest ausgeladen!");
			}
			
			return true;
		}
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		
		if (args[0].equalsIgnoreCase("maintenance")) {
			if (args.length != 1) {
				sendUsage(sender);
				return true;
			}
			
			if (EdgeCore.isMaintenance()) {
				
				EdgeCore.getInstance().setMaintenance(false);
				sender.sendMessage(EdgeCore.sysColor + "Die Serverwartung wurde beendet!");
				
			} else {
				
				maintenance();
				
				sender.sendMessage(EdgeCore.sysColor + "Der Server befindet sich nun im Wartungsmodus!");
				sender.sendMessage(EdgeCore.sysColor + "Um einzelne Spieler reinzulassen: " + ChatColor.GOLD + "/invite <player>");
				
			}
		}
		
		if (args[0].equalsIgnoreCase("invite")) {
			if (args.length != 2) {
				sendUsage(sender);
				return true;
			}
			
			if (!users.exists(args[1])) {
				sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "notfound"));
				return true;
			}
			
			if (!EdgeCore.getInvitedPlayers().contains(args[1])) {
				
				EdgeCore.getInstance().invite(args[1], true);
				sender.sendMessage(EdgeCore.sysColor + "Der Spieler " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " wurde eingeladen!");
				
			} else {
				
				EdgeCore.getInstance().invite(args[1], false);
				sender.sendMessage(EdgeCore.sysColor + "Der Spieler " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " wurde ausgeladen!");
				
				if (Bukkit.getPlayer(args[1]) != null)
					Bukkit.getPlayer(args[1]).kickPlayer(ChatColor.RED + "Du wurdest ausgeladen!");
			}
			
			return true;
		}
		
		return true;
	}	
	
	private void maintenance() {
		
		EdgeCore.getInstance().setMaintenance(true);
		
		for ( Player p : Bukkit.getServer().getOnlinePlayers() ) {			
			User u = EdgeCore.getUsers().getUser( p.getName() );
			
			if ( u != null && !Level.canUse( u, Level.ARCHITECT ) ) {
				u.getPlayer().kickPlayer("Der Server befindet sich im Wartungsmodus!");
			}
		}
		
		return;
	}
}
