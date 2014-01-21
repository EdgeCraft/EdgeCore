package net.edgecraft.edgecraft.lang;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.command.AbstractCommand;
import net.edgecraft.edgecraft.command.Level;
import net.edgecraft.edgecraft.user.User;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand extends AbstractCommand {
	
	public LanguageCommand() { /* ... */ }
	
	@Override
	public String[] getNames() {
		String[] names = { "language", "lang" };
		return names;
	}
	
	@Override
	public Level getLevel() {
		return Level.ADMIN;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		if( args.length < 1  || args.length > 3) return false;
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		sender.sendMessage( EdgeCraft.sysColor + "Default language: " + ChatColor.WHITE + LanguageHandler.getDefaultLanguage());
		return true;
	}
	
	@Override
	public boolean runImpl( Player player, User user,  String[] args ) throws Exception {
		
		String userLang = user.getLanguage();
		
		if( !Level.canUse(user, Level.GUEST)) {
			player.sendMessage( lang.getColoredMessage(userLang, "nopermission") );
			return false;
		}
		
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
		
		sender.sendMessage(EdgeCraft.usageColor + "/language");
		sender.sendMessage(EdgeCraft.usageColor + "/language set <language>");
		sender.sendMessage(EdgeCraft.usageColor + "/language set default");
		sender.sendMessage(EdgeCraft.usageColor + "/language list");
		sender.sendMessage(EdgeCraft.usageColor + "/language help");
	}
}