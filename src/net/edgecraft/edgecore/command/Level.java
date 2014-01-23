package net.edgecraft.edgecore.command;

import net.edgecraft.edgecore.user.User;

public enum Level {

	GUEST(0),
	USER(1),
	ARCHITECT(5),
	TEAM(10),
	ADMIN(15);
	
	private int level;
	
	private Level(int level) {
		
		this.level = level;
		
	}
	
	public int value(){
		return this.level;
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
