package net.edgecraft.edgecraft.util;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.edgecraft.edgecraft.EdgeCraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class EdgeCraftSystem {

	public String uptime;
	public int overloadedMemoryAmount = 80;
	private final Runtime runtime = Runtime.getRuntime();

	public void startTimer() {

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			DecimalFormat decimalFormat = new DecimalFormat("00");

			int seconds = 0;
			int minutes = 0;
			int hours = 0;
			int days = 0;

			public void run() {

				this.seconds += 1;

				if (this.seconds > 59) {
					this.seconds = 0;
					this.minutes += 1;
				}

				if (this.minutes > 59) {
					this.minutes = 0;
					this.hours += 1;
				}

				if (this.hours > 23) {
					this.hours = 0;
					this.days += 1;
				}

				uptime = (decimalFormat.format(days) + " Tage "
						+ decimalFormat.format(hours) + " Std. "
						+ decimalFormat.format(minutes) + " Min. "
						+ decimalFormat.format(seconds) + " Sek.");

			}
		};

		timer.schedule(task, new Date(), 1000L);
	}

	public void resetUptime() {
		uptime = "";
	}

	public void getConsoleOverview() {
		EdgeCraft.log.info("[EdgeCraft] Uptime: " + getUptime());
		EdgeCraft.log.info("[EdgeCraft] Maximaler RAM: " + getMaxMemory() + " MB");
		EdgeCraft.log.info("[EdgeCraft] Totaler RAM: " + getTotalMemory() + " MB");
		EdgeCraft.log.info("[EdgeCraft] Freier RAM: " + getFreeMemory() + " MB");
		EdgeCraft.log.info("[EdgeCraft] Genutzter RAM: " + getUsedMemory() + " MB");
		Bukkit.getServer().getConsoleSender().sendMessage("[EdgeCraft] Memory Status: "	+ (overloadedMemory() 
																						? ChatColor.RED + "Ausgelastet!" 
																						: new StringBuilder().append(ChatColor.GREEN).append("Gut.").toString()));
	}

	public String getUptime() {
		return uptime;
	}

	public int getMaxMemory() {
		int maxMemory = (int) this.runtime.maxMemory() / 1024 / 1024;
		return maxMemory;
	}

	public int getTotalMemory() {
		int totalMemory = (int) this.runtime.totalMemory() / 1024 / 1024;
		return totalMemory;
	}

	public int getFreeMemory() {
		int freeMemory = (int) this.runtime.freeMemory() / 1024 / 1024;
		return freeMemory;
	}

	public int getUsedMemory() {
		int usedMemory = getTotalMemory() - getFreeMemory();
		return usedMemory;
	}
	
	public boolean overloadedMemory() {
		return Math.round(getUsedMemory() / getTotalMemory() * 100) >= overloadedMemoryAmount;
	}
}
