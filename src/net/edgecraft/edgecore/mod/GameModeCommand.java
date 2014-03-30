package net.edgecraft.edgecore.mod;

import java.util.Arrays;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand extends AbstractCommand {

	private static final GameModeCommand instance = new GameModeCommand();
	
	private GameModeCommand() { super(); }
	
	public static final GameModeCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "gamemode", "gm" };
	}

	@Override
	public Level getLevel() {
		return Level.MODERATOR;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		
		return ( args.length == 2 || args.length == 3 );
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage( EdgeCore.usageColor + "/gamemode [player] <mode>");
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		if (!validArgsRange(args)) {
			sendUsage(player);
			return true;
		}
		
		if (args.length == 2) {
			
			setGameMode(player, args[1]);
			
			return true;
		}
		
		if (args.length == 3) {
			
			Player target = Bukkit.getPlayer(args[1]);
			
			if (target == null) {
				player.sendMessage(lang.getColoredMessage(user.getLanguage(), "notfound"));
				return true;
			}
			
			setGameMode(target, args[2]);
			
			return true;
		}
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		sendUsage(sender);
		return true;
	}
	
	private void setGameMode(Player p, String mode) {
		if (p == null || mode == null) return;
		
		String[] validInput = new String[] { "survival", "s", "0", "creative", "c", "1", "adventure", "a", "2" };
		
		if (!Arrays.asList(validInput).contains(mode))
			return;
		
		
	}
}
