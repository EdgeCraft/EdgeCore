package net.edgecraft.edgecore.lang;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand extends AbstractCommand {
	
	private static final LanguageCommand instance = new LanguageCommand();
	
	private LanguageCommand() { /* ... */ }
	
	public static final LanguageCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		return new String[]{ "language", "lang" };
	}
	
	@Override
	public Level getLevel() {
		return Level.GUEST;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		if( args.length < 1  || args.length > 3) return false;
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		sender.sendMessage( EdgeCore.sysColor + "Default language: " + ChatColor.WHITE + LanguageHandler.getDefaultLanguage());
		return true;
	}
	
	@Override
	public boolean runImpl( Player player, User user,  String[] args ) throws Exception {
		
		String userLang = user.getLanguage();
		
		if( args.length == 1 ) {
			player.sendMessage(lang.getColoredMessage(userLang, "lang_info").replace("[0]", user.getLanguage()));
			return true;
		}
		
		if( args[1].equalsIgnoreCase("set") ) {
			if( args.length != 3 )
					sendUsage(player);
			
			if( args[2].equalsIgnoreCase("default")) {
				user.updateLanguage(LanguageHandler.getDefaultLanguage());
				player.sendMessage(lang.getColoredMessage(userLang, "lang_set_default"));

				return true;
			} 
			
			if (!lang.exists(args[2])) {
				player.sendMessage(lang.getColoredMessage(userLang, "unknownlanguage").replace("[0]", args[2]));
				return false;
			}

			user.updateLanguage(args[2]);
			player.sendMessage(lang.getColoredMessage(userLang, "lang_set").replace("[0]", args[2]));
			
		}
		
		if( args[1].equalsIgnoreCase("list") ) {
			//TODO: to implement
			player.sendMessage(lang.getColoredMessage(userLang, "globalerror"));
			return true;
			
		}
		
		if (args[1].equalsIgnoreCase("help")) {
			sendUsage(player);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void sendUsage( CommandSender sender ) {
		
		sender.sendMessage(EdgeCore.usageColor + "/language");
		sender.sendMessage(EdgeCore.usageColor + "/language set <language>");
		sender.sendMessage(EdgeCore.usageColor + "/language set default");
		sender.sendMessage(EdgeCore.usageColor + "/language list");
		sender.sendMessage(EdgeCore.usageColor + "/language help");
	}
}