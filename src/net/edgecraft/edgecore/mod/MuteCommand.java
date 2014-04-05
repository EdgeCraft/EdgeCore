package net.edgecraft.edgecore.mod;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand extends AbstractCommand {

	private static final MuteCommand instance = new MuteCommand();
	
	private MuteCommand() { super(); }
	
	public static final MuteCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "mute", "unmute" };
	}

	@Override
	public Level getLevel() {
		return Level.SUPPORTER;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return ( args.length == 2 );
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage( EdgeCore.usageColor + "/mute list" );
		sender.sendMessage( EdgeCore.usageColor + "/mute <player>" );
		sender.sendMessage( EdgeCore.usageColor + "/unmute <player>");
		return;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) {		
		return mute(player, args);
	}
	
	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return mute(sender, args);
	}

	private boolean mute(CommandSender sender, String[] args){
		if (!validArgsRange(args)) {
			sendUsage(sender);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("mute")) {
			
			if (args[1].equalsIgnoreCase("list")) {
				
				StringBuilder sb = new StringBuilder();
				
				for (User user : users.getUsers().values()) {
					if (!user.isMuted())
						continue;
					
					if (sb.length() > 0)
						sb.append(", ");
					
					sb.append("¤6" + user.getName());
				}
				
				sender.sendMessage("¤7Muted players:");
				sender.sendMessage(sb.toString());
				
			} else {
				
				User target = users.getUser(args[1]);
				
				if (target == null) {
					sender.sendMessage((sender instanceof Player ? lang.getColoredMessage(users.getUser(sender.getName()).getLang(), "notfound") 
							: lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "notfound")));
					
					return true;
				}
				
				target.setMuted(true);
				sender.sendMessage("¤7Der Spieler ¤6" + args[1] + " ¤7wurde gemuted!");
			}
			
			return true;
			
		} else if(args[0].equalsIgnoreCase("unmute")) {
			
			User target = users.getUser(args[1]);
			
			if (target == null) {
				sender.sendMessage((sender instanceof Player ? lang.getColoredMessage(users.getUser(sender.getName()).getLang(), "notfound") 
						: lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "notfound")));
				
				return true;
			}
			
			target.setMuted(false);
			sender.sendMessage("¤7Der Spieler ¤6" + args[1] + " ¤7wurde entmuted!");
			
			return true;
		}
		
		return false;
	}	
}