package net.edgecraft.edgecore.other;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;
import net.edgecraft.edgecore.user.UserManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionHandler implements Listener {
	
	private final UserManager users = EdgeCore.getUsers();
	private final LanguageHandler lang = EdgeCore.getLang();
	
	/**
	 * Gets executed whenever a player joins.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogin(PlayerJoinEvent event) {
		
		event.setJoinMessage("");
		Player player = event.getPlayer();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (this.users.exists(p.getName())) {
				
				User user = this.users.getUser(p.getName());
				p.sendMessage(this.lang.getColoredMessage(user.getLanguage(), "login").replace("[0]", player.getName()));
				
			}
		}
	}
	
	/**
	 * Gets executed whenever a player logs out.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogout(PlayerQuitEvent event) {
		
		event.setQuitMessage("");
		Player player = event.getPlayer();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (this.users.exists(p.getName())) {
				
				User user = this.users.getUser(p.getName());
				p.sendMessage(this.lang.getColoredMessage(user.getLanguage(), "logout").replace("[0]", player.getName()));
			}
		}
	}
	
	/**
	 * TODO: Add senseful comment.
	 * @param e
	 */
	@EventHandler
	public void onBanCheck(PlayerJoinEvent e) {
		
		String joinIP = "";
		
		if (!users.exists(e.getPlayer().getName())) {
			
			joinIP = e.getPlayer().getAddress().toString();
			
		} else {
			
			joinIP = users.getUser(e.getPlayer().getName()).getIP();
			
		}
		
		if (UserManager.getBannedIPs().contains(joinIP)) {
			for (Player p : Bukkit.getOnlinePlayers()) {				
				if (p.hasPermission("edgecraft.ipwarning")) {
					
					User user = users.getUser(p.getName());
					User banned = users.getUserByIP(joinIP);
					
					if (user != null && banned != null) {
						p.sendMessage(lang.getColoredMessage(user.getLanguage(), "info_bannedip").replace("[0]", joinIP).replace("[1]", banned.getName()));
					}
				}
			}
		}
	}
}
