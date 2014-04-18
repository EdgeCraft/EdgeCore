package net.edgecraft.edgecore.user;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserCommand extends AbstractCommand {

	private static final UserCommand instance = new UserCommand();

	private UserCommand(){ /* ... */ }
	
	public static final UserCommand getInstance() {
		return instance;
	}
	
	@Override
	public String[] getNames() {
		String[] names = { "user" };
		return names;
	}
	
	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		return !( args.length < 2 || args.length > 3);
	}


	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		return super.sysAccess(sender);
	}

	@Override
	public boolean runImpl( Player player, User user, String[] args) throws Exception {
		
		String userLang = user.getLanguage();
		
		if (args[1].equalsIgnoreCase("delete")) {
			if ( args.length != 3) {
				player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
				return false;
			}

			if (!users.exists(args[2])) {
				player.sendMessage(lang.getColoredMessage(userLang, "notfound"));
				return false;
			}

			users.deleteUser(users.getUser(args[2]).getUUID());
			player.sendMessage(lang.getColoredMessage(userLang, "user_delete_success").replace("[0]", args[2]));

			return true;
		}
		
		
		if (args[1].equalsIgnoreCase("exists")) {
			if ( args.length != 3 ) {
				player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
				return false;
			}
			
			if (!users.exists(args[2])) {
				player.sendMessage(lang.getColoredMessage(userLang, "user_exists_false").replace("[0]", args[2]));
				return false;
			}

			player.sendMessage(lang.getColoredMessage(userLang, "user_exists_true").replace("[0]", args[2]));

			return true;
		}

		if (args[1].equalsIgnoreCase("reload")) {
			if (args.length == 2) {
				
				users.synchronizeUsers();
				player.sendMessage(lang.getColoredMessage(userLang, "user_reload_all_success"));

				return true;
			}

			if( args.length != 3 ) return true;
			
				
			if (!users.exists(args[2])) {
				player.sendMessage(lang.getColoredMessage(userLang, "notfound"));
				return false;
			}

			users.synchronizeUser(users.getUser(args[1]).getId());
			player.sendMessage(lang.getColoredMessage(userLang, "user_reload_success").replace("[0]", args[2]));

			return true;
		}
		
		
		if (args[1].equalsIgnoreCase("amount")) {
			if (args.length != 2) {
				player.sendMessage(lang.getColoredMessage(userLang, "argumentexception"));
				return false;
			}

			player.sendMessage(lang.getColoredMessage(userLang, "user_amount").replace("[0]", users.amountOfUsers() + ""));

			return true;
		}

		return true;
	}

	@Override
	public void sendUsageImpl(CommandSender sender) {
		
		sender.sendMessage(EdgeCore.usageColor + "/user delete <user>");
		sender.sendMessage(EdgeCore.usageColor + "/user exists <user>");
		sender.sendMessage(EdgeCore.usageColor + "/user reload [<user>]");
		sender.sendMessage(EdgeCore.usageColor + "/user amount");
	}	
}
