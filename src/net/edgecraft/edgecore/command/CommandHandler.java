package net.edgecraft.edgecore.command;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

public class CommandHandler {
	
	protected HashMap<String, AbstractCommand> cmdlist = new HashMap<>();
	
	protected static final CommandHandler instance = new CommandHandler();
	
	protected CommandHandler() { /* ... */ }
	
	public static CommandHandler getInstance() {
		return instance;
	}
	
	public boolean registerCommand( AbstractCommand cmd ){
		
		Validate.notNull(cmd);
		Validate.notNull(cmd.getName());
		
		for( Map.Entry<String, AbstractCommand> entry : cmdlist.entrySet() ) {
			for( String alias : entry.getValue().getNames() ) {
				for( String cmdAlias : cmd.getNames() ) {
					if( alias.equalsIgnoreCase(cmdAlias)) {
						return false;
					}
				}
			}
		}
		
		cmdlist.put(cmd.getName(), cmd);
		return true;
		
	}
	
	public Object deleteCommand( AbstractCommand cmd ) {
		
		return cmdlist.remove( cmd.getName() );	
	}
	
	public HashMap<String, AbstractCommand> getCmdList() {
		return cmdlist;
	}
	
	
	public AbstractCommand getCommand( String name ) {
		
		for( Map.Entry<String, AbstractCommand> entry : cmdlist.entrySet() ) {
			AbstractCommand cmd = entry.getValue();
			
			if( cmd.hasAlias(name) ) return cmd;
		}
		
		return null;
	}
	
	public boolean isCommandPresent( String name ){
		
		for( Map.Entry<String, AbstractCommand> entry : cmdlist.entrySet() ) {
			
			for( String alias : entry.getValue().getNames() ) {
				if( alias.equalsIgnoreCase(name)) {
					return true;
				}
			}
			
		}
		return false;
	}
}
