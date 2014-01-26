package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class MuteCommand extends AbstractCommand {

	@Override
	public String[] getNames() {
		
		String[] names = new String[]{
				"mute"
		};
		
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.TEAM;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return args.length >= 2;
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		if( sender instanceof Player ) {
			User u = EdgeCoreAPI.userAPI().getUser( ((Player)sender).getName() );
			
			if( !Level.canUse(u, getLevel()) ) return;
		}
		
		sender.sendMessage( EdgeCore.usageColor + "/mute list" );
		sender.sendMessage( EdgeCore.usageColor + "/mute <player>" );
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		return command(player, args);
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return command(sender, args);
	}

	private boolean command(CommandSender sender, String[] args){
		
		User target = EdgeCoreAPI.userAPI().getUser(args[1]);
		
		if(target == null){
			
			sender.sendMessage(EdgeCore.errorColor + "Spieler ist nicht online!");
			
			return true;
		}
		
		target.setMuted(true);
		
		sender.sendMessage(EdgeCore.sysColor + "Spieler gemuted.");
		
		return true;
	}
	
}
