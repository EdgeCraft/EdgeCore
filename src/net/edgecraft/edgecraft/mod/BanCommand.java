package net.edgecraft.edgecraft.mod;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecraft.EdgeCraftAPI;
import net.edgecraft.edgecraft.command.AbstractCommand;
import net.edgecraft.edgecraft.command.Level;
import net.edgecraft.edgecraft.user.User;

public class BanCommand extends AbstractCommand {

	@Override
	public String[] getNames() {
		String[] names = { "ban", "unban" };
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.TEAM; // ADMIN?
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 || args.length == 3 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		sender.sendMessage("/ban <name> <reason>");
		sender.sendMessage("/unban <player>");
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		if( args[0].equalsIgnoreCase("ban") ) {
			if( args.length != 3 ) {
				sendUsage( player );
				return true;
			}
			
			User ban = EdgeCraftAPI.userAPI().getUser( args[1] );
			ban.setBanned(true);
			ban.updateBanReason( args[3] );
			return true;
		}
		
		if( args[0].equalsIgnoreCase( "unban" ) ) {
			if( args.length != 2 ) {
				sendUsage(player);
				return true;
			}
			
			User unban = EdgeCraftAPI.userAPI().getUser( args[1] );
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
