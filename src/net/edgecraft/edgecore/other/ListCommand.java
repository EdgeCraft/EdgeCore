package net.edgecraft.edgecore.other;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand extends AbstractCommand {
	
	private static final ListCommand instance = new ListCommand();
	
	private ListCommand() { /* ... */ }
	
	public static final ListCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		String[] names = { "list", "who" };
		return names;
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
	
	@Override
	public boolean runImpl( Player player, User user , String[] args) throws Exception {
		
		String userLang = user.getLanguage();
		
		if (!Level.canUse(user, getLevel())) {
			player.sendMessage(lang.getColoredMessage(userLang, "nopermission"));
			return true;
		}
		
		player.sendMessage(users.getUserList(user.getLanguage()));
		
		return true;
	}

	@Override
	public void sendUsage(CommandSender sender) {
		sender.sendMessage( EdgeCore.usageColor + "/list");
	}
}
