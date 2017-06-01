package com.acercraft.ACPAdmin.Config;

import com.acercraft.ACPAdmin.ACPAdmin;
import com.acercraft.ACPAdmin.Util.Util;

public class MessageConfigHandler {
	
	private ACPAdmin plugin;
	
	public MessageConfigHandler(ACPAdmin instance){
		plugin = instance;
	}

	public String getNoPerms(){
		return Util.parseColorCodes(this.plugin.getMessageConfig().getString("No_Permission"));
	}
}
