package net.edgecraft.edgecraft.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.mysql.DatabaseHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class UserManager {
	
	private static Map<Integer, User> users = new HashMap<>();
	private static List<String> bannedIPs = new ArrayList<>();
	
	private static int defaultLevel;
	
	private final DatabaseHandler db = EdgeCraft.getDB();
	


	public Map<Integer, User> getUsers() {
		return users;
	}

	public static List<String> getBannedIPs() {
		return bannedIPs;
	}

	public int getDefaultLevel() {
		return defaultLevel;
	}

	
	
	public static void setDefaultLevel( int defaultLevel ) {
		if( defaultLevel >= 0 )
			UserManager.defaultLevel = defaultLevel;
	}

	public void registerUser(String name, String ip) {
		try {
			
			if (exists(name)) return;
			
			int id = generateID();
			
			this.db.executeUpdate("INSERT INTO edgecraft_users (id, name, ip, level, language, banned, banreason) VALUES ('" + id + "', '" + name + "', '" + ip + "', '" + defaultLevel + "', DEFAULT, DEFAULT, DEFAULT);");
			synchronizeUser(id);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	public void deleteUser(int id) {
		try {
			
			this.db.executeUpdate("DELETE FROM edgecraft_users WHERE id = '" + id + "';");
			users.remove(id);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int generateID() throws Exception {
		if (amountOfUsers() <= 0) return 1;
		
		return greatestID() + 1;
	}
	
	public int greatestID() throws Exception {

		Map<String, Object> greatestID = this.db.getResults("SELECT * FROM edgecraft_users ORDER BY id DESC LIMIT 1;").get(0);

		if( greatestID.isEmpty() ) return 1;
		
		return (int) greatestID.get("id");
	}
	
	public int amountOfUsers() {
		return users.size();
	}
	
	public String getUserList(String language) {
		
	    if (Bukkit.getOnlinePlayers().length <= 0) {
	    	return EdgeCraft.getLang().getColoredMessage(language, "userlist").replace("[0]", "0").replace("[1]", Bukkit.getMaxPlayers() + "").replace("[2]", "");
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
	    
	    return EdgeCraft.getLang().getColoredMessage(language, "userlist").replace("[0]", Bukkit.getOnlinePlayers().length + "").replace("[1]", Bukkit.getMaxPlayers() + "").replace("[2]", sb.toString());
	}
	
	public boolean exists(int id) {
		return users.containsKey(id);
	}
	
	public boolean exists(String name) {
		return getUser(name) != null;
	}
	
	public User getUser(int id) {
		return users.get(id);
	}
	
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
	
	public void synchronizeUsers() {
		try {
			
			for (int i = 1; i <= greatestID(); i++) {
				synchronizeUser(i);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void synchronizeUser(int id) {
		try {
			
			List<Map<String, Object>> results = db.getResults("SELECT * FROM edgecraft_users WHERE id = '" + id + "';");
			
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
						user.setLevel(Integer.valueOf(entry.getValue().toString()));
						
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
}
