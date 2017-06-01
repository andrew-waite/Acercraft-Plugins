package com.acercraft.AcerTag.Util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util {

	public static String parseColors(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public static String stripColors(String announce) {
		announce = announce.replaceAll("&0", "");
		announce = announce.replaceAll("&1", "");
		announce = announce.replaceAll("&2", "");
		announce = announce.replaceAll("&3","");
		announce = announce.replaceAll("&4","");
		announce = announce.replaceAll("&5", "");
		announce = announce.replaceAll("&6","");
		announce = announce.replaceAll("&7","");
		announce = announce.replaceAll("&8", "");
		announce = announce.replaceAll("&9", "");
		announce = announce.replaceAll("&a", "");
		announce = announce.replaceAll("&b","");
		announce = announce.replaceAll("&c", "");
		announce = announce.replaceAll("&d","");
		announce = announce.replaceAll("&e", "");
		announce = announce.replaceAll("&f", "");
		announce = announce.replaceAll("&l", "");
		announce = announce.replaceAll("&m", "");
		announce = announce.replaceAll("&n", "");
		announce = announce.replaceAll("&o", "");
		announce = announce.replaceAll("&r", "");
		return announce;
	}
	
	public static boolean hasPerm(Player player, String node) {
		return player.hasPermission(node);
	}
	
}
