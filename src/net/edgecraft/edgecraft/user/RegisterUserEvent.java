package net.edgecraft.edgecraft.user;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.lang.LanguageHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RegisterUserEvent implements Listener {
	
	private final UserManager userManager = EdgeCraft.getUsers();
	private final LanguageHandler lang = EdgeCraft.getLang();
	
	/**
	 * TODO: Add senseful comment.
	 * @param event
	 */
	@EventHandler
	public void registerUser(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		
		if (!this.userManager.exists(player.getName())) {
			try {
				
				this.userManager.registerUser(player.getName(), player.getAddress().toString());
				User user = this.userManager.getUser(player.getName());
				
		        player.sendMessage(this.lang.getColoredMessage(user.getLanguage(), "registration_success"));
		        player.sendMessage(this.lang.getColoredMessage(user.getLanguage(), "registration_help"));				
				
			} catch(Exception e) {
				e.printStackTrace();
				player.sendMessage(this.lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "globalerror"));
			}
		}
	}
}
