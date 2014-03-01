package net.edgecraft.edgecore.mod;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class ModCommand extends AbstractCommand {
	
	private static final ModHandler commands = ModHandler.getInstance();

	
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
	
	@Override
	public Level getLevel() {
		return Level.USER;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		return( args.length >= 1 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		AbstractCommand cmd = commands.getCommand( args[0] );
		
		if( cmd == null ) {
			EdgeCore.log.info("Command " + args[0] + " not found!");
			sendUsage(player);
			return true;
		}
		
		if( args.length == 2 ) {
			if( args[1].equalsIgnoreCase( "help" )) {
				cmd.sendUsage( player );
				return true;
			} else if( args[1].equalsIgnoreCase( "aliases" ) ) {
				StringBuilder sb = new StringBuilder();
				for( String alias : getNames() ) {
					sb.append( alias + ", ");
				}
				sb.deleteCharAt(sb.length());
				player.sendMessage( sb.toString() );
				return true;
			}
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
