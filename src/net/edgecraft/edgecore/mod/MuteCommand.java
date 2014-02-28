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

	@Override
	public String[] getNames() {
		
		String[] names = { "mute", "unmute" };
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.TEAM;
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
			return true;
		}
		
		else if( args[0].equalsIgnoreCase("unmute") ) {
			target.setMuted( false );
			return true;
		}
		
		sendUsage( sender );
		return true;
	}
	
}
