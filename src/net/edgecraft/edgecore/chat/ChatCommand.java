package net.edgecraft.edgecore.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class ChatCommand extends AbstractCommand {
	
	private static final ChatCommand instance = new ChatCommand();
	
	private ChatCommand() { super(); }
	
	public static ChatCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[] { "chat" };
	}

	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		return args.length == 2;
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {	
		
		sender.sendMessage(EdgeCore.usageColor + "/chat info");
		sender.sendMessage(EdgeCore.usageColor + "/chat toggle");
		sender.sendMessage(EdgeCore.usageColor + "/chat clear");
		sender.sendMessage(EdgeCore.usageColor + "/chat reset");
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		String userLang = user.getLanguage();
		
		if (args[1].equalsIgnoreCase("info")) {
			if (args.length != 2) {
				sendUsage(player);
				return true;
			}
			
			String chatKey = ChatHandler.getInstance().isChatEnabled() ? "admin_chat_info_enabled_true" : "admin_chat_info_enabled_false";
			int mutedPlayers = 0;
			
			player.sendMessage(lang.getColoredMessage(userLang, "admin_chat_info_title"));
			player.sendMessage(lang.getColoredMessage(userLang, chatKey));
			player.sendMessage(lang.getColoredMessage(userLang, "admin_chat_info_chatmessages").replace("[0]", ChatHandler.chatMessagesSent + ""));
			player.sendMessage(lang.getColoredMessage(userLang, "admin_chat_info_channelmessages").replace("[0]", ChatHandler.channelMessagesSent + ""));
			
			for (User u : users.getUsers().values()) {
				if (u.isMuted())
					mutedPlayers++;
			}
			
			player.sendMessage(lang.getColoredMessage(userLang, "admin_chat_info_mutedplayers").replace("[0]", mutedPlayers + ""));
			
			return true;
		}
		
		if (args[1].equalsIgnoreCase("toggle")) {
			if (args.length != 2) {
				sendUsage(player);
				return true;
			}
			
			if (ChatHandler.getInstance().isChatEnabled()) {
				
				ChatHandler.getInstance().enableChat(false);
				player.sendMessage(lang.getColoredMessage(userLang, "admin_chat_toggle_disabled"));
				
			} else {
				
				ChatHandler.getInstance().enableChat(true);
				player.sendMessage(lang.getColoredMessage(userLang, "admin_chat_toggle_enabled"));
				
			}
			
			return true;
		}
		
		if (args[1].equalsIgnoreCase("clear")) {
			if (args.length != 2) {
				sendUsage(player);
				return true;
			}
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				for (int i = 0; i < 100; i++) {
					p.sendMessage("");
				}
			}
			
			player.sendMessage(lang.getColoredMessage(userLang, "admin_chat_clear_success"));
			
			return true;
		}
		
		if (args[1].equalsIgnoreCase("reset")) {
			if (args.length != 2) {
				sendUsage(player);
				return true;
			}
			
			ChatHandler.channelMessagesSent = 0;
			ChatHandler.chatMessagesSent = 0;
			
			player.sendMessage(lang.getColoredMessage(userLang, "admin_chat_reset_success"));
			
			return true;
		}
		
		return true;
	}
	
}
