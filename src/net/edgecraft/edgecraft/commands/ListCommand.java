package net.edgecraft.edgecraft.commands;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.classes.User;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.util.LanguageHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements CommandExecutor {
	
	private final UserManager userManager = EdgeCraft.getUsers();
	
	/**
	 * Gets executed whenever sb. uses the ( '/list' || '/who' )-command.
	 * 
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 * @returns true/false
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		 
		if (!(sender instanceof Player)) {
			sender.sendMessage(this.userManager.getUserList(LanguageHandler.getDefaultLanguage()));
			return true;
		}
		
		Player player = (Player) sender;
		
		User user = this.userManager.getUser(player.getName());
		
		player.sendMessage(this.userManager.getUserList(user.getLanguage()));
		
		return true;
	}
}
