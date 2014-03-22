package net.edgecraft.edgecore.mod;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class SetSpawnCommand extends AbstractCommand {
	
	private static SetSpawnCommand instance = new SetSpawnCommand();
	
	private SetSpawnCommand() { super(); }
	
	public static final SetSpawnCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[] { "setspawn" };
	}

	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return args.length == 1;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
		player.sendMessage(lang.getColoredMessage(user.getLanguage(), "mod_setspawn_success").replace("[0]", player.getWorld().getName()));
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {	
		return true;
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		sender.sendMessage(EdgeCore.usageColor + "/setspawn");
	}
	
}
