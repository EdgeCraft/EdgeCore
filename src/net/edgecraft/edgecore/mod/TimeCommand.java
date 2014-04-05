package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand extends AbstractCommand {
	
	private boolean isTimeShift = false;
	private int taskID;
	
	private static final TimeCommand instance = new TimeCommand();
	
	private TimeCommand() { super(); }
	
	public static final TimeCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "time" };
	}

	@Override
	public Level getLevel() {
		return Level.ARCHITECT;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 1 || args.length == 2 );
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage( EdgeCore.usageColor + "/time" );
		sender.sendMessage( EdgeCore.usageColor + "/time shift");
		sender.sendMessage( EdgeCore.usageColor + "/time day" );
		sender.sendMessage( EdgeCore.usageColor + "/time night" );
		sender.sendMessage( EdgeCore.usageColor + "/time <time>" );
		
		return;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		return time( player, args );
		
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return time( sender, args );
	}
	
	private boolean time( CommandSender sender, String[] args ) {
		if (!validArgsRange(args)) {
			sendUsage(sender);
			return true;
		}
		
		if (args.length == 1) {
			
			sender.sendMessage("§aCurrent time in §6" + Bukkit.getWorlds().get(0).getName() + "§a: §6" + Bukkit.getWorlds().get(0).getTime() + " ticks");
			
			return true;
		}
		
		if (args[1].equalsIgnoreCase("shift")) {			
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can shift time!");
				return true;
			}
			
			Player p = (Player) sender;
			
			final World w = p.getWorld();
			final long shift = w.getTime();
			
			if (isTimeShift) {
				
				Bukkit.getScheduler().cancelTask(taskID);
				isTimeShift = false;
				
				p.sendMessage("§aTime is running normal again in §6" + w.getName() + "§a!");
				
			} else {
				
				taskID = Bukkit.getScheduler().runTaskTimer(EdgeCore.getInstance(), new Runnable() {
					
					public void run() {
						
						w.setTime(shift);
						
					}
					
				}, 0L, 20L * 2).getTaskId();
				
				isTimeShift = true;
				p.sendMessage("§aTime is standing still in §6" + w.getName() + "§a..");
				
			}
			
		} else if(args[1].equalsIgnoreCase("day")) {
			
			for (World w : Bukkit.getWorlds()) {
				w.setTime(2000L);
			}
			
			sender.sendMessage(ChatColor.GREEN + "The time has been set to " + ChatColor.GOLD + "day / 2000 ticks");
			
		} else if(args[1].equalsIgnoreCase("night")) {
			
			for (World w : Bukkit.getWorlds()) {
				w.setTime(14000L);
			}
			
			sender.sendMessage(ChatColor.GREEN + "The time has been set to " + ChatColor.GOLD + "night / 14000 ticks");
			
		} else {			
			try {
				
				long time = Long.parseLong(args[1]);
				
				for (World w : Bukkit.getWorlds()) {
					w.setTime(time);
				}
				
				sender.sendMessage(ChatColor.GREEN + "The time has been set to " + ChatColor.GOLD + "magic / " + time + " ticks");
				
			} catch(NumberFormatException e) {
				sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "numberformatexception"));
			}
		}
		
		return true;
	}

}
