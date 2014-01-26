package net.edgecraft.edgecore.command;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CommandEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private String[] command;
	private CommandSender sender;
	private boolean cancel = true;
	
	public CommandEvent(CommandSender sender, String[] command) {
		
		setCommand( command );
		setSender( sender );
		
	}
	
	protected void setCommand( String[] command ) {
		if( command != null && command.length >= 0) {
			this.command = command;
		}
	}
	
	protected void setSender( CommandSender sender ) {
		if( sender != null ) {
			this.sender = sender;
		}
	}
	
	public String[] getCommand(){
		return command;
	}
	
	public CommandSender getSender(){
		return sender;
	}
	
	public boolean isCancelled(){
		return cancel;
	}
	
	public void cancel(){
		cancel = false;
	}
	
}
