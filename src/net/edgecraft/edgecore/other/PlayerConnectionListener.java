package net.edgecraft.edgecore.other;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;
import net.edgecraft.edgecore.user.UserManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {
	
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
		
		try {
			
			Player player = event.getPlayer();
			User u = users.getUser(player.getName());
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', EdgeCore.getInstance().getConfig().getString("General.MOTD")));
			
			if( u != null)
				player.setPlayerListName(u.getLevel().getColor() + player.getName());
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getName().equalsIgnoreCase(event.getPlayer().getName())) continue;
				
				if (this.users.exists(p.getName())) {
					
					User user = this.users.getUser(p.getName());
					user.updateIP(p.getAddress().getAddress().getHostAddress());
					
					p.sendMessage(this.lang.getColoredMessage(user.getLanguage(), "login").replace("[0]", player.getName()));
					
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets executed whenever a player logs out.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLogout(PlayerQuitEvent event) {		
		try {
			
			event.setQuitMessage("");
			Player player = event.getPlayer();
			
			if (!users.exists(player.getName()))
				return;
			
			users.getUser(player.getName()).updateLastLocation(player.getLocation());
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getName().equalsIgnoreCase(event.getPlayer().getName())) continue;
				
				if (this.users.exists(p.getName())) {
					
					User user = this.users.getUser(p.getName());
					p.sendMessage(this.lang.getColoredMessage(user.getLanguage(), "logout").replace("[0]", player.getName()));
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
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
			
			joinIP = e.getPlayer().getAddress().getAddress().getHostAddress();
			
		} else {
			
			joinIP = users.getUser(e.getPlayer().getName()).getIP();
			
		}
		
		if (UserManager.getBannedIPs().contains(joinIP)) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				
				User user = users.getUser(p.getName());
				User banned = users.getUserByIP(joinIP);
				
				if (user != null && banned != null && Level.canUse(user, Level.SUPPORTER)) {
					p.sendMessage(lang.getColoredMessage(user.getLanguage(), "info_bannedip").replace("[0]", joinIP).replace("[1]", banned.getName()));
				}
			}
		}
	}
	
	/**
	 * TODO: Add senseful comment.
	 * @param e
	 */
	@EventHandler
	public void handleLoginEvent(PlayerLoginEvent e) {
		
		Player player = e.getPlayer();
		
		if (EdgeCoreAPI.userAPI().exists(player.getName())) {
			
			User user = EdgeCoreAPI.userAPI().getUser(player.getName());
			
			if (user.isBanned()) {
				e.disallow(null, lang.getColoredMessage(user.getLanguage(), "info_permban").replace("[0]", user.getBanReason()));
			}
			
			if (EdgeCore.isMaintenance() && (!Level.canUse(user, Level.SUPPORTER) || !EdgeCore.getInvitedPlayers().contains(player.getName()))) {
				e.disallow(null, ChatColor.RED + "Der Server befindet sich im Wartungsmodus!");
			}
			
		}
	}
}