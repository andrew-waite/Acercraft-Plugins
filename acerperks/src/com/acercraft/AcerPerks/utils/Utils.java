package com.acercraft.AcerPerks.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {

	/**
	 * Method to parse minecraft ColorCodes from a String 
	 * Eg. &a, &1, &c etc
	 */

	public static String parseColors(String string)
	{
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	/**
	 * Custom hasPermission method
	 * To allow for cleaner code in large command methods
	 */
	public static boolean hasPermission(Player player, String node)
	{
		if(player.hasPermission(node))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Converts a long time into readable hours and minutes
	 */

	public static String timeToString(long time) {
		// int hours = (int) ((time / 1000 + 8) % 24);
		int minutes = (int) (60 * (time % 1000) / 1000);
		return String.format("%02d", minutes);
	}

	public static void outputError(String msg){
		Bukkit.getLogger().warning(msg);
	}

	public static void outputError(String msg, CommandSender sender){
		sender.sendMessage(ChatColor.RED + "[ERROR] " + msg);
		outputError(msg);
	}
}
