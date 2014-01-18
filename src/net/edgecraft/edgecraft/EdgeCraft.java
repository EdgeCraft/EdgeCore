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
	
	
	public static String lol = "lol";
	
	
	
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
	
	
	@Override
	public void onDisable() {
	    log.info( EdgeCraft.edgebanner + "Das Plugin wird gestoppt..");
	    log.info( EdgeCraft.edgebanner + "Plugin wurde erfolgreich beendet!");		
	}
	
	@Override
	public void onEnable() {
	    registerData();

	    log.info( EdgeCraft.edgebanner + "Plugin wurde erfolgreich gestartet!");		
	}
	
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
	
	public static EdgeCraft getInstance() {
		return (EdgeCraft) instance;
	}

	public static DatabaseHandler getDB() {
		return EdgeCraft.db;
	}

	public static EdgeCraftSystem getSystem() {
		return EdgeCraft.system;
	}

	public static UserManager getUsers() {
		return EdgeCraft.users;
	}

	public static LanguageHandler getLang() {
		return EdgeCraft.lang;
	}

	public static ChatHandler getChat() {
		return EdgeCraft.chat;
	}

	public static String getCurrency() {
		return EdgeCraft.currency;
	}

	
	
	public static void setCurrency( String currency ) {
		if( currency != null ) {
			EdgeCraft.currency = currency;
		} else return;
	}
}
