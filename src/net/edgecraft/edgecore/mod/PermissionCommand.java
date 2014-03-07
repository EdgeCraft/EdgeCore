package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionCommand extends AbstractCommand {
	
	private static final PermissionCommand instance = new PermissionCommand();
	
	private PermissionCommand() { /* ... */ }
	
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
	public void sendUsage( CommandSender sender ) {
		
		if( !(sender instanceof Player) || !Level.canUse( EdgeCore.getUsers().getUser(((Player)sender).getName()), Level.ADMIN) ) {
			sender.sendMessage( EdgeCore.usageColor + "/permission setlevel <player> <level>");
			sender.sendMessage( EdgeCore.usageColor + "/permission getlevel <player>");
			sender.sendMessage( EdgeCore.usageColor + "/permission listranks" );
		}
		return;
	}

	@Override
	public boolean sysAccess( CommandSender sender, String[] args) {

		try {
			return permission(sender, args);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean runImpl(Player player, User user, String[] args) throws NumberFormatException, Exception {
		
		String userLang = user.getLanguage();
		
		if (!users.exists(args[2])) {
			player.sendMessage(lang.getColoredMessage(userLang, "notfound"));
			return true;
		}
		
		return permission((CommandSender) player, args);
	}
	
	private boolean permission(CommandSender sender, String[] args) throws NumberFormatException, Exception{
		
		switch( args.length ){
		
		case 1:
			return false;
		case 2:
			if( args[1].equalsIgnoreCase( "listranks" ) ) {
				listranks( sender );
				return true;
			} else {
				sendUsage( sender );
				return true;
			}
		case 3:
			if( args[1].equalsIgnoreCase( "getlevel" ) || args[1].equalsIgnoreCase( "getrank" )  ) {
				return getrank(sender, args[2] );
			} else {
				sendUsage( sender );
				return false;
			}
		case 4:
			if( args[1].equalsIgnoreCase("setlevel") || args[1].equalsIgnoreCase( "setrank" ) ){
				users.getUser(args[2]).updatePrefix(Level.getInstance(Integer.parseInt(args[3])).getName());
				return setrank(sender, args[2], args[3] );
			}
			sendUsage( sender );
			return false;
		default:
			sendUsage( sender );
			return false;
		
		}
		
	}
	
	private boolean setrank(CommandSender sender, String name, String level) throws NumberFormatException, Exception {
		
		User u = EdgeCoreAPI.userAPI().getUser( name );
		
		u.updateLevel( Level.getInstance(Integer.valueOf(level)) );
		sender.sendMessage( ChatColor.GREEN + " Changed!" );
		
		return true;
	}

	private boolean getrank( CommandSender sender, String name ) {
		
		sender.sendMessage(ChatColor.GREEN + "Rank: " + ChatColor.GRAY + EdgeCoreAPI.userAPI().getUser(name).getLevel());
		
		return true;
	}

	private void listranks( CommandSender sender ) {
		
		sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Rankings:");
		
		for (Level level : Level.values()) {
			sender.sendMessage(ChatColor.GRAY + level.getName() + " - Level: " + ChatColor.GREEN + level.value());
		}
	}
}
