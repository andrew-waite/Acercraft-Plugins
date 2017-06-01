package com.acercraft.ACPEssentials.Util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util {
	
	public static String parseColors(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public static boolean hasPerm(Player player, String node){
		return player.hasPermission(node);
	}

}
