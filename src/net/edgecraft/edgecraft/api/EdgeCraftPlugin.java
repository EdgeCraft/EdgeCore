package net.edgecraft.edgecraft.api;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.chat.ChatHandler;
import net.edgecraft.edgecraft.command.CommandHandler;
import net.edgecraft.edgecraft.db.DatabaseHandler;
import net.edgecraft.edgecraft.lang.LanguageHandler;
import net.edgecraft.edgecraft.system.EdgeCraftSystem;
import net.edgecraft.edgecraft.user.UserManager;

public class EdgeCraftPlugin {
	
	private static final DatabaseHandler databaseAPI = EdgeCraft.getDB();
	private static final UserManager userAPI = EdgeCraft.getUsers();
	private static final EdgeCraftSystem systemAPI = EdgeCraft.getSystem();
	private static final LanguageHandler languageAPI = EdgeCraft.getLang();
	private static final ChatHandler chatAPI = EdgeCraft.getChat();
	private static final CommandHandler commandsAPI = EdgeCraft.getCommands();
	
	/**
	 * Returns the DatabaseAPI
	 * 
	 * @return DatabaseHandler
	 */
	public static final DatabaseHandler databaseAPI() {
		return databaseAPI;
	}
	
	/**
	 * Returns the UserAPI
	 * 
	 * @return UserManager
	 */
	public static final UserManager userAPI() {
		return userAPI;
	}
	
	/**
	 * Returns the SystemAPI
	 * 
	 * @return EdgeCraftSystem
	 */
	public static final EdgeCraftSystem systemAPI() {
		return systemAPI;
	}
	
	/**
	 * Returns the LanguageAPI
	 * 
	 * @return LanguageHandler
	 */
	public static final LanguageHandler languageAPI() {
		return languageAPI;
	}
	
	/**
	 * Returns the ChatAPI
	 * 
	 * @return ChatHandler
	 */
	public static final ChatHandler chatAPI() {
		return chatAPI;
	}
	
	/**
	 * Returns the CommandsAPI
	 * 
	 * @return CommandHandler
	 */
	public static final CommandHandler commandsAPI() {
		return commandsAPI;
	}
}
