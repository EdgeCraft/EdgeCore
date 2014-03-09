package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.command.AbstractModCommand;
import net.edgecraft.edgecore.command.CommandHandler;

public class ModCommands extends CommandHandler {
	
		private static final ModCommands instance = new ModCommands();
	
		private ModCommands() { 

		}
		
		public final void registerCommand( AbstractModCommand cmd ) {
			if( cmd == null ) return;
			instance.registerCommand( cmd );
		}
		
		public static ModCommands getInstance() {
			return ModCommands.instance;
		}
}
