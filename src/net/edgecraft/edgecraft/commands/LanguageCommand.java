package net.edgecraft.edgecraft.commands;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.classes.User;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.util.LanguageHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand implements CommandExecutor {

	private final LanguageHandler lang = EdgeCraft.getLang();
	private final UserManager userManager = EdgeCraft.getUsers();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.GREEN + "Standardsprache: " + ChatColor.WHITE + LanguageHandler.defaultLanguage);
			return true;
		}

		Player player = (Player) sender;
		User user = this.userManager.getUser(player.getName());

		if (user != null) {
			
			String userLang = user.getLanguage();

			if (!player.hasPermission("edgecraft.language")) {
				player.sendMessage(this.lang.getColoredMessage(userLang, "nopermission"));
				return false;
			}

			if (args.length == 0) {
				player.sendMessage(this.lang.getColoredMessage(userLang, "lang_info").replace("[0]", user.getLanguage()));
				return true;
			}

			if (args.length > 2) {
				player.sendMessage(ChatColor.RED + "/language");
				player.sendMessage(ChatColor.RED + "/language set <language>");
				player.sendMessage(ChatColor.RED + "/language set default");
				player.sendMessage(ChatColor.RED + "/language list");
				player.sendMessage(ChatColor.RED + "/language help");

				return true;
			}

			if (args[0].equalsIgnoreCase("set")) {
				try {
					if (args[1].equalsIgnoreCase("default")) {
						
						user.updateLanguage(LanguageHandler.defaultLanguage);
						player.sendMessage(this.lang.getColoredMessage(userLang, "lang_set_default"));

						return true;
					}

					if (!this.lang.exists(args[1])) {
						player.sendMessage(this.lang.getColoredMessage(userLang, "unknownlanguage").replace("[0]", args[1]));
						return false;
					}

					user.updateLanguage(args[1]);
					player.sendMessage(this.lang.getColoredMessage(userLang, "lang_set").replace("[0]", args[1]));
					
				} catch (Exception e) {
					e.printStackTrace();
					player.sendMessage(this.lang.getColoredMessage(userLang, "globalerror"));
				}

				return true;
			}

			if (args[0].equalsIgnoreCase("list")) {
				player.sendMessage(this.lang.getColoredMessage(userLang, "globalerror"));
				return true;
			}

			if (args[0].equalsIgnoreCase("help")) {
				player.sendMessage(ChatColor.RED + "/language");
				player.sendMessage(ChatColor.RED + "/language set <language>");
				player.sendMessage(ChatColor.RED + "/language set default");
				player.sendMessage(ChatColor.RED + "/language list");
				player.sendMessage(ChatColor.RED + "/language help");

				return true;
			}
		}

		return true;
	}
}
