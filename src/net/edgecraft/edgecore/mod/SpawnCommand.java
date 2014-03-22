package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpawnCommand extends AbstractCommand {
	
	private static SpawnCommand instance = new SpawnCommand();
	
	private SpawnCommand() { super(); }
	
	public static final SpawnCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[] { "spawn" };
	}

	@Override
	public Level getLevel() {
		return Level.GUEST;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return args.length == 1;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		final Player p = player;
		final String userLang = user.getLanguage();
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 3, 1));
		
		EdgeCore.getInstance().getServer().getScheduler().runTaskLater(EdgeCore.getInstance(), new Runnable() {
			
			public void run() {
				
				p.teleport(p.getWorld().getSpawnLocation());
				p.sendMessage(lang.getColoredMessage(userLang, "mod_spawn_success"));
				
			}
			
		}, 20L * 3);
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return true;
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage(EdgeCore.usageColor + "/spawn");
		
	}	
}
