package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.command.AbstractModCommand;
import net.edgecraft.edgecore.command.CommandHandler;

public class ModCommands extends CommandHandler {
	
		private ModCommands() { 

		}
		
		public static final void registerCommand( AbstractModCommand cmd ) {
			if( cmd == null ) return;
			getInstance().registerCommand( cmd );
		}
}
