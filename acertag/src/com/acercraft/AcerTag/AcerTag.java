package com.acercraft.AcerTag;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.acercraft.AcerTag.Commands.NickNameCommand;
import com.acercraft.AcerTag.Commands.PrefixSuffixCommand;
import com.acercraft.AcerTag.Config.ConfigHandler;

public class AcerTag extends JavaPlugin
{
	public final Logger log = Bukkit.getLogger();
	private static Chat chat = null;
	public ConfigHandler config;

	public void onEnable() {
		setupChat();
		setupConfig();
		getCommand("acertag").setExecutor(new PrefixSuffixCommand(this));
		getCommand("acernick").setExecutor(new NickNameCommand(this));
		this.config = new ConfigHandler(this);
		log.info("[AcerTag] AcerTag has been loaded successfully");
		
	}

	public void onDisable() {
		log.info("[AcerTag] AcerTag has been disabled successfully");
	}
	



	private void setupConfig() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
	}

	private boolean setupChat()
	{
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
		if (chatProvider != null) {
			chat = (Chat)chatProvider.getProvider();
		}

		return chat != null;
	}

}