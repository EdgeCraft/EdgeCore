package net.edgecraft.edgecore.mod;

import java.util.Calendar;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.edgecraft.edgecore.EdgeCore;
import net.edgecraft.edgecore.EdgeCoreAPI;
import net.edgecraft.edgecore.command.AbstractCommand;
import net.edgecraft.edgecore.command.Level;
import net.edgecraft.edgecore.user.User;

public class TicketCommand extends AbstractCommand {


	TicketManager tickets = EdgeCoreAPI.ticketAPI();
	
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
	public boolean validArgsRange(String[] args) {
		return ( args.length >= 1 );
	}

	@Override
	public void sendUsage(CommandSender sender) {
		
		if( sender instanceof Player ) {
			
			User u = EdgeCoreAPI.userAPI().getUser( ((Player)sender).getName() );
			
			if( !Level.canUse( u, Level.USER )) return;
			
			sender.sendMessage( EdgeCore.usageColor + "/ticket open title msg" );
			
			if( !Level.canUse( u, Level.SUPPORTER )) return;
		}	
		
		else sender.sendMessage( EdgeCore.usageColor + "/ticket open title msg");

		
			sender.sendMessage( EdgeCore.usageColor + "/ticket" );        
            sender.sendMessage( EdgeCore.usageColor + "/ticket list" );
            sender.sendMessage( EdgeCore.usageColor + "/ticket read ID" );
            sender.sendMessage( EdgeCore.usageColor + "/ticket close ID" );	
            return;
	}

	@Override
	public boolean runImpl(Player player, User user, String[] args) throws Exception {
		
		String userLang = user.getLanguage();
        
        // '/ticket open TITLE MSG'
        if( args.length >= 4 ) {
        	
        	if( !Level.canUse( user, Level.USER ) ) {
        		player.sendMessage( lang.getColoredMessage( userLang,  "nopermission") );
        		return false;
        	}
        	
        	if( args[1].equalsIgnoreCase( "open" ) ) {
        		
        		Ticket t = new Ticket();
        		t.setAuthor( user );
        		t.setTitle( args[2] );
        		t.setID( tickets.generateID() );
        		t.setDate( Calendar.getInstance().getTime() );
        		
        		StringBuilder msg = new StringBuilder();
        		
        		for( int i = 3; i < args.length; i++ ) {
        			msg.append( args[i] + " " );
        		}
        		
        		t.setMsg( msg.toString() );
        	
        		tickets.addTicket( t );
        		tickets.notifyAll( Level.SUPPORTER, t );
        		
        		return true;
        	} else {
        		sendUsage( player );
        		return true;
        	}	
        }
        
        // All followig commands are TEAM-only
        if( !Level.canUse(user, Level.SUPPORTER) ) {
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
        if( args[1].equalsIgnoreCase( "list" ) ) {
                
                if( args.length != 2 ) {
                	sendUsage(player);
                	return true;
                }

                try {
                        
                                int greatestID = tickets.amountOfTickets();
                                
                                if( greatestID == 0 ) { 
                                	player.sendMessage( ChatColor.RED + "No open tickets." );
                                	return true;
                                }
                        
                                for( int i = greatestID; i > 0; i-- ) {
                                        player.sendMessage( ChatColor.RED + tickets.getTicket(i).getGist() );
                                }
                        
                } catch( Exception e ) {
                                e.printStackTrace();
                                return false;
                }
                
                return true;
        }
        
        else if( args[1].equalsIgnoreCase("read") ) {
        	
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
        	
        	try {
        		Ticket read = tickets.getTicket( tickets.getTicket( Integer.valueOf( args[2] ) ) );
            	
        		if( read == null ) {
        			player.sendMessage("Ticket not found.");
        			return false;
        		}
        		
            	player.sendMessage( ChatColor.BLUE + read.getGist() );
            	player.sendMessage( ChatColor.GREEN + read.getInfo() );
            	
        	} catch( NumberFormatException e ) {
        		player.sendMessage("Ticket not found.");
        		return false;
        	}

        	return true;
        }
        
        else if( args[1].equalsIgnoreCase("close") ) {
        	
        	if( args.length != 3 ) {
        		sendUsage( player );
        		return true;
        	}

        	try {
        		tickets.removeTicket( Integer.valueOf( args[2] ) );
        	 } catch( NumberFormatException e ) {
        		 player.sendMessage("Ticket not found.");
        		 return false;
        	 }
        	
        	return true;
        }
        
        else if( args[1].equalsIgnoreCase("enable") ) {
        	tickets.removeDontNotify( user );
        	player.sendMessage("Ticket-notification enabled.");
        	return true;
        }
        
        else if( args[1].equalsIgnoreCase("disable") ) {
        	tickets.addDontNotify( user );
        	player.sendMessage("Ticket-notification disabled.");
        	return true;
        }
        
        sendUsage(player);
        return true;
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
