package net.edgecraft.edgecore.mod;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class WeatherCommand extends AbstractCommand {

	@Override
	public String[] getNames() {
		String[] names = { "weather" };
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.ADMIN;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return( args.length == 2 || args.length == 3 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
			if( sender instanceof Player ) {
				User u = EdgeCoreAPI.userAPI().getUser( ((Player)sender).getName() );
				
				if( !Level.canUse(u, getLevel() ) ) return;
			}
			
			sender.sendMessage( EdgeCore.usageColor + "/weather clear");
			sender.sendMessage( EdgeCore.usageColor + "/weather rain");
			sender.sendMessage( EdgeCore.usageColor + "/weather storm");
			return;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		return weather( player, args );
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return weather( sender, args );
	}
	
	private boolean weather( CommandSender sender, String[] args ) {
		
			World w = Bukkit.getWorlds().get(0);
			
			if( args[1].equalsIgnoreCase( "clear" ) ) {

				w.setStorm( false );
				w.setThundering( false );
			}
			
			else if( args[1].equalsIgnoreCase( "rain" ) ) {
				
				w.setStorm( true );
				w.setThundering( false );
			}
			
			else if( args[1].equalsIgnoreCase( "storm" ) ) {
				
				w.setStorm(true);
				w.setThundering( true );
			}
			
			else {
				sendUsage( sender );
				return false;
			}
			
			int duration;
			
			if( args.length == 3 ) {
				duration = Integer.valueOf( args[2] );
				
			} else {
				duration = ( 428 + new Random().nextInt( 534 ) ) * 23;
			}
			
			w.setWeatherDuration( duration );
			w.setThunderDuration( duration );
		
			sendUsage( sender );
			return true;
	}

}
