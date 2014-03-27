package net.edgecraft.edgecore.chat;

import java.util.ArrayList;
import java.util.List;

import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class Channel {
	
	private int tempID;
	private String name;
	private String password;
	private int maxMembers;
	private boolean listed;
	
	private Material requiredItem;
	
	private String admin;
	private List<User> members = new ArrayList<User>();
	
	private final ChatHandler chatHandler = EdgeCoreAPI.chatAPI();
	private final LanguageHandler lang = EdgeCoreAPI.languageAPI();

	public static final String defaultAdmin = "Admin";
	
	// Constructors:
	public Channel(String name, String password, int maxMembers, String admin, List<User> members, boolean listed, Material requiredItem) {

		setTempID( chatHandler.generateChannelTempID() );
		setName( name );
		setPassword( password );
		setMaxMembers( maxMembers );
		setAdmin( admin );
		setMembers( members );
		setListed( listed );	
		setRequiredItem(requiredItem);
		
		ChatHandler.getChannels().put(getTempID(), this);
	}
	
	public Channel(String name, boolean listed, Material requiredItem) {
		
		this( name, "", ChatHandler.getDefaultMaxChannelMembers(), Channel.defaultAdmin, null, listed, requiredItem);
	}
	
	public Channel (String name, String password, boolean listed, Material requiredItem) {

		this( name, password, ChatHandler.getDefaultMaxChannelMembers(), Channel.defaultAdmin, null, listed, requiredItem);
	}
	
	public Channel(String name, String password, String admin, boolean listed, Material requiredItem) {

		this( name, password, ChatHandler.getDefaultMaxChannelMembers(), admin, null, listed, requiredItem );
	}
	
	public Channel(String name, String password, int maxMembers, String admin, boolean listed, Material requiredItem) {

		this( name, password, maxMembers, admin, null, listed, requiredItem );
	}
	
	public Channel(String name, String password, String admin, List<User> members, boolean listed, Material requiredItem) {

		this( name, password, members.size(), admin, members, listed, requiredItem );
	}

	public Channel(String name, List<User> members, boolean listed, Material requiredItem) {

		this( name, null, -1, Channel.defaultAdmin, members, listed, requiredItem );
	}
		

	/**
	 * Adds a new member to the channel.
	 * @param member
	 */
	public boolean addMember(User member) {
		return this.members.add(member);
	}
	
	/**
	 * Removes an existing User from the channel.
	 * @param member
	 */
	public boolean removeMember(User member) {
	    return this.members.remove(member);
	}
	
	/**
	 * Sends a message to all members of the channel.
	 * @param message
	 */
	public void broadcast(String message) {
		for (User user : getChannelMembers()) {
			String channelPrefix = lang.getColoredMessage(user.getLanguage(), "channelprefix").replace("[0]", getChannelName());
			String channelBroadcast = lang.getColoredMessage(user.getLanguage(), "channelprefix_broadcast").replace("[0]", channelPrefix).replace("[1]", message);
			
			user.getPlayer().sendMessage(channelBroadcast);
		}
	}
	
	/**
	 * Sends a message from the given User to the other channel-members.
	 * @param player
	 * @param message
	 */
	public void send(String player, String message) {
		
		for (User user : getChannelMembers()) {
			if (Bukkit.getPlayerExact(player) != null) {
				if (Bukkit.getPlayerExact(player).getItemInHand().getType() != getRequiredItem()) {
					
					Bukkit.getPlayerExact(player).sendMessage(lang.getColoredMessage(user.getLanguage(), "channel_invaliditem"));
					return;
					
				}
			}
			
			String channelPrefix = lang.getColoredMessage(user.getLanguage(), "channelprefix").replace("[0]", getChannelName());
			String channelMessageAdmin = lang.getColoredMessage(user.getLanguage(), "channelprefix_message_admin").replace("[0]", channelPrefix).replace("[1]", player).replace("[2]", message);
			String channelMessage = lang.getColoredMessage(user.getLanguage(), "channelprefix_message").replace("[0]", channelPrefix).replace("[1]", player).replace("[2]", message);
			
			if (this.isChannelAdmin(player)) {
				user.getPlayer().sendMessage(channelMessageAdmin);					
			} else {
				user.getPlayer().sendMessage(channelMessage);
			}
		}
	}
	
	/**
	 * Deletes the channel.
	 */
	public void delete() {
		this.chatHandler.deleteChannel(this);
	}
	
	
	
	/**
	 * Returns the id of the channel.
	 * @return Integer
	 */
	public int getTempID() {
		return this.tempID;
	}
	
	/**
	 * Returns the name of the channel.
	 * @return String
	 */
	public String getChannelName() {
		return this.name;
	}
	
	/**
	 * Returns the password of the channel.
	 * @return String
	 */
	public String getChannelPassword() {
		return this.password;
	}
	
	/**
	 * Checks whether the given String equals the password.
	 * @param pw
	 * @return true/false
	 */
	public boolean isPassword(String pw) {
		return getChannelPassword().equals(pw);
	}
	
	/**
	 * Returns the maximum amount of members.
	 * @return Integer
	 */
	public int getMaxMembers() {
		return this.maxMembers;
	}
	
	/**
	 * Checks whether the channel is visible or not.
	 * @return true/false
	 */
	public boolean isListed() {
		return this.listed;
	}
	
	/**
	 * Returns the required item
	 * @return Material
	 */
	public Material getRequiredItem() {
		return this.requiredItem;
	}
	
	/**
	 * Returns the admin of the channel.
	 * Default: "Admin"
	 * @return String
	 */
	public String getChannelAdmin() {
		return this.admin;
	}
	
	/**
	 * Checks whether the given User is the admin.
	 * @param user
	 * @return true/false
	 */
	public boolean isChannelAdmin(String user) {
		return getChannelAdmin().equalsIgnoreCase(user);
	}
	
	/**
	 * Returns all members of the channel.
	 * @return List<User>
	 */
	public List<User> getChannelMembers() {
		return this.members;
	}
	
	/**
	 * Returns a list with the user-names of all channel-members.
	 * @return String
	 */
	public String getMemberList() {
	    
	    return StringUtils.join(members, ", ");
	}
	
	/**
	 * Checks whether the given Nick already exists in the channel.
	 * @param member
	 * @return true/false
	 */
	public boolean isMember(User member) {
		return getChannelMembers().contains(member);
	}

	
	
	
	/**
	 * Changes the name of the channel.
	 * @param name
	 */
	public void setName(String name) {
		Validate.notNull(name);
		
		this.name = name;
	}
	
	/**
	 * Changes the password of the channel.
	 * @param password
	 */
	public void setPassword(String password) {
	    Validate.notNull(password);
	    
		this.password = password;
	}
	
	/**
	 * Changes the admin of the channel.
	 * @param admin
	 */
	public void setAdmin(String admin) {
	    Validate.notNull(admin);
		this.admin = admin;
	}
	
	/**
	 * Changes the amount of maximal User in the channel.
	 * @param maxMembers
	 */
	public void setMaxMembers(int maxMembers) {
	    Validate.isTrue(maxMembers > 0, "The value must be greater than zero: %s", maxMembers);
		this.maxMembers = maxMembers;
	}
	
	/**
	 * Changes the Visibility of the channel.
	 * @param val
	 */
	public void setListed(boolean val) {
		this.listed = val;
	}
	
	/**
	 * Sets the required material
	 * @param material
	 */
	public void setRequiredItem(Material material) {
		this.requiredItem = material;
	}
	
	/**
	 * Changes the id of the channel.
	 * @param tempID
	 */
	public void setTempID( int tempID ) {
	    Validate.isTrue(tempID >= 0, "The value must be greater or equal than zero: %s", tempID);
		this.tempID = tempID;
	}

	/**
	 * Changes the members of the channel.
	 * @param members
	 */
	public void setMembers( List<User> members ) {
		Validate.notNull(members);
		this.members = members;
	}
}
