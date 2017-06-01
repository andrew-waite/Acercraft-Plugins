package com.acercraft.ACPAdmin.Config;

import com.acercraft.ACPAdmin.ACPAdmin;

public class ConfigHandler {
	
	private ACPAdmin plugin;
	
	public ConfigHandler(ACPAdmin instance){
		this.plugin = instance;
	}
	
	public int getNumber(){
		return this.plugin.getConfig().getInt("something");
	}
}
