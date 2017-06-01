package me.drew1080.acerobbydestroyer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;


public final class Log {
	private static String pre = "[" + AcerObbyDestroyer.getPluginName() + "] ";
	private static final Logger LOG = Bukkit.getServer().getLogger();

	private Log() {

	}

	public static void info(String message) {
		LOG.log(Level.INFO, pre + message);
	}


	public static void warning(String message) {
		LOG.log(Level.WARNING, pre + message);
	}


	public static void severe(String message) {
		LOG.log(Level.SEVERE, pre + message);
	}

	public static Logger getLogger() {
		return LOG;
	}
	
	public static String getPrefix() {
		return pre;
	}
}
