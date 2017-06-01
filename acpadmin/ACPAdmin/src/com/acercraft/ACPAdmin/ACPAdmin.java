package com.acercraft.ACPAdmin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.acercraft.ACPAdmin.Commands.ACPTicketCommand;
import com.acercraft.ACPAdmin.Commands.BanCommand;
import com.acercraft.ACPAdmin.Commands.HistoryCommand;
import com.acercraft.ACPAdmin.Commands.MuteCommand;
import com.acercraft.ACPAdmin.Commands.TempBanCommand;
import com.acercraft.ACPAdmin.Commands.UnbanCommand;
import com.acercraft.ACPAdmin.Commands.WarnCommand;
import com.acercraft.ACPAdmin.Config.ConfigHandler;
import com.acercraft.ACPAdmin.Config.MessageConfigHandler;
import com.acercraft.ACPAdmin.Listener.PlayerListener;
import com.acercraft.ACPAdmin.Util.Util;

public class ACPAdmin extends JavaPlugin 
{

	public static final Logger log = Bukkit.getLogger();
	public MessageConfigHandler messageConfig;
	public ConfigHandler config;
	public static ACPAdmin instance;
	private FileConfiguration customConfig;
	private File customConfigFile;
	public File requestFile;
	public FileConfiguration req;
	public Player[] onlinePlayers;

	public void onEnable()
	{

		instance = this;

		this.setupConfigs();

		this.registerCommands();

		this.registerEvents();

		this.config = new ConfigHandler(this);
		
		this.messageConfig = new MessageConfigHandler(this);
		
		this.req = new YamlConfiguration();
		this.requestFile = new File(getDataFolder(), "/requests.yml");

		if (!this.requestFile.exists()) {
			firstrun();
		}

		loadYaml();
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				onlinePlayers  = Bukkit.getServer().getOnlinePlayers();
				requestsalert();
			}
		}
		/* Currently set at 3mins */	, 3600L, 3600L);
	}

	public void onDisable()
	{
		instance = null;
	}

	public void setupConfigs()
	{

		if (!(new File(getDataFolder(), "config.yml").exists()))
		{
			log.warning("[ACPAdmin] ACPAdmin could not find the file 'config.yml' generating a new one now");
			saveDefaultConfig();
		}

		if (!(new File(getDataFolder(), "messages.yml").exists()))
		{
			log.warning("[ACPAdmin] ACPAdmin could not find the file 'messages.yml' generating a new one now");
			this.saveResource("messages.yml", true);
		}

	}

	public void registerCommands()
	{
		getCommand("ban").setExecutor(new BanCommand());
		getCommand("tempban").setExecutor(new TempBanCommand());
		getCommand("history").setExecutor(new HistoryCommand());
		getCommand("warn").setExecutor(new WarnCommand());
		getCommand("unban").setExecutor(new UnbanCommand());
		getCommand("mute").setExecutor(new MuteCommand());
		getCommand("modreq").setExecutor(new ACPTicketCommand(this));
		getCommand("check").setExecutor(new ACPTicketCommand(this));
		getCommand("tp-id").setExecutor(new ACPTicketCommand(this));
		getCommand("claim").setExecutor(new ACPTicketCommand(this));
		getCommand("re-open").setExecutor(new ACPTicketCommand(this));
		getCommand("status").setExecutor(new ACPTicketCommand(this));
		getCommand("done").setExecutor(new ACPTicketCommand(this));
		getCommand("hold").setExecutor(new ACPTicketCommand(this));
	}

	public void registerEvents()
	{
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(), this);
	}

	public static ACPAdmin getInstance()
	{
		return instance;
	}

	public void reloadMessageConfig()
	{
		if (this.customConfigFile == null)
		{
			this.customConfigFile = new File(getDataFolder(), "messages.yml");
		}
		this.customConfig = YamlConfiguration
				.loadConfiguration(this.customConfigFile);

		InputStream defConfigStream = getResource("messages.yml");
		if (defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			this.customConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getMessageConfig()
	{
		if (this.customConfig == null)
		{
			reloadMessageConfig();
		}
		return this.customConfig;
	}

	public void saveMessageConfig()
	{
		if ((this.customConfig == null) || (this.customConfigFile == null)) return;
		try
		{
			getMessageConfig().save(this.customConfigFile);
		}
		catch (IOException ex)
		{
			getLogger().log(Level.SEVERE,
					"Could not save config to " + this.customConfigFile, ex);
		}
	}
	
	private void firstrun() {
		PluginDescriptionFile pdfFile = getDescription();

		if (!this.requestFile.exists()) {
			try { OutputStream out = new FileOutputStream(this.requestFile);
			out.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			log.info("[" + pdfFile.getName() + "]" + " requests.yml successfully created");
		}
		saveYaml();
	}

	public void saveYaml()
	{
		try
		{
			this.req.save(this.requestFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadYaml() {
		try {
			this.req.load(this.requestFile);
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		catch (InvalidConfigurationException e1) {
			e1.printStackTrace();
		}
	}

	public void requestsalert(){
		for (int i = this.onlinePlayers.length - 1; i >= 0; i--){
			if (this.onlinePlayers[i] != null) {

				String nr = Integer.toString(getConfig().getInt("currentrequests"));
				String cr = Integer.toString(getConfig().getInt(this.onlinePlayers[i].getName()+".claimedrequests"));
				String hr = Integer.toString(getConfig().getInt(this.onlinePlayers[i].getName()+".heldrequests"));
				if(!(getConfig().getInt("currentrequests") <= 0)) { 
					if(Util.hasPerm(this.onlinePlayers[i], "nexusadmin.nag")){
						this.onlinePlayers[i].sendMessage(ChatColor.GOLD+"["+ChatColor.RED+"NexusAdmin"+ChatColor.GOLD+"]"+ 
								ChatColor.DARK_PURPLE+ " There are currently " + ChatColor.RED + nr + ChatColor.DARK_PURPLE + " tickets open");
					}

					else {

					}
				}

				if(!(getConfig().getInt(this.onlinePlayers[i].getName()+".claimedrequests") <= 0)) { 
					if(Util.hasPerm(this.onlinePlayers[i], "nexusadmin.nag")){ 
						this.onlinePlayers[i].sendMessage(ChatColor.GOLD+"["+ChatColor.RED+"NexusAdmin"+ChatColor.GOLD+"]"+ 
								ChatColor.DARK_PURPLE + " You currently have " + ChatColor.RED + cr + ChatColor.DARK_PURPLE + " claimed tickets open");
					}
				}else{

				}

				if(!(getConfig().getInt(this.onlinePlayers[i].getName()+".heldrequests") <= 0)) { 
					if(Util.hasPerm(this.onlinePlayers[i], "nexusadmin.nag")){
						this.onlinePlayers[i].sendMessage(ChatColor.GOLD+"["+ChatColor.RED+"NexusAdmin"+ChatColor.GOLD+"]"+ 
								ChatColor.DARK_PURPLE+ " You currently have " + ChatColor.RED + hr + ChatColor.DARK_PURPLE + " tickets on hold");
					}
				}else{

				} 

			}

		} 
	}
}
