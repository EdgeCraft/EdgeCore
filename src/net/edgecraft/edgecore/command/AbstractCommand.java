package net.edgecraft.edgecore.command;

import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.chat.ChatHandler;
import net.edgecraft.edgecore.db.DatabaseHandler;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.mod.TicketManager;
import net.edgecraft.edgecore.system.EdgeCraftSystem;
import net.edgecraft.edgecore.user.User;
import net.edgecraft.edgecore.user.UserManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public abstract class AbstractCommand {

	
	protected static final UserManager users = EdgeCoreAPI.userAPI();
	protected static final LanguageHandler lang = EdgeCoreAPI.languageAPI();
	protected static final DatabaseHandler db = EdgeCoreAPI.databaseAPI();
	protected static final EdgeCraftSystem system = EdgeCoreAPI.systemAPI();
	protected static final CommandHandler commands = EdgeCoreAPI.commandsAPI();
	protected static final TicketManager tickets = EdgeCoreAPI.ticketAPI();
	protected static final ChatHandler chats = EdgeCoreAPI.chatAPI();
	
	public abstract String[] getNames();
	public abstract Level getLevel();
	
	
	public abstract boolean validArgsRange( String[] args );
	public abstract boolean runImpl( Player player, User user, String[] args ) throws Exception; // Access through Player

	// Overrideable if needed
	public boolean sysAccess( CommandSender sender, String[] args ) {
		
		sender.sendMessage( lang.getColoredMessage( "de" , "noconsole" ) );
		return true;
	}

	public abstract void sendUsageImpl( CommandSender sender ); // How to use the command.

	public void sendUsage( CommandSender sender ) {
		if( sender instanceof Player ) {
			User u = users.getUser( ((Player)sender).getName() );
			
			if (u == null) {
				sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "globalerror"));
				return;
			}
			
			if (!Level.canUse(u, getLevel())) {
				sender.sendMessage(lang.getColoredMessage(u.getLanguage(), "nopermission"));
				return;
			}
			
		}
		
		sendUsageImpl( sender );
	}
	
	public boolean run( CommandSender sender, String[] args ) throws Exception {
		
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
		
		String[] names = getNames();
		
		if( names == null || getNames().length == 0 ) return null;
		
		return getNames()[0];
	}
	
	public final boolean hasAlias( String alias ) {
		for( String name : getNames() ) {
			if( name.equalsIgnoreCase(alias) ) return true;
		}
		
		return false;
	}
}
