package net.edgecraft.edgecore.mod;

import java.util.ArrayList;
import java.util.List;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFKCommand extends AbstractCommand {
	
	private static final AFKCommand instance = new AFKCommand();
	
	private AFKCommand() { /* ... */ }
	
	public static final AFKCommand getInstance() {
		return instance;
	}
	
	private List<String> afk = new ArrayList<String>();
	
	@Override
	public String[] getNames() {
		return new String[] { "afk" };
	}

	@Override
	public Level getLevel() {
		return Level.USER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return args.length == 1;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args)	throws Exception {
		
		setAfk(player.getName());
		
		return true;
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		sender.sendMessage(EdgeCore.usageColor + "/afk");
		sender.sendMessage(EdgeCore.usageColor + "/back");
	}

	private void setAfk( String user ) {
		
		boolean isAfk = false;
		
		if (afk.contains(user)) {
			
			afk.remove(user);
			
		} else {
			
			afk.add(user);
			isAfk = true;
			
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!users.exists(p.getName()))
				continue;
			
			User u = users.getUser(p.getName());
			
			if (isAfk)
				p.sendMessage(lang.getColoredMessage(u.getLang(), "mod_afk").replace("[0]", user));
			else
				p.sendMessage(lang.getColoredMessage(u.getLang(), "mod_afk_back").replace("[0]", user));
		}
	}
}
