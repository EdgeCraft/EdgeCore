package net.edgecraft.edgecraft.command;

import java.util.ArrayList;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.EdgeCraftAPI;
import net.edgecraft.edgecraft.lang.LanguageHandler;
import net.edgecraft.edgecraft.user.User;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;


public class CommandListener implements Listener {

	private static CommandListener instance = new CommandListener();
	
	private final CommandHandler commands = EdgeCraftAPI.commandsAPI();
	private final LanguageHandler lang = EdgeCraftAPI.languageAPI();
	
	private CommandListener() { /* ... */ }
	
	public static final CommandListener getInstance() {
		return instance;
	}
	
	
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
		
		CommandEvent ee = new CommandEvent( e.getSender(), (String[]) command.toArray() );
		
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
		
		CommandEvent ee = new CommandEvent( e.getPlayer(), (String[]) command.toArray() );
		
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
			User u = EdgeCraftAPI.userAPI().getUser( e.getSender().getName() );
			
			boolean run = cmd.run(e.getSender(), args);
			
			if(!run){
				
				EdgeCraft.log.info( EdgeCraft.errorColor + "Running " + args[0] + " failed!" );
				
			}
			
			//TODO: remove instanceof if possible
			if(e.getSender() instanceof Player) EdgeCraft.log.info( u.getName() + " executed the command /" + e.getCommand()[0]);

		} catch(Exception exc){
			
			e.getSender().sendMessage( lang.getColoredMessage( LanguageHandler.getDefaultLanguage(), "nopermission" ) );
			EdgeCraft.log.severe("Error while performing command!");
			exc.printStackTrace();
			
		}
	
	}
}
