package com.acercraft.AcerPerks.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.acercraft.AcerPerks.utils.Cooldowns;
import com.acercraft.AcerPerks.utils.Messages;
import com.acercraft.AcerPerks.utils.Utils;

public class AcerSpeedCommand implements CommandExecutor{
	
	/**
	 * Time for cooldown, this is in seconds. The cooldown is for 15 minutes
	 */
	
	private int seconds = 900;
	
	public boolean onCommand(CommandSender sender, Command command, String stringLabel, String[] args)
	{
		if(stringLabel.equalsIgnoreCase("acerspeed"))
		{
			if(!(((Player)sender) instanceof Player))
			{
				sender.sendMessage("You must be a player to execute this command");
				return true;
			}
			else
			{
				if(Utils.hasPermission((Player)sender, "acerperks.speed"))
				{

					if(Cooldowns.hasSpeedCommandCooldown(sender.getName(), seconds) == true)
					{
						/**
						 * PotionEffectType.EffectType, time in ticks, amplification
						 * SPEED, is the potion effect type speed
						 * 1200 is in server ticks. 20 ticks = 1 second. 1200 ticks = 60 seconds
						 * Amplification, level 3. Amplification is just the level of the potion, EG Strength I, Strength II etc
						 */
						((Player)sender).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 3));
						Cooldowns.startSpeedCommandCoolDown(sender.getName());
					}
					else
					{
						sender.sendMessage(Messages.getPrefix() + ChatColor.RED + "Sorry, you may use this command in " + Utils.timeToString(Cooldowns.speedCommandCooldown.get(sender.getName())));
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
