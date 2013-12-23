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

public class UserCommand implements CommandExecutor {
	private final LanguageHandler lang = EdgeCraft.lang;
	private final UserManager userManager = EdgeCraft.manager;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.GREEN + "Registrierte User: " + ChatColor.WHITE + this.userManager.amountOfUsers());
			return true;
		}

		Player player = (Player) sender;
		User user = this.userManager.getUser(player.getName());

		if (user != null) {
			
			String userLang = user.getLanguage();

			if (!player.hasPermission("edgecraft.user")) {
				player.sendMessage(this.lang.getColoredMessage(userLang, "nopermission"));
				return false;
			}

			if ((args.length <= 0) || (args.length > 3)) {
				player.sendMessage(ChatColor.RED + "/user register <name> <ip>");
				player.sendMessage(ChatColor.RED + "/user delete <id>");
				player.sendMessage(ChatColor.RED + "/user exists <user>");
				player.sendMessage(ChatColor.RED + "/user reload [<user>]");
				player.sendMessage(ChatColor.RED + "/user amount");

				return true;
			}

			if (args[0].equalsIgnoreCase("register")) {
				
				if (args.length <= 2) {
					player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
					return false;
				}

				if (this.userManager.exists(args[1])) {
					player.sendMessage(this.lang.getColoredMessage(userLang, "user_register_alreadyexists").replace("[0]", args[1]));
					return false;
				}

				this.userManager.registerUser(args[1], args[2]);
				User user_ = this.userManager.getUser(args[1]);

				player.sendMessage(this.lang.getColoredMessage(userLang, "user_register_success").replace("[0]", user_.getID() + "").replace("[1]", user_.getName()).replace("[2]", user_.getIP()));

				return true;
			}

			if (args[0].equalsIgnoreCase("delete")) {
				if (args.length <= 1) {
					player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
					return false;
				}

				try {
					
					int id = Integer.parseInt(args[1]);

					if (!this.userManager.exists(id)) {
						player.sendMessage(this.lang.getColoredMessage(userLang, "notfound"));
						return false;
					}
					
					String name = this.userManager.getUser(id).getName();

					this.userManager.deleteUser(id);
					player.sendMessage(this.lang.getColoredMessage(userLang, "user_delete_success").replace("[0]", name));
					
				} catch (NumberFormatException e) {
					player.sendMessage(this.lang.getColoredMessage(userLang, "numberformatexception"));
				}

				return true;
			}

			if (args[0].equalsIgnoreCase("exists")) {
				if (args.length <= 1) {
					player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
					return false;
				}

				if (!this.userManager.exists(args[1])) {
					player.sendMessage(this.lang.getColoredMessage(userLang, "user_exists_false").replace("[0]", args[1]));
					return false;
				}

				player.sendMessage(this.lang.getColoredMessage(userLang, "user_exists_true").replace("[0]", args[1]));

				return true;
			}

			if (args[0].equalsIgnoreCase("reload")) {
				if (args.length == 1) {
					
					this.userManager.synchronizeUsers();
					player.sendMessage(this.lang.getColoredMessage(userLang, "user_reload_all_success"));

					return true;
				}

				if (args.length == 2) {
					
					if (!this.userManager.exists(args[1])) {
						player.sendMessage(this.lang.getColoredMessage(userLang, "notfound"));
						return false;
					}

					this.userManager.synchronizeUser(this.userManager.getUser(args[1]).getID());
					player.sendMessage(this.lang.getColoredMessage(userLang, "user_reload_success").replace("[0]", args[1]));

					return true;
				}
			}

			if (args[0].equalsIgnoreCase("amount")) {
				if (args.length != 1) {
					player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
					return false;
				}

				player.sendMessage(this.lang.getColoredMessage(userLang, "user_amount").replace("[0]", this.userManager.amountOfUsers() + ""));

				return true;
			}
		}

		return true;
	}
}