package com.acercraft.AcerPerks;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.acercraft.AcerPerks.commands.AcerFeedCommand;
import com.acercraft.AcerPerks.commands.AcerHealCommand;
import com.acercraft.AcerPerks.commands.AcerSpeedCommand;
import com.acercraft.AcerPerks.commands.AcerStrengthCommand;
import com.acercraft.AcerPerks.commands.ToggleInventoryCommand;
import com.acercraft.AcerPerks.config.ConfigHandler;
import com.acercraft.AcerPerks.inventory.InventoryManager;

public class AcerPerks extends JavaPlugin {
	
	public static final Logger log = Bukkit.getLogger();
	public ConfigHandler config;
	protected InventoryManager inventoryManager;
	
	public static AcerPerks instance;
	
	public void onEnable()
	{
		//Class constructor declarations
		this.config = new ConfigHandler(this);
		
		this.inventoryManager = new InventoryManager(this);
		
		instance = this;
		
		//Startup methods
		registerCommands();
		
		setupConfigs();
	}
	
	public void onDisbale()
	{
		
	}
	
	
	public void registerCommands()
	{
		getCommand("acerfeed").setExecutor(new AcerFeedCommand());
		getCommand("acerheal").setExecutor(new AcerHealCommand());
		getCommand("acerspeed").setExecutor(new AcerSpeedCommand());
		getCommand("acerstrength").setExecutor(new AcerStrengthCommand());
		getCommand("ti").setExecutor(new ToggleInventoryCommand(this, this.inventoryManager));
		getCommand("it").setExecutor(new ToggleInventoryCommand(this, this.inventoryManager));
	}
	
	public void setupConfigs()
	{
		/**
		 * Inventory config
		 */
	    File spInvFile = this.inventoryManager.getDefaultSpecialInventoryFile();
	    if (!spInvFile.exists()) {
	      saveResource(spInvFile.getName(), false);
	    }
	    
	    /**
	     * Main config file
	     */
	    
	    saveDefaultConfig();

	    getConfig().options().copyDefaults(true);
	    saveConfig();
	}
	
	public static AcerPerks getInstance()
	{
		return instance;
	}
	
	public ConfigHandler configHandler()
	{
		return this.config;
	}
}