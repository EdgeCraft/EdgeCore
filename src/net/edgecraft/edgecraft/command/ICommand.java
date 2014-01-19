package net.edgecraft.edgecraft.command;


import net.edgecraft.edgecraft.user.User;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ICommand {

	public String getName();
	public void sendUsage( CommandSender sender );
	public Level getLevel();
	
	public boolean runImpl(Player player, User user, String[] args) throws Exception;
	
}
