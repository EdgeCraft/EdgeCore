package net.edgecraft.edgecraft.command;

import java.util.HashMap;

public final class CommandHandler {
	
	private HashMap<String, AbstractCommand> cmdlist = new HashMap<>();
	private static CommandHandler instance = new CommandHandler();
	
	private CommandHandler(){ /* ... */ }
	
	public void registerCommand(String name, AbstractCommand cmd){
		
		if( name != null && name.trim().length() > 0 && cmd != null ) {
			if( !cmdlist.containsKey(name) ) {
				cmdlist.put( name, cmd );
			}
		}
	}
	
	public void deleteCommand( String name ) {
		
		cmdlist.remove( name );	
	}
	
	public HashMap<String, AbstractCommand> getCmdList() {
		return cmdlist;
	}
	
	public AbstractCommand getHandler( String name ){
		return cmdlist.get(name);
	}
	
	public boolean isCommandPresent( String name ){
		return cmdlist.containsKey(name);
	}
	
	public static CommandHandler getInstance() {
		return instance;
	}
	
}
