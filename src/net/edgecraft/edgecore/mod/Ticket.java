package net.edgecraft.edgecore.mod;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import net.edgecraft.edgecore.user.User;

public class Ticket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User author;
    private String title;
    private String msg;
    private int id;
    private Date date;
    
    
    protected Ticket() { /* ... */ }
    
    protected Ticket( User author, String title, String msg, int id, Date date ) {
   	 
   	 setAuthor( author );
   	 setTitle( title );
   	 setMsg( msg );
   	 setID( id );
   	 setDate( date );
   	
    }
    
    
    // ----------
    
    public String getGist() {
            return ( "ID: " + this.getID() + " " + "[" + this.getDateAsString() + "]" + " " + "[" + this.getAuthor() + "]" + ": " + this.getTitle() );
    }
    
    public String getInfo() {
            return ( "[" + this.getTitle() + "]" + "\n" + this.getMsg() );
    }
    
    
    // ----------
    
    public User getAuthor() {
            return author;
    }
    
    public String getTitle() {
            return title;
    }
    
    public String getMsg() {
            return msg;
    }
    
    public int getID() {
            return id;
    }
    
    public Date getDate() {
            return date;
    }
    
    // -----------
    
    protected void setAuthor( User author ) {
            if( author != null ) {
                    this.author = author;
            }
    }
    
    
    protected void setTitle( String title ) {
            if( isValidString( title ) ) {
                    this.title = title;
            }
    }
    
    protected void setMsg( String msg ) {
            if( isValidString( msg ) ) {
                    this.msg = msg;
            }
    }
    
    protected void setID( int id ) {
            if( id >= 0 ) {
                    this.id = id;
            }
    }
    
    protected void setDate( Date date ) {
            if( date != null ) {
                    this.date = date;
            }
    }
    
    
    // -------------
    
    public static boolean isValidString( String s ) {
            if( s != null && s.trim().length() > 0 )
                    return true;
            
            return false;
    }
    
    private String getDateAsString() {
            return DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.SHORT ).format( getDate() );
    }
    
    // --------------
    
    @Override
    public boolean equals( Object obj ) {
            
            if( this == obj ) return true;
            if( obj == null ) return false;
            if( getClass() != obj.getClass() ) return false;
            
            final Ticket another = (Ticket) obj;
            
            if( getAuthor().equals(another.getAuthor() )) {
                    if( getTitle().equals(another.getTitle())) {
                            if( getMsg().equals(another.getMsg() )) {
                                return true;
                            }
                    }
            }
            
            return false;
    }
    
    @Override
    public int hashCode() {
            return (int) getAuthor().hashCode() * getTitle().hashCode() + getMsg().hashCode() * getDate().toString().hashCode();
    }
	
}
