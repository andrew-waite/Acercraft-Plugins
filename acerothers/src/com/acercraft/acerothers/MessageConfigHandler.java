package com.acercraft.acerothers;

public class MessageConfigHandler {
	
	AcerOthers plugin;
	
	public MessageConfigHandler(AcerOthers instance){
		this.plugin = instance;
	}
	
	public String getPrefix(){
		return this.plugin.getMessageConfig().getString("prefix");
	}
	
	public String getEnderPearlFail(int time){
		String message = this.plugin.getMessageConfig().getString("Enderpearl_fail");
		String time1 = String.valueOf(time);
		message = message.replaceAll("%timeleft%", time1);
		return message;
	}
	
	public String getCommandCooldownError(long time){
		String message = this.plugin.getMessageConfig().getString("CommandCooldownError");
		String time1 = String.valueOf(time);
		message = message.replaceAll("%timeleft%", time1);
		return message;
		
	}

}
