package net.edgecraft.edgecore.mod;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class BanCommand extends AbstractCommand {

	private static final BanCommand instance = new BanCommand();
	
	private BanCommand() { /* ... */ }
	
	public static final BanCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "ban", "unban" };
	}

	@Override
	public Level getLevel() {
		return Level.SUPPORTER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 || args.length == 3 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		sender.sendMessage( EdgeCore.usageColor + "/ban <name> <reason>");
		sender.sendMessage( EdgeCore.usageColor + "/unban <player>");
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		String userLang = user.getLanguage();
		
		if( args[0].equalsIgnoreCase("ban") ) {
			if( args.length != 3 ) {
				sendUsage( player );
				return true;
			}
			
			User ban = EdgeCoreAPI.userAPI().getUser( args[1] );
			ban.setBanned(true);
			ban.updateBanReason( args[2] );
			return true;
		}
		
		if( args[0].equalsIgnoreCase( "unban" ) ) {
			if( args.length != 2 ) {
				sendUsage(player);
				return true;
			}
			
			User unban = EdgeCoreAPI.userAPI().getUser( args[1] );
			unban.setBanned( false );
			unban.updateBanReason( "" );
			return true;
		}
		
		sendUsage( player );
		return true;
		
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true; // ban by console?
	}

}
