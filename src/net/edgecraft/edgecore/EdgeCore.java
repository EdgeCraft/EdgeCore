package net.edgecraft.edgecore;

import java.util.logging.Logger;

import net.edgecraft.edgecore.chat.ChatHandler;
import net.edgecraft.edgecore.chat.ManageChatListener;
import net.edgecraft.edgecore.command.CommandsCollectionCommand;
import net.edgecraft.edgecore.command.CommandHandler;
import net.edgecraft.edgecore.command.CommandListener;
import net.edgecraft.edgecore.db.DatabaseCommand;
import net.edgecraft.edgecore.db.DatabaseHandler;
import net.edgecraft.edgecore.lang.LanguageCommand;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.mod.ModCommands;
import net.edgecraft.edgecore.mod.TicketManager;
import net.edgecraft.edgecore.other.ConfigHandler;
import net.edgecraft.edgecore.other.ListCommand;
import net.edgecraft.edgecore.other.PlayerConnectionListener;
import net.edgecraft.edgecore.system.EdgeCraftSystem;
import net.edgecraft.edgecore.system.SystemCommand;
import net.edgecraft.edgecore.user.RegisterUserListener;
import net.edgecraft.edgecore.user.UserCommand;
import net.edgecraft.edgecore.user.UserManager;
import net.edgecraft.edgecore.user.UserSynchronizationTask;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class EdgeCore extends JavaPlugin {
	
	public static final String edgebanner = "[EdgeCore] ";
	
	public static final String usageColor = ChatColor.RED.toString();
	public static final String sysColor = ChatColor.GREEN.toString();
	public static final String errorColor = ChatColor.RED.toString();
	
	public static final Logger log = Logger.getLogger("EdgeCore");
	private static EdgeCore instance;
	
	protected static final DatabaseHandler db = DatabaseHandler.getInstance();
	protected static final EdgeCraftSystem system = EdgeCraftSystem.getInstance();
	protected static final UserManager users = UserManager.getInstance();
	protected static final LanguageHandler lang = LanguageHandler.getInstance();
	protected static final ChatHandler chat = ChatHandler.getInstance();
	protected static final CommandHandler commands = CommandHandler.getInstance();
	protected static final TicketManager tickets = TicketManager.getInstance();
	
	private final ConfigHandler config = ConfigHandler.getInstance(this);
	
	private static String currency;
	private static boolean maintenance;
	
	/**
	 * Is used when the plugin is going to shut down
	 */
	@Override
	public void onDisable() {
		users.synchronizeUsers();
	    log.info( EdgeCore.edgebanner + "Plugin wurde erfolgreich beendet!");		
	}
	
	/**
	 * Is used when the plugin is starting up
	 */
	@Override
	public void onEnable() {
	    registerData();

	    log.info( EdgeCore.edgebanner + "Plugin wurde erfolgreich gestartet!");		
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
	    getServer().getPluginManager().registerEvents( new RegisterUserListener(), this );
	    getServer().getPluginManager().registerEvents( new PlayerConnectionListener(), this );
	    getServer().getPluginManager().registerEvents( new ManageChatListener(), this );
	    getServer().getPluginManager().registerEvents( new CommandListener(), this );

	    commands.registerCommand( SystemCommand.getInstance() );
	    commands.registerCommand( DatabaseCommand.getInstance() );
	    commands.registerCommand( UserCommand.getInstance() );
	    commands.registerCommand( ListCommand.getInstance() );
	    commands.registerCommand( LanguageCommand.getInstance() );
	    commands.registerCommand( new CommandsCollectionCommand( ModCommands.getInstance() ) );
	    
	    @SuppressWarnings("unused") BukkitTask userTask = new UserSynchronizationTask().runTaskTimer(this, 0, 20L * 60 * 10);
	}
	
	/**
	 * Returns an instance of this class
	 * 
	 * @return EdgeCraft
	 */
	public static EdgeCore getInstance() {
		return (EdgeCore) instance;
	}

	/**
	 * Returns the DatabaseAPI which is instantiated in this class
	 * 
	 * @return DatabaseHandler
	 */
	public static DatabaseHandler getDB() {
		return EdgeCore.db;
	}
	
	/**
	 * Returns the SystemAPI which is instantiated in this class
	 *  
	 * @return EdgeCraftSystem
	 */
	public static EdgeCraftSystem getSystem() {
		return EdgeCore.system;
	}
	
	/**
	 * Returns the UserAPI which is instantiated in this class
	 * 
	 * @return UserManager
	 */
	public static UserManager getUsers() {
		return EdgeCore.users;
	}
	
	/**
	 * Returns the LanguageAPI which is instantiated in this class
	 * 
	 * @return LanguageHandler
	 */
	public static LanguageHandler getLang() {
		return EdgeCore.lang;
	}
	
	/**
	 * Returns the ChatAPI which is instantiated in this class
	 * 
	 * @return ChatHandler
	 */
	public static ChatHandler getChat() {
		return EdgeCore.chat;
	}
	
	/**
	 * Returns the CommandAPI which is instantiated in this class
	 * 
	 * @return CommandHandler
	 */
	public static CommandHandler getCommands() {
		return EdgeCore.commands;
	}
	
	/**
	 * Returns the TicketAPI which is instantiated in this class
	 * 
	 * @return
	 */
	public static TicketManager getTickets() {
		return EdgeCore.tickets;
	}
	
	/**
	 * Returns the currency which is set in the configuration
	 * 
	 * @return String
	 */
	public static String getCurrency() {
		return EdgeCore.currency;
	}
	
	/**
	 * Sets the currency temporarily for this plugin-session
	 * This variable will be resetted after a restart/reload of this plugin
	 * 
	 * @param currency
	 */
	public static void setCurrency( String currency ) {
		if( currency != null ) {
			EdgeCore.currency = currency;
		} else return;
	}
	
	/**
	 * Checks if maintenance is enabled
	 * @return true/false
	 */
	public static boolean isMaintenance() {
		return EdgeCore.maintenance;
	}
	
	/**
	 * Toggles maintenance status
	 * @param var
	 */
	public void setMaintenance(boolean var) {
		EdgeCore.maintenance = var;
	}
}
