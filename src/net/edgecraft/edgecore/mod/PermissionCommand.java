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
			sender.sendMessage( EdgeCore.usageColor + "/permission getcmdlevel <cmd>");
			return;
	}
	
	@Override
	public boolean runImpl(Player player, User user, String[] args) throws NumberFormatException, Exception {
		return permission(player, args);
		
	}
	
	private boolean permission( CommandSender sender, String[] args) 
	{
		final User player = users.getUser( sender.getName() );
		
		if (player == null) {
			sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "notfound"));
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
		
		if( args[1].equalsIgnoreCase( "getcmdlevel" ) ) {
			if( args.length != 3 ) {
				sendUsage( sender );
				return true;
			}
			
			return getCmdRank( sender, args[2] );
		}
		
		if (args[1].equalsIgnoreCase("getlevel") || args[1].equalsIgnoreCase("getrank")) {
			if (args.length != 3) {
				sendUsage(sender);
				return true;
			}
			
			final User user = users.getUser(args[2]);
				
			if (user == null) {
				sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "notfound"));
				return true;
			}
			
			return getRank(sender, user.getName() );
		}
		
		if (args[1].equalsIgnoreCase("setlevel") || args[1].equalsIgnoreCase("setrank")) {
			if (args.length != 4) {
				sendUsage(sender);
				return true;
			}
			
			try {
				
				final User user = users.getUser(args[2]);
				int level = Integer.parseInt(args[3]);
				
				if( user == null ) {
					sender.sendMessage( lang.getColoredMessage( users.getUser( sender.getName() ).getLang(), "notfound" ) );
					return false;
				}
				
				if ( Level.getInstance(level) == null) {
					sender.sendMessage(lang.getColoredMessage(users.getUser( sender.getName() ).getLang(), "mod_permission_setlevel_nolevel"));
					return false;
				}
				
				setRank( sender, user.getName(), level );
				
			} catch(NumberFormatException e) {
				sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "numberformatexception"));
			}
		}
		
		return true;
	}
	
	private boolean setRank(CommandSender sender, String name, int level) {
		
		try {
			
			final User u = users.getUser( name );
			
			if( u == null ) {
				sender.sendMessage( lang.getColoredMessage( users.getUser( sender.getName()).getLang(), "notfound" ) );
				return true;
			}
			
			u.updateLevel( Level.getInstance(level) );
			u.updatePrefix(u.getLevel().getName());
			
			if (Bukkit.getPlayer(users.getUser(name).getUUID()) != null)
				Bukkit.getPlayer(users.getUser(name).getUUID()).setPlayerListName(u.getLevel().getColor() + name);
			
			sender.sendMessage( lang.getColoredMessage( users.getUser( sender.getName() ).getLang(), "mod_permission_setlevel_success" ).replace("[0]", name).replace("[1]", Level.getInstance(level).getRawName()) );
		
		} catch(Exception e) {
			e.printStackTrace();
			sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "globalerror"));
		}
		
		return true;
	}

	private boolean getRank( CommandSender sender, String name ) {
		
		sender.sendMessage(ChatColor.GREEN + "Rank: " + ChatColor.GRAY + users.getUser(name).getLevel() );
		
		return true;
	}
	
	private boolean getCmdRank( CommandSender sender, String cmdName ) {
		
		final AbstractCommand cmd = commands.getCommand( cmdName );
		
		if( cmd == null ) {
			sender.sendMessage( lang.getColoredMessage( users.getUser(sender.getName()).getLang(), "cmd_not_found" ) );
			return true;
		}
		
		sender.sendMessage( ChatColor.GREEN + "Rank: " + cmd.getLevel().toString() );
		return true;
	}

	private void listRanks( CommandSender sender ) {
		
		sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Rankings:");
		
		for (Level level : Level.values()) {
			sender.sendMessage(ChatColor.GRAY + level.getName() + " - Level: " + ChatColor.GREEN + level.value());
		}
	}
}
