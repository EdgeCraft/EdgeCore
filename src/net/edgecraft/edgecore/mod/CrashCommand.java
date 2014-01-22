package net.edgecraft.edgecore.mod;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class CrashCommand extends AbstractCommand {

	@Override
	public String[] getNames() {
		String[] names = { "crash" };
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.ADMIN;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		if( sender instanceof Player ) {
			User u = EdgeCoreAPI.userAPI().getUser( ((Player)sender).getName() );
			
			if( !Level.canUse(u, getLevel() )) return;
		}
		
		sender.sendMessage( EdgeCore.usageColor + "/crash <player>");
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		return crash(player);
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		
		return crash( EdgeCoreAPI.userAPI().getUser( args[1] ).getPlayer() );
	}

	@SuppressWarnings("deprecation")
	private final boolean crash( Player player ) {
		
		player.sendBlockChange( player.getLocation() , -1, (byte) -1); //TODO: New Command?
		return true;
	}
	
}
