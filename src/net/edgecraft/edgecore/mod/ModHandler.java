package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.command.CommandHandler;

public class ModHandler extends CommandHandler {

		private static final ModHandler instance = new ModHandler();
	
		private ModHandler() { 
			
				super.registerCommand( new TicketCommand() );
				super.registerCommand( new TeleportCommand() );
				super.registerCommand( new GiveCommand() );
				super.registerCommand( new PunishCommand() );
				super.registerCommand( new MuteCommand() );
				super.registerCommand( new TimeCommand() );
				super.registerCommand( new GameModeCommand() );
				super.registerCommand( new KillCommand() );
				super.registerCommand( new PermissionCommand() );
				super.registerCommand( new WeatherCommand() );
				super.registerCommand( new KickCommand() );
				super.registerCommand( new BanCommand() );
				super.registerCommand( new DifficultyCommand() );
				super.registerCommand( new MaintenanceCommand() );
				super.registerCommand( new BroadcastCommand() );

		}
		
		public static final ModHandler getInstance() {
			return instance;
		}
}
