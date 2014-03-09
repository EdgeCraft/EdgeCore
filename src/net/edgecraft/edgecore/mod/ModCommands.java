package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.command.AbstractModCommand;
import net.edgecraft.edgecore.command.CommandHandler;

public class ModCommands extends CommandHandler {

		private static final ModCommands instance = new ModCommands();
	
		private ModCommands() { 

		}
		
		public static final ModCommands getInstance() {
			return instance;
		}
		
		public final void registerCommand( AbstractModCommand cmd ) {
			if( cmd == null ) return;
			super.registerCommand( cmd );
		}
}
