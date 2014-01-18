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
	
	public static final DatabaseHandler getDatabaseAPI() {
		return databaseAPI;
	}
	
	public static final UserManager getUserAPI() {
		return userAPI;
	}
	
	public static final EdgeCraftSystem getSystemAPI() {
		return systemAPI;
	}
	
	public static final LanguageHandler getLanguageAPI() {
		return languageAPI;
	}
	
	public static final ChatHandler getChatAPI() {
		return chatAPI;
	}
}
