package com.acercraft.ACPEssentials.API;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.acercraft.ACPEssentials.ACPEssentials;

public class User {

	private String name;
	private FileConfiguration config;
	private File file;
	private ACPEssentials plugin = ACPEssentials.getInstance();

	public User(String user){
		this.name = user;
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public long getBanTimeStamp(){
		return this.config.getLong("Ban_TimeStamp");
	}
	
	public void setBanTimeStamp(long time){
		this.config.set("Ban_TimeStamp", time);
		this.saveToFile();
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(name);
	}
	

	public void saveToFile() {
		try {
			this.config.save(this.file);
		}
		catch (IOException e) {
			this.plugin.getLogger().severe("Error saving user data for " + this.name);
		}
	}

}
