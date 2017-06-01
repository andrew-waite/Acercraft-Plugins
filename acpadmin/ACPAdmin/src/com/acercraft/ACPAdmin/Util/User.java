package com.acercraft.ACPAdmin.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.acercraft.ACPAdmin.ACPAdmin;

public class User {

	private String name;
	private FileConfiguration config;
	private File file;
	private ACPAdmin plugin = ACPAdmin.getInstance();
	
	public User(String name)
	{
	    this.name = name;
	    this.file = new File("plugins/ACPEssentials/userdata/"+name+".yml");
	    this.loadFromFile();
	    addFirstDetails();
	}
	
	public long getBanTimeStamp()
	{
		return this.config.getLong("Ban_TimeStamp");
	}
	
	public void setBanTimeStamp(long time)
	{
		this.config.set("Ban_TimeStamp", time);
		this.saveToFile();
	}
	
	public void setWarnReason(String reason)
	{
	    List<String> warnings = this.getAllWarnings();
	    warnings.add(reason);
	    this.config.set("Warnings", warnings);
	    this.saveToFile();
	}
	
	public void setBanType(BanType bantype)
	{
	    this.config.set("BanType", bantype.toString());
	    this.saveToFile();
	}
	
	public String getBanType()
	{
	    return this.config.getString("BanType");
	}
	public List<String> getAllWarnings()
	{
	    return this.config.getStringList("Warnings");
	}
	
	public int getNumberOfTimesBanned()
	{
	    return this.config.getInt("Number_Of_Times_Banned");
	}
	
	public int getNumberOfWarnings()
	{
	    return this.config.getInt("Number_Of_Times_Warned");
	}
	
	public void addWarning()
	{
	    int totalWarnings = this.getNumberOfWarnings();
	    this.config.set("Number_Of_Times_Warned", totalWarnings + 1);
	    this.saveToFile();
	}
	
	public void addBan()
	{
	    int totalBans = this.getNumberOfTimesBanned();
	    this.config.set("Number_Of_Times_Banned", totalBans + 1);
	    this.saveToFile();
	}
	
	public void setBanReason(String banReason)
	{
	    this.config.set("Ban_Reason", banReason);
	    this.saveToFile();
	}
	
	public String getBanReason()
	{
	    return this.config.getString("Ban_Reason");
	}
	
	public boolean isPlayerBanned(String player)
	{
	    if(Bukkit.getServer().getOfflinePlayer(player).isBanned())
	    {
		return true;
	    }
	    return false;
	}
	
	public void kickPlayer(String reason)
	{
	    Player player = Bukkit.getServer().getPlayer(this.name);
	    player.kickPlayer(reason);
	}
	
	public void banPlayer(String reason, CommandSender sender)
	{
	    Player player = Bukkit.getServer().getPlayer(name);
	    
	    if (player != null)
	    {
		player.kickPlayer(reason);
	    	player.setBanned(true);
	    	addBan();
	    	setWarnReason(reason);
	    	LogUtil.logInfo("[NoCheatPlus] (" + sender.getName() + ") Banned " + player.getName() + " : " + reason);
	    }
	    else
	    {
	    	OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(name);
	    	offlinePlayer.setBanned(true);
	    	addBan();
	    	setWarnReason(reason);
	    	LogUtil.logInfo("[NoCheatPlus] (" + sender.getName() + ") Banned " + offlinePlayer.getName() + " : " + reason);
	    }
	    
	}

	public void warnPlayer(String warnReason, Player warnee, String colorWarnReason)
	{
	    Player player = Bukkit.getServer().getPlayer(name);
	    
	    if (player != null)
	    {
		player.sendMessage(warnee.getName() + " Warned you for: " + warnReason);
		this.setWarnReason(colorWarnReason);
		this.addWarning();
	    }
	    else
	    {
		this.setWarnReason(colorWarnReason);
		this.addWarning();
	    }
	}
	
	public void mutePlayer(long time)
	{
	    this.config.set("Muted.boolean", true);
	    this.saveToFile();
	    this.setMuteTime(time);
	}
	
	public void unMutePlayer()
	{
	    this.config.set("Muted.boolean", false);
	    this.saveToFile();
	    this.setMuteTime(0L);
	}
	
	public void setMuteTime(long time)
	{
	    this.config.set("Muted.time", time);
	    this.saveToFile();
	}
	
	public long getMuteTime()
	{
	    return this.config.getLong("Muted.Time");
	}
	
	public boolean isMuted()
	{
	    if(this.config.getBoolean("Muted.boolean") == true)
	    {
		return true;
	    }
	    
	    return false;
	}
	
	public void unBanPlayer()
	{
	    OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(name);
	    offlinePlayer.setBanned(false);
	    setBanTimeStamp(0L);
	    this.setBanType(BanType.Not_Banned);
	}
	
	public void sendMessage(String message)
	{
	    Player player = Bukkit.getServer().getPlayer(this.name);
	    player.sendMessage(message);
	}
	public void kickPlayerPlayerListener()
	{	
		if(this.getBanType().equals(BanType.Ban))
		{
		    this.kickPlayer(this.getBanReason());
		}
		
		if(this.getBanType().equals(BanType.Temp_Ban))
		{
		    if(this.getBanTimeStamp() == 0L)
		    {
			this.unBanPlayer();
			this.setBanTimeStamp(0L);
			this.setBanType(BanType.Not_Banned);
		    }
		    else
		    {
			 this.kickPlayer(this.getBanReason() + Util.formatDateDiff(this.getBanTimeStamp()));
		    }

		}
	}
	
	public String getName()
	{
	    return this.name;
	}
	
	public void addFirstDetails()
	{
	    if(!this.file.exists())
	    {
	    this.config.set("Ban_TimeStamp", 0);
	    this.config.set("Number_Of_Times_Banned", 0);
	    this.config.set("Number_Of_Times_Warned", 0);
	    this.config.set("Warnings", "");
	    this.config.set("Ban_Reason", "");
	    this.config.set("BanType", "");
	    this.config.set("Muted.boolean", false);
	    this.config.set("Muted.time", 0L);
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
		        this.file.createNewFile();
		      }
		      catch (IOException e) {
		        e.printStackTrace();
		      }
		    }
		    this.config = YamlConfiguration.loadConfiguration(this.file);
	  }

}
