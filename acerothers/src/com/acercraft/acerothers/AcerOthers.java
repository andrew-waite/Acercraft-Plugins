package com.acercraft.acerothers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.Essentials;

public class AcerOthers extends JavaPlugin{
	
	public static final Logger log = Bukkit.getLogger();
	public HashMap<String, Integer> cooldowns = new HashMap<String, Integer>();
	private FileConfiguration customConfig;
	private File customConfigFile;
	public ConfigHandler config;
	public MessageConfigHandler messages;
	public Economy economy = null;
	public  Permission permission = null;
	public Essentials ess = null;
	public static final Logger logger = Bukkit.getLogger();
	 public static AcerOthers main;
	
	public void onEnable(){
		
		main = this;
		
		this.config = new ConfigHandler(this);
		this.messages = new MessageConfigHandler(this);
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(this), this);
		    
		
		Bukkit.getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() { 
			public void run() {
				for(String s : cooldowns.keySet()) {
					cooldowns.put(s, cooldowns.get(s) - 1);
					if(cooldowns.get(s) == 0){
						cooldowns.remove(s);
					}
				}
			}
		}, 1*20, 1*20);

		
		Plugin essentials = getServer().getPluginManager().getPlugin("Essentials");

		if(essentials != null){
			ess = (Essentials) essentials;
		}
		else{
			System.out.print("Essentials not found");
		}
		
		setupConfig();
		setupMessageConfig();
		setupEconomy();
		setupPermissions();
		registerCommands();
	}
	
	public void onDisable(){}
	
	public void registerCommands(){
		getCommand("acerresident").setExecutor(new AcerResidentCommand(this));
		getCommand("acerothers").setExecutor(new AcerReload(this));
	}
	
	public void setupConfig(){
		if(!(new File(getDataFolder(), "config.yml").exists())){
			saveDefaultConfig();
		}
	}
	
	public void setupMessageConfig(){
		if(!(new File(getDataFolder(), "messages.yml").exists())){
			this.saveResource("messages.yml", true);
		}
	}
	
	public void reloadMessageConfig() {
		if (this.customConfigFile == null) {
			this.customConfigFile = new File(getDataFolder(), "messages.yml");
		}
		this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

		InputStream defConfigStream = getResource("messages.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = 
					YamlConfiguration.loadConfiguration(defConfigStream);
			this.customConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getMessageConfig() {
		if (this.customConfig == null) {
			reloadMessageConfig();
		}
		return this.customConfig;
	}

	public void saveMessageConfig() {
		if ((this.customConfig == null) || (this.customConfigFile == null))
			return;
		try
		{
			getMessageConfig().save(this.customConfigFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, 
					"Could not save config to " + this.customConfigFile, ex);
		}
	}
	
	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			economy = (Economy)economyProvider.getProvider();
		}
		return economy != null;
	}
	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			permission = (Permission)permissionProvider.getProvider();
			log.info("[LocketteAddon] Found permissions plugin " + permission.getName());
		}
		return permission != null;
	}
}
