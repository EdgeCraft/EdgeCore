package net.edgecraft.edgecore;

import net.edgecraft.edgecore.chat.ChatHandler;
import net.edgecraft.edgecore.command.CommandHandler;
import net.edgecraft.edgecore.db.DatabaseHandler;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.mod.TicketManager;
import net.edgecraft.edgecore.system.EdgeCraftSystem;
import net.edgecraft.edgecore.user.UserManager;

public final class EdgeCoreAPI {
	
	private static final DatabaseHandler databaseAPI = EdgeCore.getDB();
	private static final UserManager userAPI = EdgeCore.getUsers();
	private static final EdgeCraftSystem systemAPI = EdgeCore.getSystem();
	private static final LanguageHandler languageAPI = EdgeCore.getLang();
	private static final ChatHandler chatAPI = EdgeCore.getChat();
	private static final CommandHandler commandsAPI = EdgeCore.getCommands();
	private static final TicketManager ticketAPI = EdgeCore.getTickets();
	
	private EdgeCoreAPI() { /* ... */ }
	
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
	
	/**
	 * Returns the TicketAPI
	 * 
	 * @return
	 */
	public static final TicketManager ticketAPI() {
		return ticketAPI;
	}
}
