package net.edgecraft.edgecore.system;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SystemCommand extends AbstractCommand {
	
	public SystemCommand() { /* ... */ }
	
	@Override
	public String[] getNames() {
		String[] names = { "system", "sys" };
		return names;
	}
	
	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return !(args.length < 2 || args.length > 3);
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		system.getConsoleOverview();
		return true;
	}
	
	@Override
	public boolean runImpl( Player player, User user, String[] args) throws Exception {
		
		String userLang = user.getLanguage();
		
		if( !Level.canUse(user, getLevel() )) {
			player.sendMessage( lang.getColoredMessage(userLang, "nopermission") );
			return false;
		}
		
		if (args[1].equalsIgnoreCase("overview")) {
			
	        player.sendMessage(lang.getColoredMessage(userLang, "system_overview"));
	        player.sendMessage(lang.getColoredMessage(userLang, "system_uptime").replace("[0]", system.getUptime()));
	        player.sendMessage(lang.getColoredMessage(userLang, "system_memory_max").replace("[0]", system.getMaxMemory() + ""));
	        player.sendMessage(lang.getColoredMessage(userLang, "system_memory_total").replace("[0]", system.getTotalMemory() + ""));
	        player.sendMessage(lang.getColoredMessage(userLang, "system_memory_free").replace("[0]", system.getFreeMemory() + ""));
	        player.sendMessage(lang.getColoredMessage(userLang, "system_memory_used").replace("[0]", system.getUsedMemory() + ""));
	        
	        if (!system.overloadedMemory()) {
	        	player.sendMessage(lang.getColoredMessage(userLang, "system_memory_health_good").replace("[0]", EdgeCraftSystem.overloadedMemoryAmount + ""));
	        } else {
	        	player.sendMessage(lang.getColoredMessage(userLang, "system_memory_health_bad").replace("[0]", EdgeCraftSystem.overloadedMemoryAmount + ""));
	        }

	        return true;				
		}
		
		
		
		if (args[1].equalsIgnoreCase("memory")) {
			
			if (args.length != 3) {
				player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
				return false;
			}
			
			
			switch( args[1] ) {
				
				case "max":
					player.sendMessage(lang.getColoredMessage(userLang, "system_memory_max").replace("[0]", system.getMaxMemory() + ""));
					return true;
				case "total":
					player.sendMessage(lang.getColoredMessage(userLang, "system_memory_total").replace("[0]", system.getTotalMemory() + ""));
					return true;
				case "free":
					player.sendMessage(lang.getColoredMessage(userLang, "system_memory_free").replace("[0]", system.getFreeMemory() + ""));
					return true;
				case "used":
					player.sendMessage(lang.getColoredMessage(userLang, "system_memory_used").replace("[0]", system.getUsedMemory() + ""));
					return true;
					
				default:
					sendUsage(player);
					return true;
			}
		}
		
		
		if (args[1].equalsIgnoreCase("uptime")) {
			player.sendMessage(lang.getColoredMessage(userLang, "system_uptime").replace("[0]", system.getUptime()));
			return true;
		}
		
		if (args[1].equalsIgnoreCase("reset")) {
			try {
				
				system.resetUptime();
				player.sendMessage(lang.getColoredMessage(userLang, "system_reset_success"));
				
			} catch(Exception e) {
				e.printStackTrace();
				player.sendMessage(lang.getColoredMessage(userLang, "globalerror"));
			}
			
			return true;
		}
		
		return true;
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		if (sender instanceof Player) {
			
			User u = EdgeCoreAPI.userAPI().getUser(sender.getName());
			
			if (u != null) {
				
				if (!Level.canUse(u, getLevel())) {
					sender.sendMessage(lang.getColoredMessage(u.getLanguage(), "nopermission"));
					return;
				}
			}
		}
		
		sender.sendMessage(EdgeCore.usageColor + "/system overview");
		sender.sendMessage(EdgeCore.usageColor + "/system memory <max|total|free|used>");
		sender.sendMessage(EdgeCore.usageColor + "/system uptime");
		sender.sendMessage(EdgeCore.usageColor + "/system reset");
		
	}
}
