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
	
	public PermissionCommand() { /* ... */ }
	
	@Override
	public String[] getNames() {
		String[] names = { "permission", "perm" };
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.ADMIN;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		if( args.length < 2 || args.length > 4 ) return false;
		
		return true;
	}

	@Override
	public boolean sysAccess( CommandSender sender, String[] args) {
		listranks(sender);
		return true;
	}
	
	@Override
	public boolean runImpl(Player player, User user, String[] args) throws NumberFormatException, Exception {
		
		
		switch( args.length ){
		
		case 1:
			return false;
		case 2:
			if( args[1].equalsIgnoreCase( "listranks" ) ) {
				listranks( player );
				return true;
			}
			break;
		case 3:
			if( args[1].equalsIgnoreCase( "getlevel" ) ) {
				return getrank( player, args[2] );
			} else {
				return false;
			}
		case 4:
			if( args[1].equalsIgnoreCase("setlevel") ){
				return setrank( player, args[2], args[3] );
			}
			return false;
		default:
			return false;
		
		}
		
		return true;
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
		
		sender.sendMessage(ChatColor.GREEN + "Rankings:");
		sender.sendMessage(ChatColor.GRAY + "Admin - 15");
		sender.sendMessage(ChatColor.GRAY + "Team - 10");
		sender.sendMessage(ChatColor.GRAY + "Architekt - 5");
		sender.sendMessage(ChatColor.GRAY + "User - 1");
		sender.sendMessage(ChatColor.GRAY + "Gast - 0");
	}

	@Override
	public void sendUsage( CommandSender sender ) {
		
		if( !(sender instanceof Player) || Level.canUse( EdgeCore.getUsers().getUser(((Player)sender).getName()), Level.ADMIN) ) {
			sender.sendMessage( EdgeCore.usageColor + "/permission setlevel <player> <level>");
			sender.sendMessage( EdgeCore.usageColor + "/permission getlevel <player>");
			sender.sendMessage( EdgeCore.usageColor + "/permission listranks" );
		}
		return;
	}
}
