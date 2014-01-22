package net.edgecraft.edgecore.chat;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;
import net.edgecraft.edgecore.user.UserManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ManageChatEvent implements Listener {
	
	private final UserManager userManager = EdgeCore.getUsers();
	private final LanguageHandler lang = EdgeCore.getLang();
	
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		
		if (!EdgeCore.getChat().isChatEnabled()) {
			e.setCancelled(true);
		}
		
		Player p = e.getPlayer();
		
		User user = userManager.getUser(p.getName());
		String msg = e.getMessage();
		
		if (user != null) {
			if (user.getChannel() != null) {
				Channel c = user.getChannel();
				
				if (msg.startsWith(">")) {
					
					if (msg.length() <= 1) e.setCancelled(true);
					e.setFormat(lang.getRawMessage(LanguageHandler.getDefaultLanguage(), "message", "chatformat").replace("[0]", "[P]")
																											.replace("[1]", "[S]").replace("[2]", p.getName())
																											.replace("[3]", msg.substring(1, msg.length())));
					
				} else {
					
					c.send(user.getName(), msg);
					e.setCancelled(true);
					
				}
				
			} else {
				e.setFormat(lang.getRawMessage(LanguageHandler.getDefaultLanguage(), "message", "chatformat").replace("[0]", "[P]")
						.replace("[1]", "[S]").replace("[2]", p.getName())
						.replace("[3]", msg.substring(1, msg.length())));
			}
		}
	}
}
