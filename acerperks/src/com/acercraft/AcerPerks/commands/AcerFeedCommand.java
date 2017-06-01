package com.acercraft.AcerPerks.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acercraft.AcerPerks.utils.Cooldowns;
import com.acercraft.AcerPerks.utils.Messages;
import com.acercraft.AcerPerks.utils.Utils;

public class AcerFeedCommand implements CommandExecutor{

	/**
	 * 1200 seconds = 20 minutes
	 */
	
	private int seconds = 1200;

	public boolean onCommand(CommandSender sender, Command command, String stringLabel, String[] args)
	{

		if(stringLabel.equalsIgnoreCase("acerfeed"))
		{
			if(!(((Player)sender) instanceof Player))
			{
				sender.sendMessage("You must be a player to execute this command");
				return true;
			}
			else
			{
				if(Utils.hasPermission((Player)sender, "acerperks.feed"))
				{
					if(Cooldowns.hasFeedCommandCooldown(sender.getName(), seconds) == false)
					{
						Player player = ((Player)sender);
						player.setFoodLevel(20);
						Cooldowns.startFeedCommandCoolDown(sender.getName());
					}
					else
					{
						sender.sendMessage(Messages.getPrefix() + ChatColor.RED + "Sorry, you may use this command in " + Utils.timeToString(Cooldowns.feedCommandCooldown.get(sender.getName())));
					}
				}
				else
				{
					sender.sendMessage(Messages.getPrefix() + ChatColor.RED + "You do not have permission to use this command");
				}
			}
			return true;
		}
		return false;
	}
}