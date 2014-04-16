package net.edgecraft.edgecore.mod;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class KillCommand extends AbstractCommand {

	private static final KillCommand instance = new KillCommand();
	
	private KillCommand() { super(); }
	
	public static final KillCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "kill" };
	}

	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 );
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage( EdgeCore.usageColor + "/kill <player>" );
		return;
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		if (Bukkit.getPlayer(users.getUser(args[1]).getUUID()) == null) {
			player.sendMessage(lang.getColoredMessage(user.getLang(), "notfound"));
			return true;
		}
		
		Bukkit.getPlayer(users.getUser(args[1]).getUUID()).setHealth(0);
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}

}
