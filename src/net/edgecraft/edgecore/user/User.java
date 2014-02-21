package net.edgecraft.edgecore.user;

import java.io.Serializable;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.chat.Channel;
import net.edgecraft.edgecore.chat.ChatHandler;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.db.DatabaseHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String ip;
	private Level lvl;
	private String language;
	private boolean banned;
	private String banreason;
	
	private final DatabaseHandler db = EdgeCore.getDB();
	
	protected User() { /* ... */ } 
	
	/**
	 * Updates the users' level
	 * @param level
	 * @throws Exception
	 */
	public void updateLevel(Level lvl) throws Exception {
		
			update( "level", lvl.value() );
			setLevel( lvl );
	}
	
	/**
	 * Updates the users' language
	 * @param language
	 * @throws Exception
	 */
	public void updateLanguage(String language) throws Exception {

		update( "language", language );	
		setLanguage(language);
	}
	
	private void update( String var, Object obj ) throws Exception {
		if( var != null && obj != null ) {
			this.db.executeUpdate("UPDATE " + UserManager.userTable + " SET " + var + " = '" + obj.toString() + "' WHERE id = '" + this.id + "';");
		}
	}
	
	/**
	 * (Un)bans the user
	 * @param status
	 * @throws Exception
	 */
	public void setBanned(boolean status) throws Exception {
		this.banned = status;
		
		if( !status) {
			setBanReason("");
		}
		
		int banned_ = status ? 1 : 0;
		this.db.executeUpdate("UPDATE " + UserManager.userTable + " SET banned = '" + banned_ + "' WHERE id = '" + this.id + "';");
	}
	
	/**
	 * Updates the ban-reason
	 * @param reason
	 * @throws Exception
	 */
	public void updateBanReason(String reason) throws Exception {
		if( reason != null ) {
			setBanReason( reason );
			this.db.executeUpdate("UPDATE " + UserManager.userTable + " SET banreason = '" + reason + "' WHERE id = '" + this.id + "';");
		}
	}
	
	/**
	 * Returns the users' id
	 * 
	 * @return Integer
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Returns the users' name
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the users' ip
	 * 
	 * @return String
	 */
	public String getIP() {
		return ip;
	}
	
	/**
	 * Returns the users' level
	 * 
	 * @return Integer
	 */
	public Level getLevel() {
		return lvl;
	}

	/**
	 * Returns the users' language-setting
	 * 
	 * @return String
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Checks whether the user is banned or not
	 * 
	 * @return true/false
	 */
	public boolean isBanned() {
		return banned;
	}
	
	/**
	 * Returns the reason for the ban
	 * Returns null if the user isn't banned
	 * 
	 * @return String
	 */
	public String getBanReason() {
		if( isBanned() )
			return banreason;
		
		return null;
	}
	
	/**
	 * Returns the bukkit-player-instance of the user
	 * 
	 * @return Player
	 */
	public Player getPlayer() {
		return Bukkit.getServer().getPlayerExact(this.name);
	}
	
	/**
	 * Returns the users' channel
	 * 
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
	 * Sets the users' id
	 * 
	 * @param id
	 */
	protected void setID(int id) {
		if( id >= 0 )
			this.id = id;
	}

	/**
	 * Sets the users' name
	 * @param name
	 */
	protected void setName(String name) {
		if( name != null )
			this.name = name;
	}

	/**
	 * Sets the users' ip
	 * @param ip
	 */
	protected void setIP(String ip) {
		if( ip != null ) 
			this.ip = ip;
	}

	/**
	 * Sets the users' level
	 * @param level
	 */
	protected void setLevel(Level lvl) {
			this.lvl = lvl;
	}
	
	
	/**
	 * Sets the users' language
	 * @param language
	 */
	protected void setLanguage(String language) {
		if( language != null )
			this.language = language;
	}
	
	/**
	 * Sets whether the users' ban-status
	 * @param status
	 */
	protected void setBanStatus(boolean status) {
		this.banned = status;
	}
	
	/**
	 * Sets the users' ban-reason
	 * @param reason
	 */
	protected void setBanReason(String reason) {
		if( reason != null )
			this.banreason = reason;
	}
	
	@Override
	public int hashCode() {
		return (int) getName().hashCode() * getIP().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		final User another = (User) obj;
		
		if (getName().equals(another.getName())) {
			if (getIP().equals(another.getIP())) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean muted = false;
	
	public boolean isMuted(){
		return muted;
	}
	
	public void setMuted(boolean mute){
		muted = mute;
	}
	
}
