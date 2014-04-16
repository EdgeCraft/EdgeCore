package net.edgecraft.edgecore.user;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.db.DatabaseHandler;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class UserManager {
	
	public final static String userTable = "edgecore_users";
	
	private static Map<UUID, User> users = new LinkedHashMap<>();
	private static List<String> bannedIPs = new ArrayList<>();
	
	private static int defaultLevel;
	
	private final DatabaseHandler db = EdgeCore.getDB();
	
	protected static final UserManager instance = new UserManager();
		
	protected UserManager() { /* ... */ }

	public static final UserManager getInstance() {
		return instance;
	}
	
	/**
	 * Registers a new user.
	 * (local + db)
	 * @param name
	 * @param ip
	 */
	public void registerUser(Player p) {
		try {
			
			if (exists(p.getName()) || exists(p.getUniqueId())) return;
			
			String uuid = p.getUniqueId().toString();
			String lastloc = Bukkit.getWorlds().get(0).getName() + "," +
					Bukkit.getWorlds().get(0).getSpawnLocation().getBlockX() + "," +
					Bukkit.getWorlds().get(0).getSpawnLocation().getBlockY() + "," +
					Bukkit.getWorlds().get(0).getSpawnLocation().getBlockZ() + "," +
					Bukkit.getWorlds().get(0).getSpawnLocation().getYaw() + "," +
					Bukkit.getWorlds().get(0).getSpawnLocation().getPitch();
			
			PreparedStatement registerUser = db.prepareStatement("INSERT INTO " + UserManager.userTable + " (uuid, name, ip, level, lastlocation, prefix, suffix, banned, banreason, muted, mutereason) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, DEFAULT, DEFAULT, DEFAULT, DEFAULT);");
			
			registerUser.setString(1, uuid);
			registerUser.setString(2, p.getName());
			registerUser.setString(3, p.getAddress().getAddress().getHostAddress());
			registerUser.setInt(4, getDefaultLevel());
			registerUser.setString(5, lastloc);
			registerUser.setString(6, "");
			registerUser.setString(7, "");
			registerUser.executeUpdate();
			
			synchronizeUser(p.getUniqueId());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes an existing user.
	 * (local + db)
	 * @param id
	 */
	public void deleteUser(UUID uuid) {
		try {
			
			PreparedStatement deleteUser = db.prepareStatement("DELETE FROM " + UserManager.userTable + " WHERE uuid = '" + uuid.toString() + "';");
			deleteUser.executeUpdate();
			
			users.remove(uuid);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteUser(String name) {
		deleteUser(getUser(name).getUUID());
	}
		
	/**
	 * Returns the current amount of users.
	 * @return
	 */
	public int amountOfUsers() {
		return users.size();
	}
	
	/**
	 * Returns a list with all existing (online) users.
	 * @param language
	 * @return
	 */
	public String getUserList(String language) {
		
	    if (Bukkit.getOnlinePlayers().length <= 0) {
	    	return EdgeCore.getLang().getColoredMessage(language, "userlist").replace("[0]", "0").replace("[1]", Bukkit.getMaxPlayers() + "").replace("[2]", "");
	    }
	    
	    StringBuilder sb = new StringBuilder();
	    
	    for (Player p : Bukkit.getOnlinePlayers()) {
	    	if (!exists(p.getUniqueId()))
	    		continue;
	    	
	    	if (sb.length() > 0)
	    		sb.append(ChatColor.RESET + ", ");
	    	
	    	User u = getUser(p.getName());
	    	
	    	sb.append(u.getLevel().getColor() + u.getName());
	    }
	    
	    return EdgeCore.getLang().getColoredMessage(language, "userlist").replace("[0]", Bukkit.getOnlinePlayers().length + "").replace("[1]", Bukkit.getMaxPlayers() + "").replace("[2]", sb.toString());
	}
	
	/**
	 * Checks whether the given user in already in use
	 * @param user
	 * @return true/false
	 */
	public boolean exists(User user) {
		return users.containsValue(user);
	}
	
	/**
	 * Checks whether the given id is already in use. 
	 * @param id
	 * @return true/false
	 */
	public boolean exists(UUID uuid) {
		return users.containsKey(uuid);
	}
	
	
	/**
	 * Checks whether the given name is already in use.
	 * @param name
	 * @return true/false
	 */
	public boolean exists(String name) {
		return getUser(name) != null;
	}
	
	/**
	 * Returns the user connected with the given UUID
	 * @param uuid
	 * @return User
	 */
	public User getUser(UUID uuid) {
		return users.get(uuid);
	}
	
	/**
	 * Return the user registered with the given name.
	 * @param name
	 * @return User
	 */
	public User getUser(String name) {
		
		for (User user : users.values()) {
			if (user.getName().equals(name))
				return user;
		}
		
		return null;
	}
	
	/**
	 * Returns the user registered with the given IP-Token.
	 * @param ip
	 * @return User
	 */
	public User getUserByIP(String ip) {
		
		for (User user : users.values())  {
			if (user.getIP().equals(ip))
				return user;
		}
		
		return null;
	}
	
	/**
	 * Synchronizes all users.
	 */
	public void synchronizeUsers() {
		try {
			
			for (UUID uuid : users.keySet()) {
				synchronizeUser(uuid);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Synchronizes the user connected with the given UUID
	 * @param uuid
	 */
	public void synchronizeUser(UUID uuid) {
		try {
			
			List<Map<String, Object>> results = db.getResults("SELECT * FROM " + UserManager.userTable + " WHERE uuid = '" + uuid.toString() + "';");
			
			for (int i = 0; i < results.size(); i++) {
				
				User user = new User();
				
				for (Map.Entry<String, Object> entry : results.get(i).entrySet()) {
					
					if (entry.getKey().equals("uuid")) {
						user.setUUID(UUID.fromString(entry.getValue().toString()));
						
					} else if(entry.getKey().equals("name")) {
						user.setName(entry.getValue().toString());
						
					} else if(entry.getKey().equals("ip")) {
						user.setIP(entry.getValue().toString());
						
					} else if(entry.getKey().equals("level")) {
						user.setLevel(Level.getInstance(Integer.valueOf(entry.getValue().toString())));
						
					} else if(entry.getKey().equals("lastlocation")) {
						String locString = entry.getValue().toString();
						String[] split = locString.split(",");
						
						user.setLastLocation(new Location(Bukkit.getWorld(split[0]), 
											Double.parseDouble(split[1]), 
											Double.parseDouble(split[2]), 
											Double.parseDouble(split[3]), 
											Float.parseFloat(split[4]), 
											Float.parseFloat(split[5])));
						
					} else if(entry.getKey().equals("prefix")) {
						user.setPrefix(entry.getValue().toString());
						
					} else if(entry.getKey().equals("suffix")) {
						user.setSuffix(entry.getValue().toString());
						
					} else if(entry.getKey().equals("language")) {
						user.setLanguage(entry.getValue().toString());
						
					} else if(entry.getKey().equals("banned")) {
						boolean b = ((Boolean) entry.getValue()).booleanValue();
						user.setBanStatus(b);
						
						if (user.isBanned()) bannedIPs.add(user.getIP());
						
					} else if(entry.getKey().equals("banreason")) {
						user.setBanReason(entry.getValue().toString());
						
					} else if(entry.getKey().equals("muted")) {
						boolean b = ((Boolean) entry.getValue()).booleanValue();
						user.setMuted(b);
						
					}  else if(entry.getKey().equals("mutereason")) {
						user.setMuteReason(entry.getValue().toString());
						
					}					
				}
				
				users.put(user.getUUID(), user);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the map containing all users.
	 * (local)
	 * @return
	 */
	public Map<UUID, User> getUsers() {
		return users;
	}

	/**
	 * Returns the list containing all banned IPs.
	 * (local)
	 * @return
	 */
	public static List<String> getBannedIPs() {
		return bannedIPs;
	}

	/**
	 * Returns the default level of an user.
	 * @return
	 */
	public int getDefaultLevel() {
		return defaultLevel;
	}
	
	/**
	 * Sets the default level of users.
	 * @param defaultLevel
	 */
	public static void setDefaultLevel( int defaultLevel ) {
	    Validate.isTrue(defaultLevel >= 0, "The value must be greater or equal than zero: %s", defaultLevel);
		UserManager.defaultLevel = defaultLevel;
	}
	
	public void notify( User u, String msg ) {
		
		if( u == null || msg == null ) return;
		if( !exists(u) ) return;
		
		Player to = u.getPlayer();
		
		if( to == null ) return;
		
		to.sendMessage( ChatColor.BLUE + msg );
		
	}
	
	public void notifyAll( Level level, String msg ) {		
		if( level == null || msg == null ) return;
		
		for (User user : users.values()) {
			
			Player p = user.getPlayer();
			
			if (p == null) continue;
			
			if (Level.canUse(user, level)) {
				p.sendMessage( msg );
			}
		}
	}
	
}
