package net.edgecraft.edgecraft.mod;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecraft.EdgeCraft;
import net.edgecraft.edgecraft.EdgeCraftAPI;
import net.edgecraft.edgecraft.command.AbstractCommand;
import net.edgecraft.edgecraft.command.Level;
import net.edgecraft.edgecraft.user.User;

public class TicketCommand extends AbstractCommand {


	TicketManager tickets = EdgeCraftAPI.ticketAPI();
	
	public static final int defaultAmountTickets = 10;
	
	public TicketCommand() { /* .... */ }
	
	@Override
	public String[] getNames() {
		String[] names = { "ticket" };
		return names;
	}
	
	@Override
	public Level getLevel() {
		return Level.USER;
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
            sender.sendMessage( EdgeCraft.usageColor + "/ticket" );
            sender.sendMessage( EdgeCraft.usageColor + "/ticket open 'title' 'msg'" );        
            sender.sendMessage( EdgeCraft.usageColor + "/ticket list" );
            sender.sendMessage( EdgeCraft.usageColor + "/ticket read ID" );
            sender.sendMessage( EdgeCraft.usageColor + "/ticket close ID" );
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception{
		
		String userLang = user.getLanguage();
        
		
        // '/ticket open "title" "msg"'
        if( args.length == 3 ) {
                
                if( !Level.canUse( user, Level.USER ) ) {
                        player.sendMessage( lang.getColoredMessage(userLang, "nopermission") );
                        return false;
                }
                
                if( args[1] == "open" && args.length == 4 ) {
                        
                        tickets.addTicket( user, args[2], args[3] );
                        
                } else {
                        sendUsage(player);
                }
        }
        
        // All followig commands are TEAM-only
        if( !Level.canUse(user, Level.TEAM) ) {
            player.sendMessage( lang.getColoredMessage(userLang, "nopermission") );
            return false;
        }
		
        // '/ticket'
        if( args.length == 1 ) {
        	
                try {
                        
                        int greatestID = tickets.amountOfTickets();
                        int amount = 0;
                        
                        for( int i = greatestID; i > 0; i-- ) {

                                if( amount == TicketCommand.defaultAmountTickets ) return true;
                                
                                ++amount;
                                player.sendMessage( tickets.getTicket(i).getGist() );
                                
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                
                return true; 
        }
        
        // '/ticket list'
        if( args[1].equalsIgnoreCase("list") ) {
                
                if( args.length != 2 ) {
                	sendUsage(player);
                	return true;
                }

                try {
                        
                                int greatestID = tickets.amountOfTickets();
                        
                                for( int i = greatestID; i > 0; i-- ) {
                                        player.sendMessage( tickets.getTicket(i).getGist() );
                                }
                        
                        } catch( Exception e ) {
                                e.printStackTrace();
                        }
                } else {
                        sendUsage(player);
                }
        
        if( args[1].equalsIgnoreCase("read")) {
        	
        	if( args.length == 2 ) {
        		
        		Ticket read = tickets.getTicket( tickets.amountOfTickets() );
        		
        		player.sendMessage( read.getGist() );
        		player.sendMessage( read.getInfo() );
        		
        		return true;
        	} 
        	
        	if( args.length != 3 ) {
        		sendUsage( player );
        		return true;
        	}
        	
        	Ticket read = tickets.getTicket( tickets.getTicket( Integer.valueOf( args[2] ) ) );
        	player.sendMessage( read.getGist() );
        	player.sendMessage( read.getInfo() );
        	
        	
        }
        
        if( args[1].equalsIgnoreCase("close") ) {
        	
        	if( args.length != 3 ) {
        		sendUsage( player );
        		return true;
        	}
        	
        	tickets.removeTicket( Integer.valueOf( args[2] ) );
        	return true;
        }
        
        sendUsage(player);
        return true;
	}

	@Override
	public boolean validArgsRange(String[] args) {
		return !( args.length < 1 || args.length > 4 );
	}

	@Override
	public boolean sysAccess(CommandSender sender, String[] args) {

        try {
            for( int i = 1; i < tickets.amountOfTickets(); i++ ) {
                    
                    sender.sendMessage(ChatColor.GREEN + tickets.getTicket(i).getGist());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
	}
}
