package net.edgecraft.edgecraft.mod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.edgecraft.edgecraft.user.User;

public class TicketManager {
	
	private Map<Integer, Ticket> tickets = new HashMap<>();
	
	private static final TicketManager instance = new TicketManager();
	
	protected TicketManager() { autoSave(); }
	
	
	public static final TicketManager getInstance() {
		return instance;
	}
	
	public Map<Integer, Ticket> getTickets() { return tickets; }
	
	public void addTicket( User author, String title, String msg ) {
		if( author == null || title == null || msg == null ) return;
		
		addTicket( new Ticket( author, title, msg, generateID(), Calendar.getInstance().getTime() ) );
	}
	
	protected void addTicket( Ticket ticket ) {
		if( ticket != null )
			tickets.put(ticket.getID(), ticket);
	}
	
	public void removeTicket( int id ) {
		if( id <= 0 ) return; 
		tickets.remove(id);
	}
	
	public int amountOfTickets() {
		return tickets.size();
	}
	
	public int generateID() {
		return amountOfTickets()+1;
	}
	
	public Ticket getTicket( int id ) {
		return tickets.get( id );
	}
	
	public Ticket getTicket( Ticket ticket ) {
       
		int id = -1;
        
        for( Map.Entry<Integer, Ticket> entry : tickets.entrySet() ) {
                Ticket forSearch = entry.getValue();
                
                if( forSearch.equals( ticket ) ) {
                        id = forSearch.getID();
                        break;
                }
        }
        
        return getTicket(id);
	}
	
	private void saveTickets() {
		
		try {
			
			ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( "tickets.tmp" ) );
			
			out.writeObject( tickets );
			
			out.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static Map<Integer, Ticket> loadTickets( String path ) {
		
		HashMap<Integer, Ticket> tickets = null;
		
		try {
			ObjectInputStream in = new ObjectInputStream( new FileInputStream( path ));
			
			tickets = (HashMap<Integer, Ticket>) in.readObject();
			
			in.close();
			
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		} catch( IOException e ) {
			e.printStackTrace();
		} catch( ClassNotFoundException e ) {
			e.printStackTrace();
		}
	
		return tickets;
	}
	
	private void autoSave() {
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				
				saveTickets();
				
				autoSave();
			}
			
		}, 5 * 60000 );
		
	}
	
}
