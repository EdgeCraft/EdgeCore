package net.edgecraft.edgecraft;

import java.util.logging.Logger;

import net.edgecraft.edgecraft.chat.ChatHandler;
import net.edgecraft.edgecraft.chat.ManageChatEvent;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.commands.DatabaseCommand;
import net.edgecraft.edgecraft.commands.LanguageCommand;
import net.edgecraft.edgecraft.commands.ListCommand;
import net.edgecraft.edgecraft.commands.SystemCommand;
import net.edgecraft.edgecraft.commands.UserCommand;
import net.edgecraft.edgecraft.events.PlayerConnectionHandler;
import net.edgecraft.edgecraft.events.RegisterUserEvent;
import net.edgecraft.edgecraft.mysql.DatabaseHandler;
import net.edgecraft.edgecraft.util.ConfigHandler;
import net.edgecraft.edgecraft.util.EdgeCraftSystem;
import net.edgecraft.edgecraft.util.LanguageHandler;

import org.bukkit.plugin.java.JavaPlugin;

public class EdgeCraft extends JavaPlugin {
	
	public static final String edgebanner = "[EdgeCraft] ";
	
	public static final Logger log = Logger.getLogger("Minecraft");
	private static EdgeCraft instance;
	
	protected static final DatabaseHandler db = new DatabaseHandler();
	protected static final EdgeCraftSystem system = new EdgeCraftSystem();
	protected static final UserManager users = new UserManager();
	protected static final LanguageHandler lang = new LanguageHandler();
	protected static final ChatHandler chat = new ChatHandler();
	private final ConfigHandler configHandler = new ConfigHandler(this);
	
	private static String currency;
	
	/**
	 * Is used when the plugin is going to shut down
	 */
	@Override
	public void onDisable() {
	    log.info( EdgeCraft.edgebanner + "Das Plugin wird gestoppt..");
	    log.info( EdgeCraft.edgebanner + "Plugin wurde erfolgreich beendet!");		
	}
	
	/**
	 * Is used when the plugin is starting up
	 */
	@Override
	public void onEnable() {
	    registerData();

	    log.info( EdgeCraft.edgebanner + "Plugin wurde erfolgreich gestartet!");		
	}
	
	/**
	 * Is used before onEnable(), e.g. to pre-load needed functions
	 */
	@Override
	public void onLoad() {
	    instance = this;

	    this.configHandler.loadConfig();
	    this.configHandler.update(this);

	    db.loadConnection();

	    users.synchronizeUsers();
	    system.startTimer();
	    chat.enableChat(true);
	}
	
	/**
	 * Registers data the plugin will use
	 */
	private void registerData() {
	    getServer().getPluginManager().registerEvents(new RegisterUserEvent(), this);
	    getServer().getPluginManager().registerEvents(new PlayerConnectionHandler(), this);
	    getServer().getPluginManager().registerEvents(new ManageChatEvent(), this);

	    getCommand("system").setExecutor(new SystemCommand());
	    getCommand("db").setExecutor(new DatabaseCommand());
	    getCommand("user").setExecutor(new UserCommand());
	    getCommand("who").setExecutor(new ListCommand());	
	    getCommand("language").setExecutor(new LanguageCommand());
	}
	
	/**
	 * Returns an instance of this class
	 * 
	 * @return EdgeCraft
	 */
	public static EdgeCraft getInstance() {
		return (EdgeCraft) instance;
	}

	/**
	 * Returns the DatabaseAPI which is instantiated in this class
	 * 
	 * @return DatabaseHandler
	 */
	public static DatabaseHandler getDB() {
		return EdgeCraft.db;
	}
	
	/**
	 * Returns the SystemAPI which is instantiated in this class
	 *  
	 * @return EdgeCraftSystem
	 */
	public static EdgeCraftSystem getSystem() {
		return EdgeCraft.system;
	}
	
	/**
	 * Returns the UserAPI which is instantiated in this class
	 * 
	 * @return UserManager
	 */
	public static UserManager getUsers() {
		return EdgeCraft.users;
	}
	
	/**
	 * Returns the LanguageAPI which is instantiated in this class
	 * 
	 * @return LanguageHandler
	 */
	public static LanguageHandler getLang() {
		return EdgeCraft.lang;
	}
	
	/**
	 * Returns the ChatAPI which is instantiated in this class
	 * 
	 * @return ChatHandler
	 */
	public static ChatHandler getChat() {
		return EdgeCraft.chat;
	}
	
	/**
	 * Returns the currency which is set in the configuration
	 * 
	 * @return String
	 */
	public static String getCurrency() {
		return EdgeCraft.currency;
	}
	
	/**
	 * Sets the currency temporarily for this plugin-session
	 * This variable will be resetted after a restart/reload of this plugin
	 * 
	 * @param currency
	 */
	public static void setCurrency( String currency ) {
		if( currency != null ) {
			EdgeCraft.currency = currency;
		} else return;
	}
}
