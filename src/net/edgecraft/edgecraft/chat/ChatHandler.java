package net.edgecraft.edgecraft.chat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import net.edgecraft.edgecraft.EdgeCraft;

public class ChatHandler {
	
	private static int defaultMaxChannelMembers;
	public boolean chat;
	
	private final static Map<Integer, Channel> channels = new HashMap<>();
	
	public static int getDefaultMaxChannelMembers() {
		return ChatHandler.defaultMaxChannelMembers;
	}

	public static Map<Integer, Channel> getChannels() {
		return ChatHandler.channels;
	}


	public static void setDefaultMaxChannelMembers( int maxChannelMembers ) {
		if( maxChannelMembers > 0 )
			ChatHandler.defaultMaxChannelMembers = maxChannelMembers;
	}

	/**
	 * Generiert tempor�r verwendbare Channel-ID
	 * @return Integer
	 */
	protected int generateChannelTempID() {		
		return channels.size() + 1;
	}
	
	/**
	 * L�scht den angegebenen Channel
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
	 * Pr�ft, ob angegebener Channel existiert
	 * @param c
	 * @return true/false
	 */
	public boolean exists(Channel c) {
		return channels.values().contains(c);
	}
	
	/**
	 * Pr�ft, ob angegebener Channel existiert
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
	 * Gibt den zur angegebenen TempID geh�renden Channel zur�ck
	 * @param tempID
	 * @return Channel
	 */
	public Channel getChannel(int tempID) {
		return channels.get(tempID);
	}
	
	/**
	 * Gibt den angegebenen Channel zur�ck
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
	 * Pr�ft, ob der Chat nutzbar ist
	 * @return true/false
	 */
	public boolean isChatEnabled() {
		return this.chat;
	}
	
	/**
	 * Gib das festgelegte Chatformat zur�ck
	 * @param lang
	 * @return
	 */
	public String getChatFormat(String lang) {
		return EdgeCraft.getLang().getRawMessage(lang, "message", "chatformat");
	}
}
