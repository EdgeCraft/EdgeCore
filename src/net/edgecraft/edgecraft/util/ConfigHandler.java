package net.edgecraft.edgecraft.util;

import java.io.File;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.classes.UserManager;
import net.edgecraft.edgecraft.mysql.DatabaseHandler;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
	
	private EdgeCraft plugin;
	public FileConfiguration config;
	
	public ConfigHandler(EdgeCraft instance) {
		this.plugin = instance;
	}
	
	public void loadConfig() {
	    this.config = this.plugin.getConfig();

	    this.config.addDefault("Database.Host", "default");
	    this.config.addDefault("Database.User", "default");
	    this.config.addDefault("Database.Password", "default");
	    this.config.addDefault("Database.Database", "default");

	    this.config.addDefault("Language.Default", "de");

	    this.config.addDefault("User.DefaultLevel", Integer.valueOf(0));

	    this.config.options().copyDefaults(true);
	    this.plugin.saveConfig();

	    if (!new File(this.plugin.getDataFolder() + "src/config.yml").exists());
	    this.plugin.saveDefaultConfig();		
	}
	
	public final void update(EdgeCraft instance) {
		DatabaseHandler.host = this.config.getString("Database.Host");
		DatabaseHandler.user = this.config.getString("Database.User");
		DatabaseHandler.pw = this.config.getString("Database.Password");
		DatabaseHandler.db = this.config.getString("Database.Database");
		
		LanguageHandler.defaultLanguage = this.config.getString("Language.Default");
		
		UserManager.defaultLevel = this.config.getInt("User.DefaultLevel");
	}
}
