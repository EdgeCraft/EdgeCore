package net.edgecraft.edgecraft.util;

import java.io.File;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.mysql.DatabaseHandler;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
	
	private EdgeCraft plugin;
	private FileConfiguration config;
	
	public ConfigHandler(EdgeCraft instance) {
		setPlugin( instance );
	}
	
	public void loadConfig() {
		setConfig( getPlugin().getConfig() );
		
	    	getConfig().addDefault( DatabaseHandler.DatabaseHost, DatabaseHandler.unset);
	    	getConfig().addDefault( DatabaseHander.DatabaseUser, DatabaseHandler.unset);
	    	getConfig().addDefault( DatabaseHandler.DatabasePW, DatabaseHandler.unset);
	    	getConfig().addDefault( DatabaseHandler.DatabaseDB, DatabaseHandler.unset);
	   
	   	getConfig().addDefault("Language.Default", "de");

	    	getConfig().addDefault("User.DefaultLevel", Integer.valueOf(0));
	    
		getConfig().addDefault("Economy.Currency", "$");	

	    	getConfig().options().copyDefaults(true);
	    	getPlugin().saveConfig();

	    if (!new File(getPlugin().getDataFolder() + "src/config.yml").exists());
	    	getPlugin().saveDefaultConfig();		
	}
	
	public final void update(EdgeCraft instance) {

		DatabaseHandler.setHost( getConfig().getString(DatabaseHandler.DatabaseHost));
		DatabaseHandler.setUser( getConfig().getString( DatabaseHandler.DatabaseUser ));
		DatabaseHandler.setPW( getConfig().getString( DatabaseHandler.DatabasePW ));
		DatabaseHandler.setDB( getConfig().getString( DatabaseHandler.DatabaseDB ));

		
		LanguageHandler.defaultLanguage = this.config.getString("Language.Default");
		
		UserManager.defaultLevel = this.config.getInt("User.DefaultLevel");
		
		EdgeCraft.currency = this.config.getString("Economy.Currency");
	}

	public FileConfiguration getConfig() {
		return config;
	}

	protected void setConfig( FileConfiguration config ) {
		if( config != null )
			this.config = config;
	}

	protected void setPlugin( EdgeCraft instance ) {
		if( instance != null )
			plugin = instance;
	}
}
