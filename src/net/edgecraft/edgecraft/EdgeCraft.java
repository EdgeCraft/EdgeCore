package net.edgecraft.edgecraft;

import java.util.logging.Logger;

import net.edgecraft.edgecraft.chat.ChatHandler;
import net.edgecraft.edgecraft.chat.ManageChatEvent;
import net.edgecraft.edgecraft.command.CommandHandler;
import net.edgecraft.edgecraft.command.CommandListener;
import net.edgecraft.edgecraft.db.DatabaseCommand;
import net.edgecraft.edgecraft.db.DatabaseHandler;
import net.edgecraft.edgecraft.lang.LanguageCommand;
import net.edgecraft.edgecraft.lang.LanguageHandler;
import net.edgecraft.edgecraft.other.ConfigHandler;
import net.edgecraft.edgecraft.other.ListCommand;
import net.edgecraft.edgecraft.other.PlayerConnectionHandler;
import net.edgecraft.edgecraft.system.EdgeCraftSystem;
import net.edgecraft.edgecraft.system.SystemCommand;
import net.edgecraft.edgecraft.user.RegisterUserEvent;
import net.edgecraft.edgecraft.user.UserCommand;
import net.edgecraft.edgecraft.user.UserManager;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class EdgeCraft extends JavaPlugin {
	
	public static final String edgebanner = "[EdgeCraft] ";
	
	public static final String usageColor = ChatColor.RED.toString();
	public static final String sysColor = ChatColor.GREEN.toString();
	public static final String errorColor = ChatColor.RED.toString();
	
	public static final Logger log = Logger.getLogger("EdgeCraft");
	private static EdgeCraft instance;
	
	protected static final DatabaseHandler db = DatabaseHandler.getInstance();
	protected static final EdgeCraftSystem system = EdgeCraftSystem.getInstance();
	protected static final UserManager users = UserManager.getInstance();
	protected static final LanguageHandler lang = LanguageHandler.getInstance();
	protected static final ChatHandler chat = ChatHandler.getInstance();
	protected static final CommandHandler commands = CommandHandler.getInstance();
	
	private final ConfigHandler config = ConfigHandler.getInstance(this);
	
	private static String currency;
	
	private EdgeCraft() { }
	
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

	    this.config.loadConfig();
	    this.config.update(this);

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
	    getServer().getPluginManager().registerEvents( CommandListener.getInstance(), this);

	    commands.registerCommand("system", new SystemCommand() );
	    commands.registerCommand("db", new DatabaseCommand() );
	    commands.registerCommand("user", new UserCommand() );
	    commands.registerCommand("who", new ListCommand() );
	    commands.registerCommand("language", new LanguageCommand() );
//	    
	    
	    
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
	 * Returns the CommandAPI which is instantiated in this class
	 * 
	 * @return CommandHandler
	 */
	public static CommandHandler getCommands() {
		return EdgeCraft.commands;
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
