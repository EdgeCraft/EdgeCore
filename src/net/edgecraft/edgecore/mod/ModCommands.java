package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.command.CommandHandler;

public class ModCommands extends CommandHandler {
		
		private static final ModCommands instance = new ModCommands();
	
		private ModCommands() {
			
			super.registerCommand( TicketCommand.getInstance() );
			super.registerCommand( TeleportCommand.getInstance() );
			super.registerCommand( HelpCommand.getInstance() );
			super.registerCommand( AliasesCommand.getInstance() );
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
			super.registerCommand( SpawnCommand.getInstance() );
			super.registerCommand( AFKCommand.getInstance() );
			
		}
		
		public static final ModCommands getInstance() {
			return instance;
		}
		
}
