package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.CommandContainer;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends AbstractCommand {

	private static final HelpCommand instance = new HelpCommand();
	
	private HelpCommand() { /* ... */ }
	
	public static final HelpCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "help", "usage" };
	}

	@Override
	public Level getLevel() {
		return Level.USER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 1 || args.length == 2 );
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage(EdgeCore.usageColor + "/help [<command>]");
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		if (!validArgsRange(args)) {
			sendUsage(player);
			return true;
		}
		
		if (args.length == 1) {
			
			player.sendMessage("§cCommand Usage: §6/help <command>\n");
			player.sendMessage("§cEine Übersicht aller Befehle gibt es §6im Forum!");
			
			return true;
		}
		
		if (args.length == 2) {
			
			AbstractCommand cmd = commands.getCommand(args[1]);
			
			if (cmd instanceof CommandContainer)
				cmd = ((CommandContainer) cmd).getCommand(args[1]);
			
			if (cmd == null) {
				player.sendMessage(lang.getColoredMessage(user.getLang(), "cmd_not_found"));
				return true;
			}
			
			if (!Level.canUse(user, cmd.getLevel())) {
				player.sendMessage(lang.getColoredMessage(user.getLang(), "nopermission"));
				return true;
			}
			
			player.sendMessage("§7Usage instructions for command §6" + args[1] + "§7:");
			cmd.sendUsage(player);
						
			return true;
		}
		
		return true;
	}
}
