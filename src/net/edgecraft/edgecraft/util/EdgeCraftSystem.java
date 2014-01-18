package net.edgecraft.edgecraft.util;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.edgecraft.edgecraft.EdgeCraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class EdgeCraftSystem {

	private String uptime;
	public static final int overloadedMemoryAmount = 80;
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

				setUptime( decimalFormat.format(days) + " [d] " + decimalFormat.format(hours) + " [h] " + decimalFormat.format(minutes) + " [min] " + decimalFormat.format(seconds) + " [sec]." );

			}
		};

		timer.schedule(task, new Date(), 1000L);
	}

	public String getUptime() {
		return this.uptime;
	}

	public void setUptime( String uptime ) {
		if( uptime != null )
			this.uptime = uptime;
	}

	public void resetUptime() {
		setUptime( "" );
	}

	public void getConsoleOverview() {
		EdgeCraft.log.info( EdgeCraft.edgebanner + "Uptime: " + getUptime());
		EdgeCraft.log.info( EdgeCraft.edgebanner + "Maximaler RAM: " + getMaxMemory() + " [MB]");
		EdgeCraft.log.info( EdgeCraft.edgebanner + "Totaler RAM: " + getTotalMemory() + " [MB]");
		EdgeCraft.log.info( EdgeCraft.edgebanner + "Freier RAM: " + getFreeMemory() + " [MB]");
		EdgeCraft.log.info( EdgeCraft.edgebanner + "Genutzter RAM: " + getUsedMemory() + " [MB]");
		Bukkit.getServer().getConsoleSender().sendMessage( EdgeCraft.edgebanner + "Memory Status: "	+ (overloadedMemory() 
																						? ChatColor.RED + "Ausgelastet!" 
																						: new StringBuilder().append(ChatColor.GREEN).append("Gut.").toString()));
	}

	public int getMaxMemory() {
		return ( (int) this.runtime.maxMemory() / 1024 / 1024 );
	}

	public int getTotalMemory() {
		return ( (int) this.runtime.totalMemory() / 1024 / 1024 );
	}

	public int getFreeMemory() {
		return ( (int) this.runtime.freeMemory() / 1024 / 1024 );
	}

	public int getUsedMemory() {
		return ( getTotalMemory() - getFreeMemory() );
	}
	
	public boolean overloadedMemory() {
		return Math.round(getUsedMemory() / getTotalMemory() * 100) >= overloadedMemoryAmount;
	}
}
