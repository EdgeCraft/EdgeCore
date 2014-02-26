package net.edgecraft.edgecore.command;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.db.DatabaseHandler;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.system.EdgeCraftSystem;
import net.edgecraft.edgecore.user.User;
import net.edgecraft.edgecore.user.UserManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public abstract class AbstractCommand {

	
	protected static final UserManager users = EdgeCore.getUsers();
	protected static final LanguageHandler lang = EdgeCore.getLang();
	protected static final DatabaseHandler db = EdgeCore.getDB();
	protected static final EdgeCraftSystem system = EdgeCore.getSystem();
	
	public abstract String[] getNames();
	public abstract Level getLevel();
	
	
	public abstract boolean validArgsRange( String[] args );	
	public abstract void sendUsage( CommandSender sender ); // How to use the command.
	public abstract boolean runImpl( Player player, User user, String[] args ) throws Exception; // Access through Player
	public abstract boolean sysAccess( CommandSender sender, String[] args ); // Access through System.

	
	public final boolean run( CommandSender sender, String[] args ) throws Exception {
		
		if( args.length == 2 ) {
			
			if( args[1].equalsIgnoreCase("help") )
				sendUsage(sender);
			
			else if( args[1].equalsIgnoreCase("aliases")) {
				StringBuilder sb = new StringBuilder();
				for( String alias : getNames() ) {
					sb.append( alias + ", ");
				}
				sb.deleteCharAt(sb.length());
				sender.sendMessage( sb.toString() );
			}
				
			return true;
		}
		
		if( !validArgsRange( args ) ) {
			sendUsage(sender);
			return true;
		}
		
		if( !(sender instanceof Player) ) {
			return sysAccess(sender, args);
		}
		
		
		Player player = (Player) sender;
		User user = users.getUser(player.getName());
		
		if( user == null ) return true;
		
		if( !Level.canUse( user, getLevel() ) ) {
			sender.sendMessage( lang.getColoredMessage(user.getLanguage(), "nopermission"));
			return false;
		}
		
		return runImpl( player, user, args );
			
	}

	public final boolean sysAccess( CommandSender sender ) {
		sendUsage(sender);
		return true;
	}
	
	public final String getName() {
		return getNames()[0];
	}
	
	public final boolean hasAlias( String alias ) {
		for( String name : getNames() ) {
			if( name.equalsIgnoreCase(alias) ) return true;
		}
		
		return false;
	}
}
