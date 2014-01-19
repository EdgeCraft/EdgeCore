package net.edgecraft.edgecrat.other;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.command.AbstractCommand;
import net.edgecraft.edgecraft.command.Level;
import net.edgecraft.edgecraft.lang.LanguageHandler;
import net.edgecraft.edgecraft.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends AbstractCommand {
	
	
	public ListCommand() { instance = new ListCommand(); }
	
	@Override
	public ListCommand getInstance() {
		return (ListCommand)super.instance;
	}
	
	@Override
	public boolean runImpl( Player player, User user , String[] args) throws Exception {
		
		player.sendMessage(users.getUserList(user.getLanguage()));
		
		return true;
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public void sendUsage(CommandSender sender) {
		sender.sendMessage( EdgeCraft.usageColor + "/list");
		
	}

	@Override
	public Level getLevel() {
		return Level.GUEST;
	}


	@Override
	public boolean validArgsRange(String[] args) {
		return (args.length == 1);
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		sender.sendMessage(users.getUserList(LanguageHandler.getDefaultLanguage()));
		return true;
	}
}
