package net.edgecraft.edgecore.mod;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class SpawnCommand extends AbstractCommand 
{

	private static final SpawnCommand instance = new SpawnCommand();
	
	private SpawnCommand() { /* ... */ }
	
	public static final SpawnCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[] { "spawn", "setspawn" };
	}

	@Override
	public Level getLevel()	{
		return Level.GUEST;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 1 );
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		final Player p = player;
		final String userLang = user.getLang();
		
		if( args[0].equalsIgnoreCase( "spawn" ) ) {
			
			p.addPotionEffect( new PotionEffect(PotionEffectType.CONFUSION, 20 * 6, 1 ) );
			
			EdgeCore.getInstance().getServer().getScheduler().runTaskLater( EdgeCore.getInstance(), new Runnable() {

				@Override
				public void run() 
				{
					p.teleport( p.getWorld().getSpawnLocation() );
					p.sendMessage( lang.getColoredMessage( userLang, "mod_spawn_success" ) );
				}
				
			}, 20L * 3 );
		}
		
		if( args[0].equalsIgnoreCase( "setspawn" ) ) {
			
			if( !Level.canUse( user, Level.DEVELOPER ) ) {
				player.sendMessage( lang.getColoredMessage( userLang, "nopermission" ) );
				return true;
			}
			
			final Location loc = player.getLocation();
			
			player.getWorld().setSpawnLocation(  loc.getBlockX(), loc.getBlockY(), loc.getBlockZ() );
			player.sendMessage(lang.getColoredMessage( userLang, "mod_setspawn_success").replace("[0]", player.getWorld().getName()));
			
			return true;
		}
		
		return true;
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		if (!(sender instanceof Player)) return;
		
		sender.sendMessage(EdgeCore.usageColor + "/spawn");
		
		User u = users.getUser(sender.getName());
		
		if (!Level.canUse(u, Level.DEVELOPER))
			return;
		
		sender.sendMessage(EdgeCore.usageColor + "/setspawn");
	}

}
