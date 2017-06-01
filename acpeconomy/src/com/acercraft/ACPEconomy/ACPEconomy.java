package com.acercraft.ACPEconomy;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ACPEconomy extends JavaPlugin{
    
    public final Logger log = Bukkit.getLogger();
    public static ACPEconomy instance;
    
    public void onEnable()
    {
	instance = this;
    }
    
    public void onDsiable()
    {
	instance = null;
    }
    
    public static ACPEconomy getInstance()
    {
	return instance;
    }    
}
