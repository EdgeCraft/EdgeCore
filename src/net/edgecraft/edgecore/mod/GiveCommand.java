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

	@Override
	public String[] getNames() {
		String[] names = { "give" };
		return names;
	}

	@Override
	public Level getLevel() {
		return Level.ADMIN;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length > 2 && args.length <= 5 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		sender.sendMessage( EdgeCore.usageColor + "/give <player> <item> [amount] [data]" );
		return;
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
		
		Player p = Bukkit.getPlayerExact( args[1] );
		Material m = Material.getMaterial( args[2] );
		int amount = 1;
		short data = 0;
		
		if( args.length >= 4 ) {
			amount = Integer.valueOf( args[3] );
		} 
		
		if( args.length == 5 ) {
			data = Short.valueOf( args[4] );
		}
		
		p.getInventory().addItem( new ItemStack( m, amount, data ) );

		sendUsage( player );
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}

}
