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
	
	private TeleportCommand() { super(); }
	
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
	public void sendUsageImpl(CommandSender sender) {
		
		if( sender instanceof Player ) {
			User u = EdgeCoreAPI.userAPI().getUser( ((Player)sender).getName() );
			
			if( u == null || !Level.canUse(u, getLevel()) ) return;
		}
		
		sender.sendMessage(EdgeCore.usageColor + "/teleport <target>");
		sender.sendMessage(EdgeCore.usageColor + "/teleport <x> <y> <z>");
		sender.sendMessage(EdgeCore.usageColor + "/teleport <player> <target>");
		sender.sendMessage(EdgeCore.usageColor + "/teleport <player> <x> <y> <z>");
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		if (!validArgsRange(args)) {
			sendUsage(player);
			return true;
		}
		
		Player tp;
		
		if (args.length == 2) {
			
			tp = Bukkit.getPlayer(users.getUser(args[1]).getUUID());
			
			if (tp == null) {
				if (!users.exists(args[1])) {
					player.sendMessage(lang.getColoredMessage(user.getLanguage(), "notfound"));
					return true;
				}
				
				player.teleport(users.getUser(args[1]).getLastLocation());
				player.sendMessage(lang.getColoredMessage(user.getLanguage(), "mod_teleport_lastlocation").replace("[0]", args[1]));
				
				return true;
			}
			
			player.teleport(tp.getLocation());
			// TODO: Add language key @horoking
			
			return true;
		}
		
		if (args.length == 3) {
			
			tp = Bukkit.getPlayer(users.getUser(args[1]).getUUID());
			Player to = Bukkit.getPlayer(users.getUser(args[2]).getUUID());
			
			if (tp == null) {
				player.sendMessage(lang.getColoredMessage(user.getLanguage(), "notfound"));
				return true;
			}
			
			if (to == null) {
				if (!users.exists(args[2])) {
					player.sendMessage(lang.getColoredMessage(user.getLanguage(), "notfound"));
					return true;
				}
				
				tp.teleport(users.getUser(args[2]).getLastLocation());
				player.sendMessage(lang.getColoredMessage(user.getLanguage(), "mod_teleport_lastlocation_other").replace("[0]", tp.getName()).replace("[1]", args[2]));
				
				return true;
			}
			
			tp.teleport(to);
			// TODO: Add language key @horoking
			
			return true;
		}
		
		try { 
			
			if (args.length == 4) {
				
				player.teleport(new Location(player.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
				// TODO: Add language key @horoking
				
				return true;
			}
			
			if (args.length == 5) {
				
				tp = Bukkit.getPlayer(users.getUser(args[1]).getUUID());
				
				if (tp == null) {
					player.sendMessage(lang.getColoredMessage(user.getLanguage(), "notfound"));
					return true;
				}
				
				tp.teleport(new Location(tp.getWorld(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4])));
				// TODO: Add language key @horoking
				
				return true;
			}
			
		} catch(NumberFormatException e) {
			player.sendMessage(lang.getColoredMessage(user.getLanguage(), "numberformatexception"));
		}
		
		return true;	
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		sendUsage(sender);
		return true;
	}

}
