package net.edgecraft.edgecore.command;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.user.User;

public class CommandContainer extends AbstractCommand {
	
	private CommandHandler commands;

	public CommandContainer( CommandHandler handler ) {
		setCommands( handler );
	}
	
	private void setCommands( CommandHandler handler ) {
		
		if( handler == null ) return;
		
		commands = handler;
	}
	
	@Override
	public String[] getNames() {
		
		ArrayList<String> names = new ArrayList<>();
		
		for( Map.Entry<String, AbstractCommand> cmd : commands.getCmdList().entrySet() ) {
			for( String alias : cmd.getValue().getNames() ) {
				names.add( alias );
			}
		}
		
		String[] namesArray = names.toArray(new String[names.size()]);
		
		return namesArray;
		
	}
	
	public CommandHandler getCommands() {
		return commands;
	}
	
	public AbstractCommand getCommand( String name ) {
		return commands.getCommand( name );
	}
	
	@Override
	public Level getLevel() {
		return Level.USER;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		return( args.length >= 1 );
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		return;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		AbstractCommand cmd = commands.getCommand( args[0] );
		
		if( cmd == null ) {
			EdgeCore.log.info("Command " + args[0] + " not found!");
			sendUsage(player);
			return true;
		}
		
		cmd.run( player, args );
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		
		AbstractCommand cmd = commands.getCommand( args[0] );
		
		if( cmd == null ) return false;
		
		return cmd.sysAccess( sender, args );
	}
}
