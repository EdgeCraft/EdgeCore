package net.edgecraft.edgecore.user;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;

import org.bukkit.scheduler.BukkitRunnable;

public class UserSynchronizationTask extends BukkitRunnable {
	
	public UserSynchronizationTask() { }
	
	@Override
	public void run() {
		
		EdgeCore.log.info(EdgeCore.edgebanner + "Starte User-Synchronisation..");		
		EdgeCoreAPI.userAPI().synchronizeUsers();
		EdgeCore.log.info(EdgeCore.edgebanner + "Automatische User-Synchronisation abgeschlossen!");
		
	}
}
