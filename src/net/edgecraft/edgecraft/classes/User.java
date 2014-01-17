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
	private int level;
	private String language;
	private boolean banned;
	private String banreason;
	
	private final DatabaseHandler db = EdgeCraft.getDB();
	
	protected User() { }
	
	/**
	 * Gibt die User-ID zurück
	 * @return Integer
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Gibt den User-Name zurück
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gibt die User-IP zurück
	 * @return String
	 */
	public String getIP() {
		return ip;
	}
	
	/**
	 * Gibt das User-Level zurück
	 * @return Integer
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gibt die eingestellte User-Sprache zurück
	 * @return String
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Gibt zurück, ob Spieler gebannt ist
	 * @return true/false
	 */
	public boolean isBanned() {
		return banned;
	}
	
	/**
	 * Gibt den Grund der evtl. vorhandenen Bannung zurück
	 * @return String
	 */
	public String getBanReason() {
		return banreason;
	}
	
	/**
	 * Gibt den zugehörigen Bukkit-Player zurück
	 * @return Player
	 */
	public Player getPlayer() {
		return Bukkit.getServer().getPlayerExact(this.name);
	}
	
	/**
	 * Gibt den Channel zurück, in welchem der Spieler schreibt
	 * @return Channel
	 */
	public Channel getChannel() {
		Channel c = null;
		
		for (Channel channel : ChatHandler.channels.values()) {
			if (channel.isMember(this)) {
				c = channel;
				break;
			}
		}
		
		return  c;
	}
	
	/**
	 * Erneuert das User-Level in der Datenbank
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
	 * Erneuert die User-Sprache in der Datenbank
	 * @param language
	 * @throws Exception
	 */
	public void updateLanguage(String language) throws Exception {
		setLanguage(language);
		this.db.executeUpdate("UPDATE edgecraft_users SET language = '" + language + "' WHERE id = '" + this.id + "';");	
	}
	
	/**
	 * Ent-/Bannt einen Spieler in der Datenbank
	 * @param status
	 * @throws Exception
	 */
	public void setBanned(boolean status) throws Exception {
		this.banned = status;
		
		int banned_ = status ? 1 : 0;
		this.db.executeUpdate("UPDATE edgecraft_users SET banned = '" + banned_ + "' WHERE id = '" + this.id + "';");
	}
	
	/**
	 * Erneuert den evtl. vorhandenen Banngrund
	 * @param reason
	 * @throws Exception
	 */
	public void updateBanReason(String reason) throws Exception {
		if( reason != null ) {
			setBanReason( reason );
			this.db.executeUpdate("UPDATE edgecraft_users SET banreason = '" + reason + "' WHERE id = '" + this.id + "';");
		}
	}
	
	protected void setID(int id) {
		if( id >= 0 )
			this.id = id;
	}

	protected void setName(String name) {
		if( name != null )
			this.name = name;
	}

	protected void setIP(String ip) {
		if( ip != null ) 
			this.ip = ip;
	}

	protected void setLevel(int level) {
		if( level >= 0 )
			this.level = level;
	}

	protected void setLanguage(String language) {
		if( language != null )
			this.language = language;
	}
	
	protected void setBanStatus(boolean status) {
		this.banned = status;
	}
	
	protected void setBanReason(String reason) {
		if( reason != null )
			this.banreason = reason;
	}
}
