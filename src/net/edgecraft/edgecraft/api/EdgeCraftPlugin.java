package net.edgecraft.edgecraft.api;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.chat.ChatHandler;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.mysql.DatabaseHandler;
import net.edgecraft.edgecraft.util.EdgeCraftSystem;
import net.edgecraft.edgecraft.util.LanguageHandler;

public class EdgeCraftPlugin {
	
	private static final DatabaseHandler databaseAPI = EdgeCraft.db;
	private static final UserManager userAPI = EdgeCraft.manager;
	private static final EdgeCraftSystem systemAPI = EdgeCraft.system;
	private static final LanguageHandler languageAPI = EdgeCraft.lang;
	private static final ChatHandler chatAPI = EdgeCraft.chat;
	
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
