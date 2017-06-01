package com.acercraft.ACPAdmin.Config;

import com.acercraft.ACPAdmin.ACPAdmin;

public enum Message {
	
	NO_PERM(ACPAdmin.getInstance().messageConfig.getNoPerms());
	
	
	private String message;
	private Message(String string){
		this.message = string;
	}
	
	public String toString(){
		return message;
	}

}


