package me.drew1080.acerobbydestroyer;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class AcerObbyDestroyer extends JavaPlugin {

	private AODConfig config = new AODConfig(this);
	private final AODEntityListener entityListener = new AODEntityListener(this);
	public static AcerObbyDestroyer plugin;
	public static final Logger log = Bukkit.getServer().getLogger();

	private static String version;
	private static final String PLUGIN_NAME = "AcerObbyDestroyer";

	@Override
	public void onDisable() {
		config.saveDurabilityToFile();
	}

	@Override
	public void onEnable() {
        PluginDescriptionFile pdfFile = getDescription();
        version = pdfFile.getVersion();
        
		getCommand("acerobbydestroyer").setExecutor(new AODCommands(this));

		config.loadConfig();
		entityListener.setObsidianDurability(config.loadDurabilityFromFile());
		entityListener.setEnchantmenttableDurability(config.loadDurabilityFromFile());

		getServer().getPluginManager().registerEvents(entityListener, this);
	}
	

	public static String getVersion() {
		return version;
	}

	public static String getPluginName() {
		return PLUGIN_NAME;
	}

	@Override
	public String toString() {
		return getPluginName();
	}

	public AODConfig getAODConfig() {
		return config;
	}

	public AODEntityListener getListener() {
		return entityListener;
	}

	public boolean reload() {
		return config.loadConfig();
	}
}
