package com.acercraft.ACPEssentials;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.acercraft.ACPEssentials.Listener.PlayerJoinListener;

public class ACPEssentials extends JavaPlugin{
	
	public static final Logger log = Bukkit.getLogger();
	private static ACPEssentials instance;
	
	public void onEnable(){
		
		instance = this;
		this.registerListeners();
	}
	
	public void onDisable(){
		
	}
	
	public void registerListeners(){
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new PlayerJoinListener(), this);
	}
	
	public void registerCommands(){
	}
	
	public static ACPEssentials getInstance(){
		return instance;
	}
	
	

}
