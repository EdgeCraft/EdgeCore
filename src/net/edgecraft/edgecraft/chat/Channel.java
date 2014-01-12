package net.edgecraft.edgecraft.chat;

import java.util.ArrayList;
import java.util.List;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.classes.User;
import net.edgecraft.edgecraft.util.LanguageHandler;

public class Channel {
	
	private int tempID;
	private String name;
	private String password;
	private int maxMembers;
	private boolean listed;
	
	private String admin;
	private List<User> members = new ArrayList<User>();
	
	private final ChatHandler chatHandler = EdgeCraft.chat;
	private final LanguageHandler lang = EdgeCraft.lang;
	
	public Channel(String name, boolean listed) {
		this.tempID = chatHandler.generateChannelTempID();
		this.name = name;
		this.maxMembers = ChatHandler.defaultMaxChannelMembers;
		this.listed = listed;
		this.admin = "Admin";
		
		ChatHandler.channels.put(getTempID(), this);
	}
	
	public Channel (String name, String password, boolean listed) {
		this.tempID = chatHandler.generateChannelTempID();
		this.name = name;
		this.password = password;
		this.maxMembers = ChatHandler.defaultMaxChannelMembers;
		this.listed = listed;
		this.admin = "Admin";
		
		ChatHandler.channels.put(getTempID(), this);
	}
	
	public Channel(String name, String password, String admin, boolean listed) {
		this.tempID = chatHandler.generateChannelTempID();
		this.name = name;
		this.password = password;
		this.maxMembers = ChatHandler.defaultMaxChannelMembers;
		this.admin = admin;
		this.listed = listed;
		
		ChatHandler.channels.put(getTempID(), this);
	}
	
	public Channel(String name, String password, int maxMembers, String admin, boolean listed) {
		this.tempID = chatHandler.generateChannelTempID();
		this.name = name;
		this.password = password;
		this.maxMembers = maxMembers;
		this.admin = admin;
		this.listed = listed;
		
		ChatHandler.channels.put(getTempID(), this);
	}
	
	public Channel(String name, String password, int maxMembers, String admin, List<User> members, boolean listed) {
		this.tempID = chatHandler.generateChannelTempID();
		this.name = name;
		this.password = password;
		this.maxMembers = maxMembers;
		this.admin = admin;
		this.members = members;
		this.listed = listed;
		
		ChatHandler.channels.put(getTempID(), this);
	}
	
	public Channel(String name, String password, String admin, List<User> members, boolean listed) {
		this.tempID = chatHandler.generateChannelTempID();
		this.name = name;
		this.password = password;
		this.maxMembers = members.size();
		this.admin = admin;
		this.members = members;
		this.listed = listed;
		
		ChatHandler.channels.put(getTempID(), this);
	}
	
	public Channel(String name, List<User> members, boolean listed) {
		this.tempID = chatHandler.generateChannelTempID();
		this.name = name;
		this.members = members;
		this.listed = listed;
		this.admin = "Admin";
		
		ChatHandler.channels.put(getTempID(), this);
	}
	
	/**
	 * Gibt Channel-ID zurück
	 * @return Integer
	 */
	public int getTempID() {
		return this.tempID;
	}
	
	/**
	 * Gibt Channel-Namen zurück
	 * @return String
	 */
	public String getChannelName() {
		return this.name;
	}
	
	/**
	 * Gibt Channel-Passwort zurück
	 * @return String
	 */
	public String getChannelPassword() {
		return this.password;
	}
	
	/**
	 * Prüft ob angegebener String mit dem Passwort übereinstimmt 
	 * @param pw
	 * @return true/false
	 */
	public boolean isPassword(String pw) {
		return getChannelPassword().equals(pw);
	}
	
	/**
	 * Gibt maximale Anzahl von Mitgliedern zurück
	 * @return Integer
	 */
	public int getMaxMembers() {
		return this.maxMembers;
	}
	
	/**
	 * Prüft, ob Channel sichtbar ist
	 * @return true/false
	 */
	public boolean isListed() {
		return this.listed;
	}
	
