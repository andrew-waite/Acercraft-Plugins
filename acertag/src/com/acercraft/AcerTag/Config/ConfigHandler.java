package com.acercraft.AcerTag.Config;

import java.util.List;

import com.acercraft.AcerTag.AcerTag;
import com.acercraft.AcerTag.Util.Util;

public class ConfigHandler {
	
	AcerTag plugin;
	
	public ConfigHandler(AcerTag instance){
		this.plugin = instance;
	}
	
	public String getMessagePrefix(){
		return Util.parseColors(this.plugin.getConfig().getString("Message_Prefix"));
	}
	
	public String getRulesLineOne(){
		return Util.parseColors(this.plugin.getConfig().getString("NickNameRules.LineOne"));
	}
	
	public String getRulesLineTwo(){
		return Util.parseColors(this.plugin.getConfig().getString("NickNameRules.LineTwo"));
	}
	
	public String getRulesLineThree(){
		return Util.parseColors(this.plugin.getConfig().getString("NickNameRules.LineThree"));
	}
	
	public String getRulesLineFour(){
		return Util.parseColors(this.plugin.getConfig().getString("NickNameRules.LineFour"));
	}
	
	public List<String> getBlackList(){
		return this.plugin.getConfig().getStringList("Blacklist");
		
	}
	
	public int getMinimumNickNameLength(){
		return this.plugin.getConfig().getInt("MinimumNickNameLength");
	}
	
	public int getMaximumNickNameLength(){
		return this.plugin.getConfig().getInt("MaximumNickNameLength");
	}

}
