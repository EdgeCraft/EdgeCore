package net.edgecraft.edgecore.chat;

import java.util.HashMap;
import java.util.Map;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;
import net.edgecraft.edgecore.user.UserManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ManageChatListener implements Listener {
	
	private final UserManager userManager = EdgeCore.getUsers();
	private final LanguageHandler lang = EdgeCore.getLang();
	
	private Map<String, Boolean> canChat = new HashMap<>();
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		
		if (!canChat.containsKey(e.getPlayer().getName()))
			canChat.put(e.getPlayer().getName(), true);
		
		if (!EdgeCore.getChat().isChatEnabled()) {
			e.setCancelled(true);
		}
		
		final Player p = e.getPlayer();		
		User user = userManager.getUser(p.getName());				
		String msg = e.getMessage();
		
		if (user == null) {
			e.setCancelled(true);
			return;
		}
		
		if (user != null) {
			
			if (!canChat.get(p.getName()) && !Level.canUse(user, Level.SUPPORTER)) {
				
				p.sendMessage(lang.getColoredMessage(user.getLanguage(), "info_spam"));
				e.setCancelled(true);
				
			} else {
				
				canChat.put(p.getName(), false);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(EdgeCore.getInstance(), new Runnable() {
					
					public void run() {
						canChat.put(p.getName(), true);
					}
					
				}, 20L);
			}
			
			if(user.isMuted()){
				p.sendMessage(lang.getColoredMessage(user.getLanguage(), "info_mute").replace("[0]", "Unknown"));
				e.setCancelled(true);
			}
						
			if (user.getChannel() != null) {
				Channel c = user.getChannel();
				
				if (c.getRequiredItem() != null) {
					if (p.getItemInHand().getType() == c.getRequiredItem()) {
						
						c.send(user.getName(), msg);
						e.setCancelled(true);
						
					} 
				}
				
				if (msg.startsWith(">")) {
					
					if (msg.length() <= 1) e.setCancelled(true);
					e.setFormat(lang.getRawMessage(LanguageHandler.getDefaultLanguage(), "message", "chatformat").replace("[0]", user.getLevel().getColor() + user.getPrefix() + ChatColor.RESET)
																											.replace("[1]", user.getSuffix() + ChatColor.RESET).replace("[2]", p.getName())
																											.replace("[3]", msg.substring(1, msg.length())));
					
				} else {
					
					c.send(user.getName(), msg);
					e.setCancelled(true);
					
				}
				
			} else {
				
				if (Level.canUse(user, Level.ARCHITECT)) {
					
					e.setFormat(lang.getRawMessage(LanguageHandler.getDefaultLanguage(), "message", "chatformat").replace("[0]", user.getLevel().getColor() + user.getPrefix() + ChatColor.RESET)
							.replace("[1]", user.getLevel().getColor() + user.getSuffix() + ChatColor.RESET).replace("[2]", p.getName())
							.replace("[3]", ChatColor.translateAlternateColorCodes('&', msg)));
					
				} else {
					
					e.setFormat(lang.getRawMessage(LanguageHandler.getDefaultLanguage(), "message", "chatformat").replace("[0]", user.getLevel().getColor() + user.getPrefix() + ChatColor.RESET)
							.replace("[1]", user.getLevel().getColor() + user.getSuffix() + ChatColor.RESET).replace("[2]", p.getName())
							.replace("[3]", msg));
					
				}
			}
		}
	}
}
