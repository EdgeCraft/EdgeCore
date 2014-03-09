package net.edgecraft.edgecore.mod;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractModCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class DifficultyCommand extends AbstractModCommand {

	private static final DifficultyCommand instance = new DifficultyCommand();
	
	private DifficultyCommand() { super(); }
	
	public static final DifficultyCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "difficulty" };
	}

	@Override
	public Level getLevel() {
		return Level.ADMIN;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 );
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage( EdgeCore.usageColor + "/difficulty peaceful");
		sender.sendMessage( EdgeCore.usageColor + "/difficulty easy");
		sender.sendMessage( EdgeCore.usageColor + "/difficulty normal");
		sender.sendMessage( EdgeCore.usageColor + "/difficulty hard");
		return;
		
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {
				
		if( Bukkit.isHardcore() ) return false;
		
		World w = Bukkit.getWorlds().get(0);
		
		if( args[1].equalsIgnoreCase( "peaceful" ) ) {
			w.setDifficulty( Difficulty.PEACEFUL );
			return true;
		}
		
		if( args[1].equalsIgnoreCase( "easy" ) ) {
			w.setDifficulty( Difficulty.EASY );
			return true;
		}
		
		if( args[1].equalsIgnoreCase( "normal" ) ) {
			w.setDifficulty( Difficulty.NORMAL );
			return true;
		}
		
		if( args[1].equalsIgnoreCase( "hard" ) ) {
			w.setDifficulty( Difficulty.HARD );
			return true;
		}
		
		sendUsage( player );
		return true;
		
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		sendUsage( sender );
		return true;
	}

}
