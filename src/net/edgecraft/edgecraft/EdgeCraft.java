package net.edgecraft.edgecraft;

import java.util.logging.Logger;

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
	
	public static final Logger log = Logger.getLogger("Minecraft");
	private static EdgeCraft instance;
	
	public static final DatabaseHandler db = new DatabaseHandler();
	public static final EdgeCraftSystem system = new EdgeCraftSystem();
	public static final UserManager manager = new UserManager();
	public static final LanguageHandler lang = new LanguageHandler();
	private final ConfigHandler configHandler = new ConfigHandler(this);
	  
	@Override
	public void onDisable() {
	    log.info("[EdgeCraft] Das Plugin wird gestoppt..");
	    log.info("[EdgeCraft] Plugin wurde erfolgreich beendet!");		
	}
	
	@Override
	public void onEnable() {
	    registerData();

	    log.info("[EdgeCraft] Plugin wurde erfolgreich gestartet!");		
	}
	
	@Override
	public void onLoad() {
	    instance = this;

	    this.configHandler.loadConfig();
	    this.configHandler.update(this);

	    db.loadConnection();

	    manager.synchronizeUsers();
	    system.startTimer();		
	}
	
	private void registerData() {
	    getServer().getPluginManager().registerEvents(new RegisterUserEvent(), this);
	    getServer().getPluginManager().registerEvents(new PlayerConnectionHandler(), this);

	    getCommand("system").setExecutor(new SystemCommand());
	    getCommand("db").setExecutor(new DatabaseCommand());
	    getCommand("user").setExecutor(new UserCommand());
	    getCommand("who").setExecutor(new ListCommand());	
	    getCommand("language").setExecutor(new LanguageCommand());
	}
	
	public static EdgeCraft getInstance() {
		return (EdgeCraft) instance;
	}
}
