package net.edgecraft.edgecraft.events;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.classes.User;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.util.LanguageHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionHandler implements Listener {
	
	private final UserManager userManager = EdgeCraft.getUsers();
	private final LanguageHandler lang = EdgeCraft.getLang();
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogin(PlayerJoinEvent event) {
		
		event.setJoinMessage("");
		Player player = event.getPlayer();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (this.userManager.exists(p.getName())) {
				
				User user = this.userManager.getUser(p.getName());
				p.sendMessage(this.lang.getColoredMessage(user.getLanguage(), "login").replace("[0]", player.getName()));
				
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogout(PlayerQuitEvent event) {
		
		event.setQuitMessage("");
		Player player = event.getPlayer();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (this.userManager.exists(p.getName())) {
				
				User user = this.userManager.getUser(p.getName());
				p.sendMessage(this.lang.getColoredMessage(user.getLanguage(), "logout").replace("[0]", player.getName()));
			}
		}
	}
	
	@EventHandler
	public void onBanCheck(PlayerJoinEvent e) {
		
		String joinIP = "";
		
		if (!userManager.exists(e.getPlayer().getName())) {
			
			joinIP = e.getPlayer().getAddress().toString();
			
		} else {
			
			joinIP = userManager.getUser(e.getPlayer().getName()).getIP();
			
		}
		
		if (UserManager.bannedIPs.contains(joinIP)) {
			for (Player p : Bukkit.getOnlinePlayers()) {				
				if (p.hasPermission("edgecraft.ipwarning")) {
					
					User user = userManager.getUser(p.getName());
					User banned = userManager.getUserByIP(joinIP);
					
					if (user != null && banned != null) {
						p.sendMessage(lang.getColoredMessage(user.getLanguage(), "info_bannedip").replace("[0]", joinIP).replace("[1]", banned.getName()));
					}
				}
			}
		}
	}
}
