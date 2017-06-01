package com.acercraft.acerothers;

public class ConfigHandler {
	AcerOthers plugin;
	
	public ConfigHandler(AcerOthers instance){
		this.plugin = instance;
	}
	
	public int getEnderpearlCooldown(){
		return this.plugin.getConfig().getInt("enderpearl_cooldown");
	}
	
}
