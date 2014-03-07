package net.edgecraft.edgecore.command;

import org.bukkit.ChatColor;

import net.edgecraft.edgecore.user.User;

public enum Level {
	
	/*
	 * TODO:
	 * 
	 * Sh!t System dynamisch machen
	 * - Gruppen erstellen
	 * - Gruppen löschen
	 * - Gruppen bearbeiten
	 * - Gruppen für User managen
	 * 
	 */
	GUEST(0, "[Gast]", ChatColor.GRAY),
	USER(1, "", ChatColor.WHITE),
	SUPPORTER(5, "[Supporter]", ChatColor.GOLD),
	ARCHITECT(7, "[Architekt]", ChatColor.DARK_GREEN),
	MODERATOR(10, "[Moderator]", ChatColor.BLUE),
	DEVELOPER(15, "[Entwickler]", ChatColor.DARK_AQUA),
	ADMIN(20, "[Admin]", ChatColor.DARK_RED);
	
	private int level;
	private String chatName;
	private ChatColor color;
	
	private Level(int level, String chatName, ChatColor color) {
		
		this.level = level;
		this.chatName = chatName;
		this.color = color;
		
	}
	
	public int value(){
		return this.level;
	}
	
	public String getName() {
		return this.chatName;
	}
	
	public ChatColor getColor() {
		return this.color;
	}
	
	public static Level[] getLevels() {
		
		Level[] levels = { Level.GUEST, Level.USER, Level.SUPPORTER, Level.ARCHITECT, Level.MODERATOR, Level.DEVELOPER, Level.ADMIN };
		return levels;
	}
	
	public static Level getInstance( int lvl ) {
		
		Level[] lvls = getLevels();
		
		for( int i = 0; i < lvls.length; i++ ) {
			if( lvl == lvls[i].value() ) {
				return lvls[i];
			}
		}
		
		return null;
	}
	
	public static boolean canUse(User u, Level level){
		return level.value() <= u.getLevel().value() ;
	}
	
}
