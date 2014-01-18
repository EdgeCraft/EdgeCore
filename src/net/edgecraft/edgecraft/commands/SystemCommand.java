package net.edgecraft.edgecraft.commands;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.classes.User;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.util.EdgeCraftSystem;
import net.edgecraft.edgecraft.util.LanguageHandler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SystemCommand implements CommandExecutor {
	
	private final LanguageHandler lang = EdgeCraft.getLang();
	private final EdgeCraftSystem system = EdgeCraft.getSystem();
	private final UserManager userManager = EdgeCraft.getUsers();
	
	
	/**
	 * Gets executed whenever sb. uses the '/system'-command.
	 * 
	 * @param sender
	 * @param cmd
	 * @param label
	 * @param args
	 * @return true/false
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			this.system.getConsoleOverview();
			return true;
		}
		
		Player player = (Player) sender;
		User user = this.userManager.getUser(player.getName());
		
		if (user != null) {
			
			String userLang = user.getLanguage();
			
			if (!player.hasPermission("edgecraft_system")) {
				player.sendMessage(this.lang.getColoredMessage(userLang, "nopermission"));
				return false;
			}
			
			if (args.length == 0 || args.length >= 5) {
				player.sendMessage(ChatColor.RED + "/system overview");
				player.sendMessage(ChatColor.RED + "/system memory <max|total|free|used>");
				player.sendMessage(ChatColor.RED + "/system uptime");
				player.sendMessage(ChatColor.RED + "/system reset");
				
				return true;
			}
			
			if (args[0].equalsIgnoreCase("overview")) {
								
		        player.sendMessage(this.lang.getColoredMessage(userLang, "system_overview"));
		        player.sendMessage(this.lang.getColoredMessage(userLang, "system_uptime").replace("[0]", this.system.getUptime()));
		        player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_max").replace("[0]", this.system.getMaxMemory() + ""));
		        player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_total").replace("[0]", this.system.getTotalMemory() + ""));
		        player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_free").replace("[0]", this.system.getFreeMemory() + ""));
		        player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_used").replace("[0]", this.system.getUsedMemory() + ""));
		        
		        if (!this.system.overloadedMemory()) {
		        	player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_health_good").replace("[0]", EdgeCraftSystem.overloadedMemoryAmount + ""));
		        } else {
		        	player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_health_bad").replace("[0]", EdgeCraftSystem.overloadedMemoryAmount + ""));
		        }

		        return true;				
			}
			
			if (args[0].equalsIgnoreCase("memory")) {
				if (args.length == 1) {
					player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
					return false;
				}
				
				if (args[1].equalsIgnoreCase("max")) {
					player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_max").replace("[0]", this.system.getMaxMemory() + ""));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("total")) {
					player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_total").replace("[0]", this.system.getTotalMemory() + ""));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("free")) {
					player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_free").replace("[0]", this.system.getFreeMemory() + ""));
					return true;
				}
				
				if (args[1].equalsIgnoreCase("used")) {
					player.sendMessage(this.lang.getColoredMessage(userLang, "system_memory_used").replace("[0]", this.system.getUsedMemory() + ""));
					return true;
				}
				
				return true;
			}
			
			if (args[0].equalsIgnoreCase("uptime")) {
				player.sendMessage(this.lang.getColoredMessage(userLang, "system_uptime").replace("[0]", this.system.getUptime()));
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reset")) {
				try {
					
					this.system.resetUptime();
					player.sendMessage(this.lang.getColoredMessage(userLang, "system_reset_success"));
					
				} catch(Exception e) {
					e.printStackTrace();
					player.sendMessage(this.lang.getColoredMessage(userLang, "globalerror"));
				}
				
				return true;
			}
		}
		
		return true;
	}
}
