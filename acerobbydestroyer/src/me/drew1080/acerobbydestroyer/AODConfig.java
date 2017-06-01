package me.drew1080.acerobbydestroyer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public final class AODConfig {

	private AcerObbyDestroyer plugin;
	private static String pluginName;
	private static String pluginVersion;


	private static String directory = "plugins" + File.separator + AcerObbyDestroyer.getPluginName() + File.separator;
	private File configFile = new File(directory + "config.yml");
	private File durabilityFile = new File(directory + "durability.dat");
	private File enchantmentDurabilityFile = new File(directory +"enchantDurability.dat");
	private YamlConfiguration bukkitConfig = new YamlConfiguration();

	private int explosionRadius = 3;
	private boolean tntEnabled = true;
	private boolean cannonsEnabled = false;
	private boolean creepersEnabled = false;
	private boolean ghastsEnabled = false;
	private boolean durabilityEnabled = false;
	private int durability = 1;
	private boolean durabilityTimerEnabled = true;
	private long durabilityTime = 600000L; // 10 minutes
	private double chanceToDropBlock = 0.7;
	private boolean waterProtection = true;


	public AODConfig(AcerObbyDestroyer plugin) {
		this.plugin = plugin;
		pluginName = AcerObbyDestroyer.getPluginName();
	}

	public boolean loadConfig() {
		boolean isErrorFree = true;
		pluginVersion = AcerObbyDestroyer.getVersion();

		new File(directory).mkdir();

		if (configFile.exists()) {
			try {
				bukkitConfig.load(configFile);

				if (bukkitConfig.getString("Version", "").equals(pluginVersion)) {
					// config file exists and is up to date
					Log.info(pluginName + " config file found, loading config...");
					loadData();
				} else {
					// config file exists but is outdated
					Log.info(pluginName + " config file outdated, adding old data and creating new values. " + "Make sure you change those!");
					loadData();
					writeDefault();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// config file does not exist
			try {
				Log.info(pluginName + " config file not found, creating new config file...");
				configFile.createNewFile();
				writeDefault();
			} catch (IOException ioe) {
				Log.severe("Could not create the config file for " + pluginName + "!");
				ioe.printStackTrace();
				isErrorFree = false;
			}
		}

		return isErrorFree;
	}

	private void loadData() {
		try {
			bukkitConfig.load(configFile);

			explosionRadius = bukkitConfig.getInt("Radius", 3);
			waterProtection = bukkitConfig.getBoolean("FluidsProtect", true);

			tntEnabled = bukkitConfig.getBoolean("EnabledFor.TNT", true);
			cannonsEnabled = bukkitConfig.getBoolean("EnabledFor.Cannons", false);
			creepersEnabled = bukkitConfig.getBoolean("EnabledFor.Creepers", false);
			ghastsEnabled = bukkitConfig.getBoolean("EnabledFor.Ghasts", false);

			durabilityEnabled = bukkitConfig.getBoolean("Durability.Enabled", false);
			durability = bukkitConfig.getInt("Durability.Amount", 1);
			durabilityTimerEnabled = bukkitConfig.getBoolean("Durability.ResetEnabled", true);
			durabilityTime = readLong("Durability.ResetAfter", "600000");

			chanceToDropBlock = bukkitConfig.getDouble("Blocks.ChanceToDrop", 0.7);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeDefault() {
		write("Version", AcerObbyDestroyer.getVersion());
		write("Radius", explosionRadius);
		write("FluidsProtect", waterProtection);

		write("EnabledFor.TNT", tntEnabled);
		write("EnabledFor.Cannons", cannonsEnabled);
		write("EnabledFor.Creepers", creepersEnabled);
		write("EnabledFor.Ghasts", ghastsEnabled);

		write("Durability.Enabled", durabilityEnabled);
		write("Durability.Amount", durability);
		write("Durability.ResetEnabled", durabilityTimerEnabled);
		write("Durability.ResetAfter", "" + durabilityTime);

		write("Blocks.ChanceToDrop", chanceToDropBlock);

		loadData();
	}

	private void write(String key, Object o) {
		try {
			bukkitConfig.load(configFile);
			bukkitConfig.set(key, o);
			bukkitConfig.save(configFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private long readLong(String key, String def) {
		try {
			bukkitConfig.load(configFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Bukkit Config has no getLong(..)-method, so we are using Strings
		String value = bukkitConfig.getString(key, def);

		long tmp = 0;

		try {
			tmp = Long.parseLong(value);
		} catch (NumberFormatException nfe) {
			Log.warning("Error parsing a long from the config file. Key=" + key);
			nfe.printStackTrace();
		}

		return tmp;
	}
	public int getRadius() {
		return explosionRadius;
	}
	
	public boolean getTntEnabled() {
		return tntEnabled;
	}
	
	public boolean getCannonsEnabled() {
		return cannonsEnabled;
	}
	
	public boolean getCreepersEnabled() {
		return creepersEnabled;
	}
	
	public boolean getGhastsEnabled() {
		return ghastsEnabled;
	}

	public boolean getDurabilityEnabled() {
		return durabilityEnabled;
	}

	public int getDurability() {
		return durability;
	}

	public boolean getDurabilityResetTimerEnabled() {
		return durabilityTimerEnabled;
	}

	public long getDurabilityResetTime() {
		return durabilityTime;
	}

	public double getChanceToDropBlock() {
		return chanceToDropBlock;
	}
	
	public boolean getWaterProtection() {
		return waterProtection;
	}

	public String[] printLoadedConfig() {
		return new String[] { "this doesn't work." };
	}

	public File getConfigFile() {
		return configFile;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void saveDurabilityToFile() {
		if (plugin.getListener() == null || plugin.getListener().getObsidianDurability() == null) {
			return;
		}

		HashMap<Integer, Integer> map = plugin.getListener().getObsidianDurability();

		new File(directory).mkdir();

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(durabilityFile));
			oos.writeObject(map);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			Log.severe("Failed writing obsidian durability for " + AcerObbyDestroyer.getPluginName());
			e.printStackTrace();
		}
	}
	
	public void saveEnchantDurability() {
		if (plugin.getListener() == null || plugin.getListener().getEnchantmentDurability() == null) {
			return;
		}
		
		HashMap<Integer, Integer> map = plugin.getListener().getEnchantmentDurability();
		
		new File(directory).mkdir();
		
		try {
			ObjectOutputStream oosenchant = new ObjectOutputStream(new FileOutputStream(enchantmentDurabilityFile));
			oosenchant.writeObject(map);
			oosenchant.flush();
			oosenchant.close();
		} catch (IOException e) {
			Log.severe("Failed writing enchantment table durability for " + AcerObbyDestroyer.getPluginName());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<Integer, Integer> loadDurabilityFromFile() {
		if (!durabilityFile.exists() || plugin.getListener() == null || plugin.getListener().getObsidianDurability() == null) {
			return null;
		}

		new File(directory).mkdir();

		HashMap<Integer, Integer> map = null;
		Object result = null;

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(durabilityFile));
			result = ois.readObject();
			map = (HashMap<Integer, Integer>) result;
			ois.close();
		} catch (IOException ioe) {
			Log.severe("Failed reading obsidian durability for " + AcerObbyDestroyer.getPluginName());
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			Log.severe("Obsidian durability file contains an unknown class, was it modified?");
			cnfe.printStackTrace();
		}

		return map;
	}
}
