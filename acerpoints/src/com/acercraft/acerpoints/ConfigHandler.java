package com.acercraft.acerpoints;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.skills.utilities.SkillType;
import com.gmail.nossr50.util.Users;

public class ConfigHandler {

	public FileConfiguration mobs;
	public File mobsFile;
	public AcerPoints AP;
	public ConfigHandler(AcerPoints AP) {
		this.AP = AP;
	}
	
	public void addPoints(String name, int amount) {
		AP.reloadConfig();
		name = name.toLowerCase();
		if(!AP.getConfig().contains(name)) {
			AP.getConfig().set(name, 0);
		}
		int curPoints = AP.getConfig().getInt(name);
		int newPoints = curPoints + amount;
		AP.getConfig().set(name, newPoints);
		AP.saveConfig();
	}
	
	public void subPoints(String name, int amount) {
		AP.reloadConfig();
		name = name.toLowerCase();
		if(!AP.getConfig().contains(name)) {
			AP.getConfig().set(name, 0);
		}
		int curPoints = AP.getConfig().getInt(name);
		int newPoints = curPoints - amount;
		AP.getConfig().set(name, newPoints);
		AP.saveConfig();
	}
	
	public int getPoints(String name) {
		AP.reloadConfig();
		name = name.toLowerCase();
		if(!AP.getConfig().contains(name)) {
			AP.getConfig().set(name, 0);
		}
		int points = AP.getConfig().getInt(name);
		return points;
	}
	
	public String getRedeemedRank(String name) {
		AP.reloadConfig();
		name = name.toLowerCase();
		if(AP.getConfig().getString("Has_Redeemed." + name) != null) {
			return AP.getConfig().getString("Has_Redeemed." + name);
		}
		return null;
	}
	
	public int getPointsForGroup(String group) {
		AP.reloadConfig();
		group = group.toLowerCase();
		if(AP.getConfig().get("Groups." + group) == null) return -1;
		int amount = AP.getConfig().getInt("Groups." + group);
		return amount;
	}
	
	public String getBannedSkillMessage(){
		return this.parseColors(this.AP.getConfig().getString("Banned_Skill_Message"));
	}
	
	public List<String> getAllHolders() {
			AP.reloadConfig();
			HashMap<String, Integer> holders = new HashMap<String, Integer>();

			for(String s : AP.getConfig().getKeys(false)) {
				if(s.equalsIgnoreCase("Mobs")) continue; // Safety check from old config versions
				if(s.equalsIgnoreCase("Groups")) continue; // Same as above but from current config
				if(s.equalsIgnoreCase("Has_Redeemed")) continue; // Same as above
				holders.put(s, getPoints(s));
			}
			
			@SuppressWarnings("unchecked")
			final HashMap<String, Integer> clone = (HashMap<String, Integer>) holders.clone();

			List<String> players = new ArrayList<String>(clone.keySet());
			Collections.sort(players, new Comparator<String>() {
			    @Override
			    public int compare(String s1, String s2) {
			        Integer tokens1 = clone.get(s1);
			        Integer tokens2 = clone.get(s2);
			        return - tokens1.compareTo(tokens2);
			    }
			});
			return players;
	}
	
	public void resetMcMMO() {
		for(String s : Users.getPlayers().keySet()) {
			for(SkillType skill : SkillType.class.getEnumConstants()) {
				Player p = Users.getPlayer(s).getPlayer();
				ExperienceAPI.setLevel(p, skill.name(), 0);
			}
		}
	}
	
	public String parseColors(String string){
		return ChatColor.translateAlternateColorCodes('&', string);
	}
}
