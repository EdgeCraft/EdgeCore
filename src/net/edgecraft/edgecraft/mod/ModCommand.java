package net.edgecraft.edgecraft.mod;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecraft.command.AbstractCommand;
import net.edgecraft.edgecraft.command.Level;
import net.edgecraft.edgecraft.user.User;

public class ModCommand extends AbstractCommand {
	
	private static final ModHandler commands = ModHandler.getInstance();
	
	public ModCommand() { /* ... */ }
	
	@Override
	public String[] getNames() {
		
		ArrayList<String> names = new ArrayList<>();
		
		for( Map.Entry<String, AbstractCommand> cmd : commands.getCmdList().entrySet() ) {
			for( String alias : cmd.getValue().getNames() ) {
				names.add( alias );
			}
		}
		
		return (String[])names.toArray();
		
	}
	
	@Override
	public Level getLevel() {
		return Level.USER;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		return true; 
	}

	@Override
	public void sendUsage(CommandSender sender) {
		return;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		AbstractCommand cmd = commands.getCommand( args[0] );
		
		if( cmd == null ) {
			sendUsage(player);
			return true;
		}
		
		cmd.run( player, args );
		
		sendUsage(player);
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		
		AbstractCommand cmd = commands.getCommand( args[0] );
		
		if( cmd == null ) return false;
		
		return cmd.sysAccess( sender, args );
	}
}
