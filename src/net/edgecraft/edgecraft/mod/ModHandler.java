package net.edgecraft.edgecraft.mod;

import net.edgecraft.edgecraft.command.CommandHandler;

public class ModHandler extends CommandHandler {

		private static final ModHandler instance = new ModHandler();
	
		private ModHandler() { 
				super.registerCommand( new KickCommand() );
				super.registerCommand( new BanCommand() );
				super.registerCommand( new TicketCommand() );
		}
		
		public static final ModHandler getInstance() {
			return instance;
		}
}
