package net.edgecraft.edgecore.db;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.lang.LanguageHandler;
import net.edgecraft.edgecore.user.User;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DatabaseCommand extends AbstractCommand {
	
	private static final DatabaseCommand instance = new DatabaseCommand();
	
	private DatabaseCommand() { /* ... */ }
	
	public static final DatabaseCommand getInstance() {
		return instance;
	}

	@Override
	public String[] getNames() {
		return new String[]{ "database", "db" };
	}

	@Override
	public Level getLevel() {
		return Level.DEVELOPER;
	}
	
	@Override
	public boolean validArgsRange(String[] args) {
		
		if( args.length < 2 || args.length > 6 ) return false;
		
		return true;
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {
		try {
			
			sender.sendMessage(EdgeCore.sysColor + "Databases: " + ChatColor.WHITE + db.getDatabases().replace("edgecraft", ChatColor.AQUA + "edgecraft"));
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
			sender.sendMessage(lang.getColoredMessage(LanguageHandler.getDefaultLanguage(), "globalerror"));
		}
		
		return false;
	}
	
	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
			
			String userLang = user.getLanguage();
		
			// TODO: DOUBLE CHECK IT BITCHES!
			if ( args[1].equalsIgnoreCase("check") ) {
				
				
				if ( args.length > 4 ) {
					sendUsage(player);
					return false;
				}
				
				if (args.length == 2) {
					player.sendMessage(lang.getColoredMessage(userLang, "db_check").replace("[0]", ChatColor.GREEN + "Verbunden!"));
					return true;
				}
				
				if (args.length == 3) {
					try {
						
						if ( !db.existsDatabase(args[2]) ) {
							player.sendMessage(lang.getColoredMessage(userLang, "db_check_db_false").replace("[0]", args[2]));
							return true;
						}
						
						player.sendMessage(lang.getColoredMessage(userLang, "db_check_db_true").replace("[0]", args[2]));
						
					} catch(Exception e) {
						e.printStackTrace();
						player.sendMessage(lang.getColoredMessage(userLang, "globalerror"));
					}
					
					return true;
				}
				
				if (args.length == 4) {
					try {
						
						if (!db.existsDatabase(args[2])) {
							player.sendMessage(lang.getColoredMessage(userLang, "db_check_db_false").replace("[0]", args[2]));
							return true;
						}
						
						if (!db.existsTable(args[3])) {
							player.sendMessage(lang.getColoredMessage(userLang, "db_check_table_false").replace("[0]", args[3]));
							return true;
						}
						
						player.sendMessage(lang.getColoredMessage(userLang, "db_check_table_true").replace("[0]", args[3]));
						
					} catch(Exception e) {
						e.printStackTrace();
						player.sendMessage(lang.getColoredMessage(userLang, "globalerror"));
					}
					
					return true;
				}
			}
			
			
			
			
			if (args[1].equalsIgnoreCase("connect")) {
				if ( args.length != 6) {
					sendUsage(player);
					return false;
				}
				
				try {
						
						if ( !db.isAvailable() ) {
							player.sendMessage(lang.getColoredMessage(userLang, "db_connect_alreadyconnected"));
							return false;
						}
						
						db.loadConnection(args[2], args[3], args[4], args[5]);
						player.sendMessage(lang.getColoredMessage(userLang, "db_connect_success").replace("[0]", args[5]));
						
					} catch(Exception e) {
						e.printStackTrace();
						player.sendMessage(lang.getColoredMessage(userLang, "db_connect_failed").replace("[0]", args[5]));
						
					} finally {
						db.setConnection( null );
					}
			}
		
			
			
			
			if ( args[1].equalsIgnoreCase("close") ) {
				
				if( args.length > 2 ) {
					sendUsage(player);
					return false;
				}
				
				try {
					
					db.closeConnection();
					player.sendMessage(lang.getColoredMessage(userLang, "db_close_success"));
					
				} catch(Exception e) {
					e.printStackTrace();
					player.sendMessage(lang.getColoredMessage(userLang, "db_close_failed"));
				}
			}	
		return true;
	}

	@Override
	public void sendUsage( CommandSender sender ) {

		if( !(sender instanceof Player) || EdgeCore.getUsers().getUser(((Player)sender).getName()).getLevel() != Level.ADMIN ) 
				return;
	
		
			sender.sendMessage(EdgeCore.usageColor + "/db check <name> <table>");
			sender.sendMessage(EdgeCore.usageColor + "/db connect <host> <user> <pw> <database>");
			sender.sendMessage(EdgeCore.usageColor + "/db close");
			return;
	}
}
