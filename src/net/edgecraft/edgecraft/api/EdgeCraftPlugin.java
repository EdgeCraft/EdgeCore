package net.edgecraft.edgecraft.api;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.chat.ChatHandler;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.mysql.DatabaseHandler;
import net.edgecraft.edgecraft.util.EdgeCraftSystem;
import net.edgecraft.edgecraft.util.LanguageHandler;

public class EdgeCraftPlugin {
	
	private static final DatabaseHandler databaseAPI = EdgeCraft.getDB();
	private static final UserManager userAPI = EdgeCraft.getUsers();
	private static final EdgeCraftSystem systemAPI = EdgeCraft.getSystem();
	private static final LanguageHandler languageAPI = EdgeCraft.getLang();
	private static final ChatHandler chatAPI = EdgeCraft.getChat();
	
	/**
	 * Returns the DatabaseAPI
	 * 
	 * @return DatabaseHandler
	 */
	public static final DatabaseHandler getDatabaseAPI() {
		return databaseAPI;
	}
	
	/**
	 * Returns the UserAPI
	 * 
	 * @return UserManager
	 */
	public static final UserManager getUserAPI() {
		return userAPI;
	}
	
	/**
	 * Returns the SystemAPI
	 * 
	 * @return EdgeCraftSystem
	 */
	public static final EdgeCraftSystem getSystemAPI() {
		return systemAPI;
	}
	
	/**
	 * Returns the LanguageAPI
	 * 
	 * @return LanguageHandler
	 */
	public static final LanguageHandler getLanguageAPI() {
		return languageAPI;
	}
	
	/**
	 * Returns the ChatAPI
	 * 
	 * @return ChatHandler
	 */
	public static final ChatHandler getChatAPI() {
		return chatAPI;
	}
}
