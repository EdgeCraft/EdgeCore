package net.edgecraft.edgecraft.mod;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecraft.command.AbstractCommand;
import net.edgecraft.edgecraft.command.Level;
import net.edgecraft.edgecraft.user.User;

public class KickCommand extends AbstractCommand {

	@Override
	public String[] getNames() {
		String[] names = { "kick" };
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.TEAM;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return( args.length == 2 || args.length == 3 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		sender.sendMessage("/kick <name>");
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
        
        if( args.length == 2 ) {
        	user.getPlayer().kickPlayer("You were kicked.");
        	return true;
        }
        
        user.getPlayer().kickPlayer( args[2] );
	    return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		// Kick all?
		return true;
	}

}
