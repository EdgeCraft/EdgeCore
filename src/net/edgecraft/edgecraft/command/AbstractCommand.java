package net.edgecraft.edgecraft.command;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.db.DatabaseHandler;
import net.edgecraft.edgecraft.lang.LanguageHandler;
import net.edgecraft.edgecraft.system.EdgeCraftSystem;
import net.edgecraft.edgecraft.user.User;
import net.edgecraft.edgecraft.user.UserManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements ICommand {

	
	protected static final UserManager users = EdgeCraft.getUsers();
	protected static final LanguageHandler lang = EdgeCraft.getLang();
	protected static final DatabaseHandler db = EdgeCraft.getDB();
	protected static final EdgeCraftSystem system = EdgeCraft.getSystem();
	
	protected AbstractCommand instance;
	
	public AbstractCommand getInstance() {
		return instance;
	}
	
	public final boolean run( CommandSender sender, String[] args ) throws Exception {
		
		if( !(sender instanceof Player) ) {
			return sysAccess(sender, args);
		}
		
		if( !validArgsRange( args ) ) {
			sendUsage(sender);
			return true;
		}
		
		Player player = (Player) sender;
		User user = users.getUser(player.getName());
		
		if( user == null ) return true;
		
		if( !Level.canUse(user, getLevel() )) {
			sender.sendMessage( lang.getColoredMessage(user.getLanguage(), "nopermission"));
			return false;
		}
		
		return runImpl( player, user, args );
			
	}

	public abstract boolean validArgsRange( String[] args );
	
	public abstract boolean sysAccess( CommandSender sender, String[] args );
	
	public final boolean sysAccess( CommandSender sender ) {
		sendUsage(sender);
		return true;
	}

}
