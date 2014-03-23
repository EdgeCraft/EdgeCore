package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionCommand extends AbstractCommand {
	
	private static final PermissionCommand instance = new PermissionCommand();
	
	private PermissionCommand() { super(); }
	
	public static final PermissionCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "permission", "perm" };
	}

	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		
		return ( args.length >= 2 && args.length <= 4 );
	}
	

	@Override
	public void sendUsageImpl( CommandSender sender ) {
		
			sender.sendMessage( EdgeCore.usageColor + "/permission setlevel <player> <level>");
			sender.sendMessage( EdgeCore.usageColor + "/permission getlevel <player>");
			sender.sendMessage( EdgeCore.usageColor + "/permission listranks" );
			return;
	}

	@Override
	public boolean sysAccess( CommandSender sender, String[] args) {
		
		return permission(sender, args, true);
		
	}
	
	@Override
	public boolean runImpl(Player player, User user, String[] args) throws NumberFormatException, Exception {
				
		return permission(player, args, false);
		
	}
	
	private boolean permission(CommandSender sender, String[] args, boolean console) {
		
		User player = users.getUser(sender.getName());
		
		if (player == null) {
			sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "globalerror"));
			return true;
		}
		
		if (args[1].equalsIgnoreCase("listranks") || args[1].equalsIgnoreCase("listlevel")) {
			if (args.length != 2) {
				sendUsage(sender);
				return true;
			}
			
			listRanks(sender);
			
			return true;
		}
		
		if (args[1].equalsIgnoreCase("getlevel") || args[1].equalsIgnoreCase("getrank")) {
			if (args.length != 3) {
				sendUsage(sender);
				return true;
			}
			
			User user = users.getUser(args[2]);
			
			if (console) {
				
				if (user == null) {
					sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "notfound"));
					return true;
				}
				
			} else {
				
				if (user == null) {
					sender.sendMessage(lang.getColoredMessage(player.getLanguage(), "notfound"));
					return true;
				}
				
			}
			
			return getRank(sender, user.getName());
		}
		
		if (args[1].equalsIgnoreCase("setlevel") || args[1].equalsIgnoreCase("setrank")) {
			if (args.length != 4) {
				sendUsage(sender);
				return true;
			}
			
			try {
				
				User user = users.getUser(args[2]);
				int level = Integer.parseInt(args[3]);
			
				if (console) {
					
					if (user == null) {
						sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "notfound"));
						return true;
					}
					
				} else {
					
					if (user == null) {
						sender.sendMessage(lang.getColoredMessage(player.getLanguage(), "notfound"));
						return true;
					}
					
				}
				
				setRank(sender, user.getName(), level);
				
			} catch(NumberFormatException e) {
				sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "numberformatexception"));
			}
		}
		
		return true;
	}
	
	private boolean setRank(CommandSender sender, String name, int level) {
		
		try {
			
			User u = EdgeCoreAPI.userAPI().getUser( name );
			
			u.updateLevel( Level.getInstance(level) );
			sender.sendMessage( ChatColor.GREEN + " Das Level wurde geändert!" );
		
		} catch(Exception e) {
			e.printStackTrace();
			sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "globalerror"));
		}
		
		return true;
	}

	private boolean getRank( CommandSender sender, String name ) {
		
		sender.sendMessage(ChatColor.GREEN + "Rank: " + ChatColor.GRAY + EdgeCoreAPI.userAPI().getUser(name).getLevel());
		
		return true;
	}

	private void listRanks( CommandSender sender ) {
		
		sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Rankings:");
		
		for (Level level : Level.values()) {
			sender.sendMessage(ChatColor.GRAY + level.getName() + " - Level: " + ChatColor.GREEN + level.value());
		}
	}
}
