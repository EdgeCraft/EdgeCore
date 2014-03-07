package net.edgecraft.edgecore.chat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class ChatCommand extends AbstractCommand {

	@Override
	public String[] getNames() {
		return null;
	}

	@Override
	public Level getLevel() {
		return null;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return false;
	}

	@Override
	public void sendUsage(CommandSender sender) {		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		return false;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}	
}
