package com.acercraft.AcerPerks.config;

import com.acercraft.AcerPerks.AcerPerks;

public class ConfigHandler {
	
	private AcerPerks plugin;
	
	public ConfigHandler(AcerPerks plugin)
	{
		this.plugin = plugin;
	}

	public String getPrefix()
	{
		return this.plugin.getConfig().getString("Messages.PluginPrefix");
	}
}
