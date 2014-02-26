package net.edgecraft.edgecore.user;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.db.DatabaseHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class UserManager {
	
	public final static String userTable = "edgecore_users";
	
	private static Map<Integer, User> users = new LinkedHashMap<>();
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
	public void registerUser(String name, String ip) {
		try {
			
			if (exists(name)) return;
			
			int id = generateID();
			
			PreparedStatement registerUser = db.prepareUpdate("INSERT INTO " + UserManager.userTable + " (id, name, ip, level, language, banned, banreason) VALUES (?, ?, ?, ?, DEFAULT, DEFAULT, DEFAULT);");
			registerUser.setInt(1, id);
			registerUser.setString(2, name);
			registerUser.setString(3, ip);
			registerUser.setInt(4, getDefaultLevel());
			registerUser.executeUpdate();
			
			synchronizeUser(id);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes an existing user.
	 * (local + db)
	 * @param id
	 */
	public void deleteUser(int id) {
		try {
			
			PreparedStatement deleteUser = db.prepareUpdate("DELETE FROM " + UserManager.userTable + " WHERE id = '" + id + "';");
			deleteUser.executeUpdate();
			
			users.remove(id);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates a possible user-id.
	 * @return
	 * @throws Exception
	 */
	public int generateID() throws Exception {
		if (amountOfUsers() <= 0) return 1;
		
		return greatestID() + 1;
	}
	
	/**
	 * Returns the greatest user-id.
	 * @return
	 * @throws Exception
	 */
	public int greatestID() throws Exception {
		List<Map<String, Object>> tempVar = db.getResults("SELECT IFNULL(MAX(id), 0) FROM " + UserManager.userTable);
		if (tempVar.get(0).get("id") == null) return 1;

		return (int) tempVar.get(0).get("id");
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
	    
	    for (User user : users.values()) {
	    	if (sb.length() > 0) {
	    		sb.append(ChatColor.WHITE);
	    		sb.append(", ");
	    	}
	    	
	    	if ((user != null) && (Bukkit.getPlayerExact(user.getName()).isOnline())) {
	    		sb.append(Bukkit.getPlayerExact(user.getName()).isOp() ? ChatColor.RED : ChatColor.WHITE);
	    		sb.append(user.getName());
	    	}
	    }
	    
	    return EdgeCore.getLang().getColoredMessage(language, "userlist").replace("[0]", Bukkit.getOnlinePlayers().length + "").replace("[1]", Bukkit.getMaxPlayers() + "").replace("[2]", sb.toString());
	}
	
	/**
	 * Checks whether the given id is already in use. 
	 * @param id
	 * @return true/false
	 */
	public boolean exists(int id) {
		return users.containsKey(id);
	}
	
	
	/**
	 * Checks whether the given name is already in use.
	 * @param name
	 * @return
	 */
	public boolean exists(String name) {
		return getUser(name) != null;
	}
	
	/**
	 * Returns the user registered with the given id.
	 * @param id
	 * @return
	 */
	public User getUser(int id) {
		return users.get(id);
	}
	
	/**
	 * Return the user registered with the given name.
	 * @param name
	 * @return
	 */
	public User getUser(String name) {
		
		int id = 0;
		
		for (Map.Entry<Integer, User> entry : users.entrySet()) {
			User searchFor = entry.getValue();
			
			if (searchFor.getName().equals(name)) {
				id = searchFor.getID();
				break;
			}
		}
		
		return users.get(id);
	}
	
	/**
	 * Returns the user registered with the given IP-Token.
	 * @param ip
	 * @return
	 */
	public User getUserByIP(String ip) {
		
		int id = 0;
		
		for (Map.Entry<Integer, User> entry : users.entrySet()) {
			User searchFor = entry.getValue();
			
			if (searchFor.getIP().equals(ip)) {
				id = searchFor.getID();
				break;
			}
		}
		
		return users.get(id);
	}
	
	/**
	 * Synchronizes all users.
	 */
	public void synchronizeUsers() {
		try {
			
			for (int i = 1; i <= greatestID(); i++) {
				synchronizeUser(i);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Synchronizes the user given through his id.
	 * ( db --> local )
	 * @param id
	 */
	public void synchronizeUser(int id) {
		try {
			
			List<Map<String, Object>> results = db.getResults("SELECT * FROM " + UserManager.userTable + " WHERE id = '" + id + "';");
			
			for (int i = 0; i < results.size(); i++) {
				
				User user = new User();
				
				for (Map.Entry<String, Object> entry : results.get(i).entrySet()) {
					
					if (entry.getKey().equals("id")) {
						user.setID(Integer.valueOf(entry.getValue().toString()));
						
					} else if(entry.getKey().equals("name")) {
						user.setName(entry.getValue().toString());
						
					} else if(entry.getKey().equals("ip")) {
						user.setIP(entry.getValue().toString());
						
					} else if(entry.getKey().equals("level")) {						
						user.setLevel( Level.getInstance(Integer.valueOf(entry.getValue().toString())) );
					} else if(entry.getKey().equals("language")) {
						user.setLanguage(entry.getValue().toString());
						
					} else if(entry.getKey().equals("banned")) {
						boolean b = ((Boolean) entry.getValue()).booleanValue();
						user.setBanStatus(b);
						
						if (user.isBanned()) bannedIPs.add(user.getIP());
						
					} else if(entry.getKey().equals("banreason")) {
						user.setBanReason(entry.getValue().toString());
					}
				}
				
				users.put(user.getID(), user);
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
	public Map<Integer, User> getUsers() {
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
		if( defaultLevel >= 0 )
			UserManager.defaultLevel = defaultLevel;
	}
	
	public boolean exists( User u ) {
		return users.containsValue( u );
	}
	
	public void notify( User u, String msg ) {
		
		if( u == null || msg == null ) return;
		if( !exists(u) ) return;
		
		u.getPlayer().sendMessage( msg );
		
	}
	
	public void notifyAll( Level level, String msg ) {
		
		if( level == null || msg == null ) return;
		
		for( Map.Entry<Integer, User> entry : users.entrySet() ) {
			
			User cur = entry.getValue();
			
			if( Level.canUse( cur, level) ) {
				cur.getPlayer().sendMessage( msg );
			}
			
		}
		
	}
	
}
