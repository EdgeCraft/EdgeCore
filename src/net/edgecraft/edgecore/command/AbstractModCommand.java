package net.edgecraft.edgecore.command;

import net.edgecraft.edgecore.mod.ModCommands;

public abstract class AbstractModCommand extends AbstractCommand {

	public AbstractModCommand( AbstractModCommand cmd  ) {
		ModCommands.registerCommand( cmd );
	}
	
}
