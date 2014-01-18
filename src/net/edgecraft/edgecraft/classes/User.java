package net.edgecraft.edgecraft.classes;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.chat.Channel;
import net.edgecraft.edgecraft.chat.ChatHandler;
import net.edgecraft.edgecraft.mysql.DatabaseHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class User {
	
	private int id;
	private String name;
	private String ip;
	private int level; // deprecated soon.
	private String language;
	private boolean banned;
	private String banreason;
	
	private final DatabaseHandler db = EdgeCraft.getDB();
	
	protected User() { /* ... */ } 
	
	/**
	 * Updates the users' level.
	 * @param level
	 * @throws Exception
	 */
	public void updateLevel(int level) throws Exception {
		if( level >= 0 ) {
			setLevel( level );
			db.executeUpdate("UPDATE edgecraft_users SET level = '" + level + "' WHERE id = '" + this.id + "';");
		}
	}
	
	/**
	 * Updates the users' language.
	 * @param language
	 * @throws Exception
	 */
	public void updateLanguage(String language) throws Exception {
		setLanguage(language);
		this.db.executeUpdate("UPDATE edgecraft_users SET language = '" + language + "' WHERE id = '" + this.id + "';");	
	}
	
	/**
	 * (Un)bans the user.
	 * @param status
	 * @throws Exception
	 */
	public void setBanned(boolean status) throws Exception {
		this.banned = status;
		
		if( !status) {
			this.banreason = "";
		}
		
		int banned_ = status ? 1 : 0;
		this.db.executeUpdate("UPDATE edgecraft_users SET banned = '" + banned_ + "' WHERE id = '" + this.id + "';");
	}
	
	/**
	 * Updates the ban-reason.
	 * @param reason
	 * @throws Exception
	 */
	public void updateBanReason(String reason) throws Exception {
		if( reason != null ) {
			setBanReason( reason );
			this.db.executeUpdate("UPDATE edgecraft_users SET banreason = '" + reason + "' WHERE id = '" + this.id + "';");
		}
	}
	
	/**
	 * Returns the users' id.
	 * @return Integer
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Returns the users' name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the users' ip.
	 * @return String
	 */
	public String getIP() {
		return ip;
	}
	
	/**
	 * Returns the users' level.
	 * @return Integer
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Returns the users' language-setting.
	 * @return String
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Checks whether the user is banned or not.
	 * @return true/false
	 */
	public boolean isBanned() {
		return banned;
	}
	
	/**
	 * Returns the reason for the ban.
	 * Returns null if the user isn't banned.
	 * @return String
	 */
	public String getBanReason() {
		if( isBanned() )
			return banreason;
		
		return null;
	}
	
	/**
	 * Returns the bukkit-player-instance of the user.
	 * @return Player
	 */
	public Player getPlayer() {
		return Bukkit.getServer().getPlayerExact(this.name);
	}
	
	/**
	 * Returns the users' channel.
	 * @return Channel
	 */
	public Channel getChannel() {
		Channel c = null;
		
		for (Channel channel : ChatHandler.getChannels().values()) {
			if (channel.isMember(this)) {
				c = channel;
				break;
			}
		}
		
		return  c;
	}
	
	
	/**
	 * Sets the users' id.
	 * @param id
	 */
	protected void setID(int id) {
		if( id >= 0 )
			this.id = id;
	}

	/**
	 * Sets the users' name.
	 * @param name
	 */
	protected void setName(String name) {
		if( name != null )
			this.name = name;
	}

	/**
	 * Sets the users' ip.
	 * @param ip
	 */
	protected void setIP(String ip) {
		if( ip != null ) 
			this.ip = ip;
	}

	/**
	 * Sets the users' level.
	 * @param level
	 */
	protected void setLevel(int level) {
		if( level >= 0 )
			this.level = level;
	}

	/**
	 * Sets the users' language.
	 * @param language
	 */
	protected void setLanguage(String language) {
		if( language != null )
			this.language = language;
	}
	
	/**
	 * Sets whether the users' ban-status.
	 * @param status
	 */
	protected void setBanStatus(boolean status) {
		this.banned = status;
	}
	
	/**
	 * Sets the users' ban-reason.
	 * @param reason
	 */
	protected void setBanReason(String reason) {
		if( reason != null )
			this.banreason = reason;
	}
}
