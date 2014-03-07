package net.edgecraft.edgecore.mod;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class GiveCommand extends AbstractCommand {

	private static final GiveCommand instance = new GiveCommand();
	
	private GiveCommand() { /* ... */ }
	
	public static final GiveCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "give" };
	}

	@Override
	public Level getLevel() {
		return Level.MODERATOR;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length >= 3 && args.length <= 5 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		sender.sendMessage( EdgeCore.usageColor + "/give <player> <item> [amount] [data]" );
		return;
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		Player p = Bukkit.getPlayerExact( args[1] );
		Material m = null;
		
		try {
			m = Material.getMaterial( Integer.valueOf( args[2] ) );
		} catch( NumberFormatException e ) {
			m = Material.getMaterial( args[2] );
		}
		
		int amount = 1;
		short data = 0;
		
		if( p == null ) {
			player.sendMessage( "Player " + args[1] + " not found.");
			return false;
		} else if ( m == null ) {
			player.sendMessage( "Material " + args[2] + " not found." );
			return false;
		}
		
		if( args.length >= 4 ) {
			try {
				amount = Integer.valueOf( args[3] );
			} catch( NumberFormatException e ) {
				sendUsage( player );
			}
		} 
		
		if( args.length == 5 ) {
			try {
				data = Short.valueOf( args[4] );
			} catch( NumberFormatException e ) {
				sendUsage( player );
			}
		}
		
		p.getInventory().addItem( new ItemStack( m, amount, data ) );

		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}

}
