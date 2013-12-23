package net.edgecraft.edgecraft.commands;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.classes.User;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.mysql.DatabaseHandler;
import net.edgecraft.edgecraft.util.LanguageHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DatabaseCommand implements CommandExecutor {
	
	private final LanguageHandler lang = EdgeCraft.lang;
	private final DatabaseHandler db = EdgeCraft.db;
	private final UserManager userManager = EdgeCraft.manager;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			try {
				
				sender.sendMessage(ChatColor.GREEN + "Datenbanken: " + ChatColor.WHITE + db.getDatabases().replace("edgecraft", ChatColor.AQUA + "edgecraft"));
				return true;
				
			} catch(Exception e) {
				e.printStackTrace();
				sender.sendMessage(lang.getColoredMessage(LanguageHandler.defaultLanguage, "globalerror"));
			}
		}
		
		Player player = (Player) sender;
		User user = userManager.getUser(player.getName());
		
		if (user != null) {
			
			String userLang = user.getLanguage();
			
			if (!player.hasPermission("edgecraft.db")) {
				player.sendMessage(lang.getColoredMessage(userLang, "nopermission"));
				return false;
			}
			
			if (args.length == 0 || args.length > 5) {
				player.sendMessage(ChatColor.RED + "/db check [<name> <table>]");
				player.sendMessage(ChatColor.RED + "/db connect <host> <user> <pw> <database>");
				player.sendMessage(ChatColor.RED + "/db close");
				
				return true;
			}
			
			if (args[0].equalsIgnoreCase("check")) {
				if (args.length < 1 || args.length > 3) {
					player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
					return false;
				}
				
				if (args.length == 1) {
					player.sendMessage(lang.getColoredMessage(userLang, "db_check").replace("[0]", ChatColor.GREEN + "Verbunden!"));
					return true;
				}
				
				if (args.length == 2) {
					try {
						
						if (!this.db.existsDatabase(args[1])) {
							player.sendMessage(lang.getColoredMessage(userLang, "db_check_db_false").replace("[0]", args[1]));
							return true;
						}
						
						player.sendMessage(lang.getColoredMessage(userLang, "db_check_db_true").replace("[0]", args[1]));
						
					} catch(Exception e) {
						e.printStackTrace();
						player.sendMessage(lang.getColoredMessage(userLang, "globalerror"));
					}
					
					return true;
				}
				
				if (args.length == 3) {
					try {
						
						if (!this.db.existsDatabase(args[1])) {
							player.sendMessage(lang.getColoredMessage(userLang, "db_check_db_false").replace("[0]", args[1]));
							return true;
						}
						
						if (!this.db.existsTable(args[2])) {
							player.sendMessage(lang.getColoredMessage(userLang, "db_check_table_false").replace("[0]", args[2]));
						}
						
						player.sendMessage(lang.getColoredMessage(userLang, "db_chcek_table_true").replace("[0]", args[2]));
						
					} catch(Exception e) {
						e.printStackTrace();
						player.sendMessage(lang.getColoredMessage(userLang, "globalerror"));
					}
					
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("connect")) {
				if (args.length < 1) {
					player.sendMessage(ChatColor.RED + "/db connect <host> <user> pw> <database>");
					return false;
				}
				
				if (args.length == 5) {
					try {
						
						if (!db.isAvailable()) {
							player.sendMessage(lang.getColoredMessage(userLang, "db_connect_alreadyconnected"));
							return false;
						}
						
						db.loadConnection(args[1], args[2], args[3], args[4]);
						player.sendMessage(lang.getColoredMessage(userLang, "db_connect_success").replace("[0]", args[4]));
						
					} catch(Exception e) {
						e.printStackTrace();
						player.sendMessage(lang.getColoredMessage(userLang, "db_connect_failed").replace("[0]", args[4]));
						
					} finally {
						db.connection = null;
					}
				}
			}
			
			if (args[0].equalsIgnoreCase("close")) {
				try {
					
					db.closeConnection();
					player.sendMessage(lang.getColoredMessage(userLang, "db_close_success"));
					
				} catch(Exception e) {
					e.printStackTrace();
					player.sendMessage(lang.getColoredMessage(userLang, "db_close_failed"));
				}
			}
		}
		
		return true;
	}
}
