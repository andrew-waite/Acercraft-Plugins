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

public class AcerStrengthCommand implements CommandExecutor{
	
	/**
	 * Time for cooldown, this is in seconds. The cooldown is for 20 minutes
	 */
	
	private int seconds = 1200;
	
	public boolean onCommand(CommandSender sender, Command command, String stringLabel, String[] args)
	{
		if(stringLabel.equalsIgnoreCase("acerstrength"))
		{
			if(!(((Player)sender) instanceof Player))
			{
				sender.sendMessage("You must be a player to execute this command");
				return true;
			}
			else
			{
				if(Utils.hasPermission((Player)sender, "acerperks.strength"))
				{

					if(Cooldowns.hasStrengthCommandCooldown(sender.getName(), seconds) == true)
					{
						/**
						 * PotionEffectType.EffectType, time in ticks, amplification
						 * INCREASE_DAMAGE is Strength and  REGENERATION IS Regen
						 * 600 is in server ticks. 20 ticks = 1 second. 600 ticks = 30 seconds
						 * Amplification, level 2. Amplification is just the level of the potion, EG Strength I, Strength II etc
						 */
						((Player)sender).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 2));
						((Player)sender).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 2));
					
						Cooldowns.startStrengthCommandCoolDown(sender.getName());
					}
					else
					{
						sender.sendMessage(Messages.getPrefix() + ChatColor.RED + "Sorry, you may use this command in " + Utils.timeToString(Cooldowns.strengthCommandCooldown.get(sender.getName())));
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
