package com.acercraft.ACPEconomy.Util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.acercraft.ACPEconomy.ACPEconomy;

public class User {

	private String name;
	private FileConfiguration config;
	private File file;
	private ACPEconomy plugin = ACPEconomy.getInstance();
	
	public User(String name)
	{
	    this.name = name;
	    this.file = new File("plugins/ACPEssentials/userdata/"+name+".yml");
	    this.loadFromFile();
	    addFirstDetails();
	}
	
	public String getName()
	{
	    return this.name;
	}
	
	public double getBalance()
	{
	    return this.config.getDouble("Balance");
	}
	
	public void addMoney(User user, double ammount)
	{
	    this.config.set("Balance", user.getBalance() + ammount);
	}
	
	public void withDrawMoney(User user, double ammount)
	{
	    this.config.set("Balance", user.getBalance() - ammount);
	}
	
	public void setMoney(double ammount)
	{
	    this.config.set("Balance", ammount);
	}
	public void payPlayer(User user, double ammount, String originalPlayer)
	{
	    this.config.set("Balance", user.getBalance() + ammount);
	    User original = new User(originalPlayer);
	    original.config.set("Balance", original.getBalance() - ammount);
	}
	
	public void addFirstDetails()
	{
	    if(!this.file.exists())
	    {
	    this.config.set("Balance", 0);
	    this.saveToFile();
	    }
	}

	public void saveToFile() 
	{
		try {
			this.config.save(this.file);
		}
		catch (IOException e) {
			this.plugin.getLogger().severe("Error saving user data for " + this.name);
		}
	}
	
	  public void loadFromFile() 
	  {
		    if (!this.file.exists()) {
		      this.file.getParentFile().mkdirs();
		      try {
			  this.plugin.log.info(this.name + " config file not found at /plugins/ACPEssentials/"+this.name+".yml");
			  this.plugin.log.info("Creating one now at " + "/plugins/ACPEssentials/"+this.name+".yml");
		        this.file.createNewFile();
			  this.plugin.log.info("File successfully created");
		      }
		      catch (IOException e) {
		        e.printStackTrace();
		      }
		    }
		this.config = YamlConfiguration.loadConfiguration(this.file);
	  }

}
