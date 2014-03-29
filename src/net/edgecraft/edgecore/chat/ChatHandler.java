package net.edgecraft.edgecore.chat;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import net.edgecraft.edgecore.EdgeCore;

public class ChatHandler {
	
	public static int chatMessagesSent;
	public static int channelMessagesSent;
	
	private static int defaultMaxChannelMembers;
	public boolean chat;
	
	private final static Map<Integer, Channel> channels = new HashMap<>();
	
	private static final ChatHandler instance = new ChatHandler();
	
	protected ChatHandler(){ /* ... */ }
	
	public static final ChatHandler getInstance() {
		return instance;
	}
	
	/**
	 * Sends a message to the server.
	 * @param msg
	 */
	public void broadcast(String msg) {
		if (!isChatEnabled()) return;
		Bukkit.getServer().broadcastMessage(msg);
	}
		
	/**
	 * Generates a temporarily used channel-id
	 * @return Integer
	 */
	protected int generateChannelTempID() {		
		return channels.size() + 1;
	}
	
	/**
	 * Adds the given channel to the channels-map.
	 * @param c
	 */
	public void addChannel(Channel c) {
		Validate.notNull(c);
		channels.put(c.getTempID(), c);
	}
	
	/**
	 * Deletes the given channel.
	 * @param c
	 */
	public Channel deleteChannel(Channel c) {
		return channels.remove(c.getTempID());
	}
		
	/**
	 * Enables/disables the chat.
	 * @param chat
	 */
	public void enableChat(boolean chat) {
		this.chat = chat;
	}
	
	/**
	 * Checks whether the given channel exists or not.
	 * @param c
	 * @return true/false
	 */
	public boolean exists(Channel c) {
		return channels.values().contains(c);
	}
	
	/**
	 * Checks whether the given channel exists or not.
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
	 * Checks whether the chat is enabled or disabled.
	 * @return true/false
	 */
	public boolean isChatEnabled() {
		return this.chat;
	}
		
	/**
	 * Returns the with the tempID specified channel.
	 * @param tempID
	 * @return Channel
	 */
	public Channel getChannel(int tempID) {
		return channels.get(tempID);
	}
	
	/**
	 * Returns the with the name specified channel.
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
	 * Returns the fixed chatformat.
	 * @param lang
	 * @return
	 */
	public String getChatFormat(String lang) {
		return EdgeCore.getLang().getRawMessage(lang, "message", "chatformat");
	}
	
	/**
	 * Returns the maximum amount of channel members.
	 * @return
	 */
	public static int getDefaultMaxChannelMembers() {
		return ChatHandler.defaultMaxChannelMembers;
	}

	/**
	 * Returns the map with all channels in the handler.
	 * @return
	 */
	public static Map<Integer, Channel> getChannels() {
		return ChatHandler.channels;
	}	
		
	/**
	 * Sets the default maximum of channel-members.
	 * @param maxChannelMembers
	 */
	public static void setDefaultMaxChannelMembers( int maxChannelMembers ) {
	    Validate.isTrue(maxChannelMembers > 0, "The value must be greater than zero: %s", maxChannelMembers);
			ChatHandler.defaultMaxChannelMembers = maxChannelMembers;
	}
}
