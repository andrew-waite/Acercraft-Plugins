package com.acercraft.AcerStaffChat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StaffChatCommand implements CommandExecutor{
	
	public AcerStaffChat plugin;
	
	public StaffChatCommand(AcerStaffChat instance)
	{
		this.plugin = instance;
	}

	public boolean onCommand(CommandSender commandSender, Command command, String stringLabel, String[] args) {
		if(command.getName().equalsIgnoreCase("staffchat"))
		{
			if(plugin.getAdminList().contains(commandSender.getName()))
			{
				this.plugin.removeAdminList(commandSender.getName());
				commandSender.sendMessage(plugin.PREIFX + ChatColor.GREEN + "Staff chat now disabled");
				return true;
			}
			else
			{
				this.plugin.addAdminList(commandSender.getName());
				commandSender.sendMessage(plugin.PREIFX + ChatColor.GREEN + "Staff chat now enabled");
				return true;
			}
		}
		return false;
	}
	
	

}
