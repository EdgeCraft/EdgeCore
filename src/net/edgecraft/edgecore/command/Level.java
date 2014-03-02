package net.edgecraft.edgecore.command;

import org.bukkit.ChatColor;

import net.edgecraft.edgecore.user.User;

public enum Level {

	GUEST(0, "Gast", ChatColor.GRAY),
	USER(1, "Spieler", ChatColor.WHITE),
	ARCHITECT(5, "Architekt", ChatColor.DARK_GRAY),
	TEAM(10, "Team", ChatColor.BLUE),
	ADMIN(15, "Admin", ChatColor.DARK_RED);
	
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
		
		Level[] levels = { Level.GUEST, Level.USER, Level.ARCHITECT, Level.TEAM, Level.ADMIN };
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
