package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.command.AbstractModCommand;
import net.edgecraft.edgecore.command.CommandHandler;

public class ModCommands {
	
		private final static CommandHandler cmds = new CommandHandler();
		
		private ModCommands() {}
		
		public static final CommandHandler getCommands() {
			return cmds;
		}
		
		public final static void registerCommand( AbstractModCommand cmd ) {
			cmds.registerCommand( cmd );
		}
		
		
}
