package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.command.CommandHandler;

public class ModHandler extends CommandHandler {

		private static final ModHandler instance = new ModHandler();
	
		private ModHandler() { 
			
				super.registerCommand( TicketCommand.getInstance() );
				super.registerCommand( TeleportCommand.getInstance() );
				super.registerCommand( GiveCommand.getInstance() );
				super.registerCommand( PunishCommand.getInstance() );
				super.registerCommand( MuteCommand.getInstance() );
				super.registerCommand( TimeCommand.getInstance() );
				super.registerCommand( GameModeCommand.getInstance() );
				super.registerCommand( KillCommand.getInstance() );
				super.registerCommand( PermissionCommand.getInstance() );
				super.registerCommand( WeatherCommand.getInstance() );
				super.registerCommand( KickCommand.getInstance() );
				super.registerCommand( BanCommand.getInstance() );
				super.registerCommand( DifficultyCommand.getInstance() );
				super.registerCommand( MaintenanceCommand.getInstance() );
				super.registerCommand( BroadcastCommand.getInstance() );

		}
		
		public static final ModHandler getInstance() {
			return instance;
		}
}
