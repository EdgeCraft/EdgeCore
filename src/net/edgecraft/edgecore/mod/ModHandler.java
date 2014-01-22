package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.command.CommandHandler;

public class ModHandler extends CommandHandler {

		private static final ModHandler instance = new ModHandler();
	
		private ModHandler() { 
				super.registerCommand( new PermissionCommand() );
				super.registerCommand( new KickCommand() );
				super.registerCommand( new BanCommand() );
				super.registerCommand( new CrashCommand() );
				super.registerCommand( new TimeCommand() );
				super.registerCommand( new TeleportCommand() );
				super.registerCommand( new TicketCommand() );
		}
		
		public static final ModHandler getInstance() {
			return instance;
		}
}
