package net.edgecraft.edgecraft.chat;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.classes.User;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.util.LanguageHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ManageChatEvent implements Listener {
	
	private final UserManager userManager = EdgeCraft.getUsers();
	private final LanguageHandler lang = EdgeCraft.getLang();
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		
		if (!EdgeCraft.getChat().isChatEnabled()) {
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
