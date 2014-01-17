package net.edgecraft.edgecraft.chat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import net.edgecraft.edgecraft.EdgeCraft;

public class ChatHandler {
	
	private static int defaultMaxChannelMembers;
	public boolean chat;
	
	private final static Map<Integer, Channel> channels = new HashMap<>();
	
	public int getDefaultMaxChannelMembers() {
		return ChatHandler.defaultMaxChannelMembers;
	}

	public Map<Integer, Channel> getChannels() {
		return ChatHandler.channels;
	}


	public void setDefaultMaxChannelMembers( int maxChannelMembers ) {
		if( maxChannelMembers > 0 )
			ChatHandler.defaultMaxChannelMembers = maxChannelMembers;
	}

	/**
	 * Generiert temporär verwendbare Channel-ID
	 * @return Integer
	 */
	protected int generateChannelTempID() {		
		return channels.size() + 1;
	}
	
	/**
	 * Löscht den angegebenen Channel
	 * @param c
	 */
	public void deleteChannel(Channel c) {
		channels.remove(c.getTempID());
	}
	
	public void broadcast(String msg) {
		if (!isChatEnabled()) return;
		Bukkit.getServer().broadcastMessage(msg);
	}
	
	/**
	 * Regelt die Nutzbarkeit des kompletten Chats
	 * @param chat
	 */
	public void enableChat(boolean chat) {
		this.chat = chat;
	}
	
	/**
	 * Prüft, ob angegebener Channel existiert
	 * @param c
	 * @return true/false
	 */
	public boolean exists(Channel c) {
		return channels.values().contains(c);
	}
	
	/**
	 * Prüft, ob angegebener Channel existiert
	 * @param channel
	 * @return true/false
	 */
	public boolean exists(String channel) {
		for (Channel c : channels.values()) {
			if (c.getChannelName().equalsIgnoreCase("channel")) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gibt den zur angegebenen TempID gehörenden Channel zurück
	 * @param tempID
	 * @return Channel
	 */
	public Channel getChannel(int tempID) {
		return channels.get(tempID);
	}
	
	/**
	 * Gibt den angegebenen Channel zurück
	 * @param name
	 * @return Channel
	 */
	public Channel getChannel(String name) {
		for (Channel c : channels.values()) {
			if (c.getChannelName().equalsIgnoreCase(name)) {
				return c;
			}
		}
		
		return null;
	}
	
	/**
	 * Prüft, ob der Chat nutzbar ist
	 * @return true/false
	 */
	public boolean isChatEnabled() {
		return this.chat;
	}
	
	/**
	 * Gib das festgelegte Chatformat zurück
	 * @param lang
	 * @return
	 */
	public String getChatFormat(String lang) {
		return EdgeCraft.lang.getRawMessage(lang, "message", "chatformat");
	}
}
