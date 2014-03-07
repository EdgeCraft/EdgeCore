package net.edgecraft.edgecore.mod;

import java.util.Map;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand extends AbstractCommand {

	private static final MuteCommand instance = new MuteCommand();
	
	private MuteCommand() { /* ... */ }
	
	public static final MuteCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "mute", "unmute" };
	}

	@Override
	public Level getLevel() {
		return Level.SUPPORTER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		if( sender instanceof Player ) {
			User u = EdgeCoreAPI.userAPI().getUser( sender.getName() );
			
			if( !Level.canUse(u, getLevel()) ) return;
		}
		
		sender.sendMessage( EdgeCore.usageColor + "/mute list" );
		sender.sendMessage( EdgeCore.usageColor + "/mute <player>" );
		sender.sendMessage( EdgeCore.usageColor + "/unmute <player>");
		return;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		String userLang = user.getLanguage();
		
		if (!Level.canUse(user, getLevel())) {
			player.sendMessage(lang.getColoredMessage(userLang, "nopermission"));
			return true;
		}
		
		return mute(player, args);
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return mute(sender, args);
	}

	private boolean mute(CommandSender sender, String[] args){
		
		if( args[0].equalsIgnoreCase("mute") && args[1].equalsIgnoreCase("list") ) {
			
			for( Map.Entry<Integer, User> entry : EdgeCoreAPI.userAPI().getUsers().entrySet() ) {
				User cur = entry.getValue();
				
				if( cur.isMuted() )
						sender.sendMessage( cur.getName() );
			}
			
			return true;
		}
		
		User target = EdgeCoreAPI.userAPI().getUser( args[1] );
		
		if(target == null){
			
			sender.sendMessage(EdgeCore.errorColor + "Spieler ist nicht online!");
			return true;
		}
		
		
		if( args[0].equalsIgnoreCase("mute") ) { 
			target.setMuted( true );
			sender.sendMessage("Muted " + args[1] );
			return true;
		}
		
		else if( args[0].equalsIgnoreCase("unmute") ) {
			target.setMuted( false );
			sender.sendMessage("Unmuted " + args[1] );
			return true;
		}
		
		sendUsage( sender );
		return true;
	}
	
}
