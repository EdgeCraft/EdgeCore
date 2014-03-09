package net.edgecraft.edgecore.mod;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractModCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class PunishCommand extends AbstractModCommand {
	
	private static final PunishCommand instance = new PunishCommand();
	
	private PunishCommand() { super(); }
	
	public static final PunishCommand getInstance() {
		return instance;
	} 
	
	@Override
	public String[] getNames() {
		return new String[]{ "punish" };
	}

	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 || args.length == 3);
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage( EdgeCore.usageColor + "/punish <player>" );
		sender.sendMessage( EdgeCore.usageColor + "/punish <player> burn" );
		sender.sendMessage( EdgeCore.usageColor + "/punish <player> lightning");
		return;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		Player punish = Bukkit.getPlayerExact( args[1] );
		
		if( args.length == 2 ) {
		
			punish.setHealth( punish.getHealth() / 2 );
			return true;
		}
		
		else if( args[2].equalsIgnoreCase("burn") ) {
			
			punish.setFireTicks( 1000 ); // 1000?
			return true;
		}
		
		else if( args[2].equalsIgnoreCase("lightning") ) {
			
			World w = Bukkit.getWorlds().get(0);
			w.strikeLightning( punish.getLocation() );
			return true;
		}
		
		sendUsage(player);
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}

}
