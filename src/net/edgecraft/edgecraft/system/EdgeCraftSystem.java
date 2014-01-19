package net.edgecraft.edgecraft.system;

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

	protected static final EdgeCraftSystem instance = new EdgeCraftSystem();
	
	protected EdgeCraftSystem() { /* ... */ }
	
	public static EdgeCraftSystem getInstance() {
		return instance;
	}
	
	/**
	 * Logs important system-information of the runtime.
	 * 
	 */
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
	
	/**
	 * Starts the timer for the uptime-value.
	 * 
	 */
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

	/**
	 * Returns the maximum amount of memory [MB] the VM will use.
	 * 
	 * @return
	 */
	public int getMaxMemory() {
		return ( (int) this.runtime.maxMemory() / 1024 / 1024 );
	}

	/**
	 * Returns total amount of memory [MB] of the VM.
	 * 
	 * @return
	 */
	public int getTotalMemory() {
		return ( (int) this.runtime.totalMemory() / 1024 / 1024 );
	}

	/**
	 * Returns the amount of free memory [MB] of the VM.
	 * 
	 * @return
	 */
	public int getFreeMemory() {
		return ( (int) this.runtime.freeMemory() / 1024 / 1024 );
	}

	/**
	 * Returns the amount of used memory [MB] of the VM.
	 * 
	 * @return
	 */
	public int getUsedMemory() {
		return ( getTotalMemory() - getFreeMemory() );
	}
	
	/**
	 * Returns true if the system is (80%) busy.
	 * 
	 * @return true/false
	 */
	public boolean overloadedMemory() {
		return Math.round(getUsedMemory() / getTotalMemory() * 100) >= overloadedMemoryAmount;
	}
	
	/**
	 * Returns the uptime of the EdgeCraft-instance.
	 * 
	 * @return
	 */
	public String getUptime() {
		return this.uptime;
	}

	/**
	 * Sets the uptime of the EdgeCraft-instance.
	 * @param uptime
	 */
	public void setUptime( String uptime ) {
		if( uptime != null )
			this.uptime = uptime;
	}

	/**
	 * Resets the uptime of the EdgeCraft-instance.
	 * 
	 */
	public void resetUptime() {
		setUptime( "" );
	}
}
