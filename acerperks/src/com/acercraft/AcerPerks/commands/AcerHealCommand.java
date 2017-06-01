package com.acercraft.AcerPerks.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acercraft.AcerPerks.utils.Cooldowns;
import com.acercraft.AcerPerks.utils.Messages;
import com.acercraft.AcerPerks.utils.Utils;

public class AcerHealCommand implements CommandExecutor{

	/**
	 * This int, is how long the cooldown is for in seconds. 1200 seconds is equvilant to 20 minutes.
	 */

	private int seconds  = 1200;

	public boolean onCommand(CommandSender sender, Command command, String stringLabel, String[] args)
	{
		if(stringLabel.equalsIgnoreCase("acerheal"))
		{
			if(!(((Player)sender) instanceof Player))
			{
				sender.sendMessage("You must be a player to execute this command");
				return true;
			}
			else
			{
				if(Utils.hasPermission((Player)sender, "acerperks.heal"))
				{

					if(Cooldowns.hasHealCommandCooldown(sender.getName(), seconds) == true)
					{
						Player player = ((Player)sender);
						player.setHealth(20.0D);
						Cooldowns.startHealCommandCoolDown(sender.getName());
					}
					else
					{
						sender.sendMessage(Messages.getPrefix() + ChatColor.RED + "Sorry, you may use this command in " + Utils.timeToString(Cooldowns.healCommandCooldown.get(sender.getName())));
					}
				}
				else
				{
					sender.sendMessage(Messages.getPrefix() + ChatColor.RED + "Sorry you don't have permission to use this command");
				}
			}
			return true;
		}
		return false;
	}
}
