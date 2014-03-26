package net.edgecraft.edgecore.other;

import java.io.File;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.db.DatabaseHandler;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.UserManager;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
	
	private EdgeCore plugin;
	private FileConfiguration config;
	
	protected static final ConfigHandler instance = new ConfigHandler();
	
	protected ConfigHandler() { /* ... */ }
	
	public static final ConfigHandler getInstance( EdgeCore plugin ) {
		instance.setPlugin(plugin);
		return instance;
	}
	
	/**
	 * Loads the config of the EdgeCraft-Instance.
	 * 
	 */
	public void loadConfig() {
			
			// Config-itself.
			setConfig( getPlugin().getConfig() );
		
			// Database
	    	getConfig().addDefault( DatabaseHandler.DatabaseHost, DatabaseHandler.unset);
	    	getConfig().addDefault( DatabaseHandler.DatabaseUser, DatabaseHandler.unset);
	    	getConfig().addDefault( DatabaseHandler.DatabasePW, DatabaseHandler.unset);
	    	getConfig().addDefault( DatabaseHandler.DatabaseDB, DatabaseHandler.unset);
	   
	    	// Language
	    	getConfig().addDefault("Language.Default", "de");

	    	// User
	    	getConfig().addDefault("User.DefaultLevel", Integer.valueOf(0));
	    
	    	// Economy
	    	getConfig().addDefault("Economy.Currency", "$");	

	    	getConfig().options().copyDefaults(true);
	    	getPlugin().saveConfig();

	    if (!new File(getPlugin().getDataFolder() + "src/config.yml").exists());
	    	getPlugin().saveDefaultConfig();		
	}
	
	/**
	 * Updates all local settings using the configuration.
	 * 
	 * @param instance
	 */
	public final void update(EdgeCore instance) {

		DatabaseHandler.setHost( getConfig().getString(DatabaseHandler.DatabaseHost));
		DatabaseHandler.setUser( getConfig().getString( DatabaseHandler.DatabaseUser ));
		DatabaseHandler.setPW( getConfig().getString( DatabaseHandler.DatabasePW ));
		DatabaseHandler.setDB( getConfig().getString( DatabaseHandler.DatabaseDB ));

		
		LanguageHandler.setDefaultLanguage( this.config.getString("Language.Default") );
		
		UserManager.setDefaultLevel( this.config.getInt("User.DefaultLevel") );
		
		EdgeCore.setCurrency( this.config.getString("Economy.Currency") );
	}
	
	/**
	 * Returns the used EdgeCraft-instance.
	 * 
	 * @return
	 */
	private EdgeCore getPlugin() {
		
		return plugin;
	}
	
	/**
	 * Returns the used file-configuration.
	 * 
	 * @return
	 */
	public FileConfiguration getConfig() {
		return config;
	}
	
	/**
	 * Sets the EdgeCraft-instance.
	 * 
	 * @param instance
	 */
	protected void setPlugin( EdgeCore instance ) {
		Validate.notNull(instance);
		plugin = instance;
	}

	/**
	 * Sets the file-configuration.
	 * 
	 * @param config
	 */
	protected void setConfig( FileConfiguration config ) {
	    Validate.notNull(config);
		this.config = config;
	}
	
}
