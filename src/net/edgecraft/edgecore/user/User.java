package net.edgecraft.edgecore.user;

import java.util.UUID;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.chat.Channel;
import net.edgecraft.edgecore.chat.ChatHandler;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.db.DatabaseHandler;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class User {
	
	@Deprecated
	private int id;
	
	private UUID uuid;
	private String name;
	private String ip;
	
	private Level level;
	private Location lastLocation;
	
	private String prefix;
	private String suffix;
	private String language;
	
	private boolean banned;
	private String banReason;
	private boolean muted;
	private String muteReason;
	
	private final DatabaseHandler db = EdgeCore.getDB();
	
	protected User() { /* ... */ }
	
	@Deprecated
	public int getID() {
		return id;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIP() {
		return ip;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Location getLastLocation() {
		return lastLocation;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public String getLang() {
		return getLanguage();
	}
	
	public boolean isBanned() {
		return banned;
	}
	
	public String getBanReason() {
		return banReason;
	}
	
	public boolean isMuted() {
		return muted;
	}
	
	public String getMuteReason() {
		return muteReason;
	}
	
	public Player getPlayer() {
		return EdgeCore.getInstance().getServer().getPlayer(getUUID());
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return EdgeCore.getInstance().getServer().getOfflinePlayer(getUUID());
	}
	
	public Channel getChannel() {
		for (Channel c : ChatHandler.getChannels().values()) {
			if (ChatHandler.getInstance().exists(c) && c.isMember(this))
				return c;
		}
		
		return null;
	}
	
	private void update(String var, Object obj) throws Exception {
		if (var != null && obj != null)
			this.db.prepareStatement("UPDATE " + UserManager.userTable + " SET " + var + " = '" + obj.toString() + "' WHERE uuid = '" + getUUID() + "';").executeUpdate();
	}
	
	public void updateName(String name) throws Exception {
		setName(name);
		update("name", name);
	}
	
	public void updateIP(String ip) throws Exception {
		setIP(ip);
		update("ip", ip);
	}
	
	public void updateLevel(Level level) throws Exception {
		setLevel(level);
		update("level", level.value());
	}
	
	public void updateLastLocation(Location loc) throws Exception {
		setLastLocation(loc);
		update( "lastlocation", loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "," + loc.getYaw() + "," + loc.getPitch());
	}
	
	public void updatePrefix(String prefix) throws Exception {
		setPrefix(prefix);
		update("prefix", prefix);
	}
	
	public void updateSuffix(String suffix) throws Exception {
		setSuffix(suffix);
		update("suffix", suffix);
	}
	
	public void updateLanguage(String lang) throws Exception {
		setLanguage(lang);
		update("language", lang);
	}
	
	public void setBanned(boolean var) throws Exception {
		setBanned(var);
		
		int _var = var ? 1 : 0;
		update("banned", _var);
	}
	
	public void updateBanReason(String reason) throws Exception {
		setBanReason(reason);
		update("banreason", reason);
	}
	
	public void mute(boolean var) throws Exception {
		setMuted(var);
		
		int _var = var ? 1 : 0;
		update("muted", _var);
	}
	
	public void updateMuteReason(String reason) throws Exception {
		setMuteReason(reason);
		update("mutereason", reason);
	}
	
	protected void setUUID(UUID uuid) {
		if (uuid != null)
			this.uuid = uuid;
	}
	
	protected void setName(String name) {
		if (name != null)
			this.name = name;
	}
	
	protected void setIP(String ip) {
		if (ip != null)
			this.ip = ip;
	}
	
	protected void setLevel(Level level) {
		if (level != null)
			this.level = level;
	}
	
	protected void setLastLocation(Location loc) {
		if (loc != null)
			this.lastLocation = loc;
	}
	
	protected void setPrefix(String prefix) {
		if (prefix != null)
			this.prefix = prefix;
	}
	
	protected void setSuffix(String suffix) {
		if (suffix != null)
			this.suffix = suffix;
	}
	
	protected void setLanguage(String lang) {
		if (lang != null)
			this.language = lang;
	}
	
	protected void setBanStatus(boolean var) {
		this.banned = var;
	}
	
	protected void setBanReason(String reason) {
		if (reason != null)
			this.banReason = reason;
	}
	
	protected void setMuted(boolean var) {
		this.muted = var;
	}
	
	protected void setMuteReason(String reason) {
		this.muteReason = reason;
	}
	
	@Override
	public String toString() {
		return "User {" + getUUID() + " ; " + getName() + " ; " + getIP() + " ; " + getPrefix() + " ; " + getSuffix();
	}
	
	@Override
	public int hashCode() {
		return (int) uuid.hashCode() * name.hashCode() * ip.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		final User another = (User) obj;
		
		if (getUUID().equals(another.getUUID())) {
			if (getName().equals(another.getName())) {
				if (getIP().equals(another.getIP())) {
					return true;
				}
			}
		}
		
		return false;
	}
}