	/**
	 * Gibt Channel-Admin zurück
	 * Default: "Admin"
	 * @return String
	 */
	public String getChannelAdmin() {
		return this.admin;
	}
	
	/**
	 * Prüft, ob angegebener Spieler der Admin ist
	 * @param user
	 * @return true/false
	 */
	public boolean isChannelAdmin(String user) {
		return getChannelAdmin().equalsIgnoreCase(user);
	}
	
	/**
	 * Gibt alle Channel-Mitglieder zurück
	 * @return List<User>
	 */
	public List<User> getChannelMembers() {
		return this.members;
	}
	
	/**
	 * Gibt eine Liste (String) von allen Channel-Mitgliedern zurück
	 * @return String
	 */
	public String getMemberList() {
		
		StringBuilder sb = new StringBuilder();
		
		for (User user : members) {
			if (sb.length() > 0) sb.append(", ");
			if (members.contains(user)) sb.append(user.getName());
		}
		
		return sb.toString();
	}
	
	/**
	 * Prüft, ob der angegebene Spieler im Channel vertreten ist
	 * @param member
	 * @return true/false
	 */
	public boolean isMember(User member) {
		return getChannelMembers().contains(member);
	}
	
	/**
	 * Fügt einen Spieler hinzu
	 * @param member
	 */
	public void addMember(User member) {
		if (!isMember(member)) {
			this.members.add(member);
			
		} else return;
	}
	
	/**
	 * Entfernt einen Spieler
	 * @param member
	 */
	public void removeMember(User member) {
		if (isMember(member)) {
			this.members.remove(member);
			
		} else return;
	}
	
	/**
	 * Sendet eine Nachricht an alle Mitglieder des Channels
	 * @param message
	 */
	public void broadcast(String message) {
		for (User user : getChannelMembers()) {
			if (isMember(user)) {
				String channelPrefix = lang.getColoredMessage(user.getLanguage(), "channelprefix").replace("[0]", getChannelName());
				String channelBroadcast = lang.getColoredMessage(user.getLanguage(), "channelprefix_broadcast").replace("[0]", channelPrefix).replace("[1]", message);
				
				user.getPlayer().sendMessage(channelBroadcast);
				
			} else return;
		}
	}
	
	/**
	 * Sendet vom angegebenen Spieler aus eine Nachricht in den Channel
	 * @param player
	 * @param message
	 */
	public void send(String player, String message) {
		for (User user : getChannelMembers()) {
			if (isMember(user)) {
				
				String channelPrefix = lang.getColoredMessage(user.getLanguage(), "channelprefix").replace("[0]", getChannelName());
				String channelMessageAdmin = lang.getColoredMessage(user.getLanguage(), "channelprefix_message_admin").replace("[0]", channelPrefix).replace("[1]", player).replace("[2]", message);
				String channelMessage = lang.getColoredMessage(user.getLanguage(), "channelprefix_message").replace("[0]", channelPrefix).replace("[1]", player).replace("[2]", message);
				
				if (this.isChannelAdmin(player)) {
					user.getPlayer().sendMessage(channelMessageAdmin);					
				} else {
					user.getPlayer().sendMessage(channelMessage);
				}
				
			} else return;
		}
	}
	
	/**
	 * Löscht den Channel
	 */
	public void delete() {
		this.chatHandler.deleteChannel(this);
	}
	
	/**
	 * Ändert den Channel-Namen
	 * @param newName
	 */
	public void changeName(String newName) {
		this.name = newName;
	}
	
	/**
	 * Ändert das Channel-Passwort
	 * @param newPassword
	 */
	public void changePassword(String newPassword) {
		this.password = newPassword;
	}
	
	/**
	 * Ändert den Channel-Admin
	 * @param newAdmin
	 */
	public void switchAdmin(String newAdmin) {
		this.admin = newAdmin;
	}
	
	/**
	 * Ändert die Anzahl der maximaler Mitglieder im Channel
	 * @param newAmount
	 */
	public void changeMaxMembers(int newAmount) {
		this.maxMembers = newAmount;
	}
	
	/**
	 * Macht den Channel un-/sichtbar
	 * @param val
	 */
	public void setListed(boolean val) {
		this.listed = val;
	}
}
