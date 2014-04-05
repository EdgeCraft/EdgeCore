package net.edgecraft.edgecore.user;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.chat.Channel;
import net.edgecraft.edgecore.chat.ChatHandler;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.db.DatabaseHandler;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class User {
	
	private int id;
	private String name;
	private String ip;
	
	private Level lvl;
	private Location lastLocation;
	
	private String prefix;
	private String suffix;
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
	
	public void updateLastLocation(Location loc) throws Exception {
		
		update( "lastlocation", loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getYaw() + "," + loc.getPitch());
	}
	
	/**
	 * Updates the users' prefix
	 * @param prefix
	 * @throws Exception
	 */
	public  void updatePrefix(String prefix) throws Exception {
		
		update( "prefix", prefix);
		setPrefix(prefix);
		
	}
	
	/**
	 * Updates the users' suffix
	 * @param suffix
	 * @throws Exception
	 */
	public  void updateSuffix(String suffix) throws Exception {
		
		update( "suffix", suffix);
		setSuffix(suffix);
		
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
			this.db.prepareStatement("UPDATE " + UserManager.userTable + " SET " + var + " = '" + obj.toString() + "' WHERE id = '" + this.id + "';").executeUpdate();
		}
	}
	
	/**
	 * Updates the users' ip
	 * @param ip
	 * @throws Exception
	 */
	public void updateIP(String ip) throws Exception {
		
		update("ip",  ip);
		setIP(ip);
		
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
		update("banned", banned_);
	}
	
	/**
	 * Updates the ban-reason
	 * @param reason
	 * @throws Exception
	 */
	public void updateBanReason(String reason) throws Exception {
		Validate.notNull(reason);
		
		setBanReason( reason );
		update("banreason", reason);
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
	 * Returns the last location the user was seen at
	 * 
	 * @return Location
	 */
	public Location getLastLocation() {
		return lastLocation;
	}
	
	/**
	 * Returns the users' prefix
	 * @return String
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Returns the users' suffix
	 * @return String
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Returns the users' language-setting
	 * 
	 * @return String
	 */
	public String getLanguage() {
		return language;
	}
	
	public String getLang()
	{
		return getLanguage();
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
		Player ret = Bukkit.getServer().getPlayerExact( getName() );
		
		if( ret == null ) {
			EdgeCore.log.info("[USER]Player " + getName() + " doesn't exist.");
		}
		
		return ret;
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
		Validate.notNull(name);
		this.name = name;
	}

	/**
	 * Sets the users' ip
	 * @param ip
	 */
	protected void setIP(String ip) {
	    Validate.notNull(ip);
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
	 * Sets the users' last location
	 * @param loc
	 */
	protected void setLastLocation(Location loc) {
		Validate.notNull(loc);
		this.lastLocation = loc;
	}
	
	/**
	 * Sets the users' prefix
	 * @param prefix
	 */
	protected void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * Sets the users' suffix
	 * @param suffix
	 */
	protected void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	/**
	 * Sets the users' language
	 * @param language
	 */
	protected void setLanguage(String language) {
		Validate.notNull(language);
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
		Validate.notNull(reason);
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
