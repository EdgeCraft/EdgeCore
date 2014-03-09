package net.edgecraft.edgecore.command;

import net.edgecraft.edgecore.mod.ModCommands;

public abstract class AbstractModCommand extends AbstractCommand {

	public AbstractModCommand() {
		ModCommands.getInstance().registerCommand( this );
	}
	
}
