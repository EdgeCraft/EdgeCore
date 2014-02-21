package net.edgecraft.edgecore.command;

import java.util.ArrayList;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandListener implements Listener {
	
	private final CommandHandler commands = EdgeCoreAPI.commandsAPI();
	private final LanguageHandler lang = EdgeCoreAPI.languageAPI();
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void consoleCmd( ServerCommandEvent e ){
		
		String rawCommand[] = e.getCommand().split(" ");
		rawCommand[0] = rawCommand[0].substring(1);
		
		ArrayList<String> command = new ArrayList<>();
		
		for( int i = 0; i < rawCommand.length; i++ ) {
			if( rawCommand[i] != "" ) {
				command.add(rawCommand[i]);
			}
		}
		
		String[] commandArray = command.toArray(new String[command.size()]);
		CommandEvent ee = new CommandEvent( e.getSender(), commandArray );
		
		if( commands.isCommandPresent( command.get(0) ) ){
			Bukkit.getPluginManager().callEvent(ee);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void cmd( PlayerCommandPreprocessEvent e ){
		
		String rawCommand[] = e.getMessage().split(" ");
		rawCommand[0] = rawCommand[0].substring(1);
		
		ArrayList<String> command = new ArrayList<>();
		
		for( int i = 0; i < rawCommand.length; i++ ) {
			if( rawCommand[i] != "" ) {
				command.add(rawCommand[i]);
			}
		}
		
		String[] commandArray = command.toArray(new String[command.size()]);
		CommandEvent ee = new CommandEvent( e.getPlayer(), commandArray );
		
		if( commands.isCommandPresent( command.get(0) ) ){
			Bukkit.getPluginManager().callEvent(ee);
		
			if(	ee.isCancelled() ){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void handleCmd( CommandEvent e ){
		
		try{
			
			String[] args = e.getCommand();
			
			AbstractCommand cmd = commands.getCommand( args[0] );
			
			//PERMISSION
			User u = EdgeCoreAPI.userAPI().getUser( e.getSender().getName() );
			
			if(u == null){
				// Console
			}
			else if(!(u.getLevel().value() >= cmd.getLevel().value())) throw new Exception();
			
			boolean run = cmd.run(e.getSender(), args);
			
			if(!run){
				
				EdgeCore.log.info( EdgeCore.errorColor + "Running " + args[0] + " failed!" );
				
			}
			
			//TODO: remove instanceof if possible
			if(e.getSender() instanceof Player) EdgeCore.log.info( u.getName() + " executed the command /" + e.getCommand()[0]);

		} catch(Exception exc){
			
			e.getSender().sendMessage( lang.getColoredMessage( LanguageHandler.getDefaultLanguage(), "nopermission" ) );
			EdgeCore.log.severe("Error while performing command!");
			exc.printStackTrace();
			
		}
	
	}
}
