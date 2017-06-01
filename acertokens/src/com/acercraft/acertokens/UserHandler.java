package com.acercraft.acertokens;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class UserHandler {

	private FileConfiguration users;
	private File usersFile;

	AcerTokens plugin;
	public UserHandler(AcerTokens plugin) {
		this.plugin = plugin;
		reloadUsers();
	}
	public void reloadUsers() {
		if (usersFile == null) {
			usersFile = new File(plugin.getDataFolder(), "users.yml");
		}
		users = YamlConfiguration.loadConfiguration(usersFile);
	}

	public FileConfiguration getUsers() {
		return users;
	}

	public void saveUsers() {
		if (users == null || usersFile == null)
		{
			plugin.getLogger().severe((users == null ? "users" : "usersFile" ) + " was null, saving aborted!");
			return;
		}
		try
		{
			getUsers().save(usersFile);
		}
		catch(IOException e)
		{
			plugin.getLogger().severe("Error saving users.yml! Data may be lost!");
		}
	}

	public int getTokens(String name) {
		name = name.toLowerCase();
		if(getUsers().getString(name) == null) {
			getUsers().set(name + ".Tokens", 0);
		}
		return getUsers().getInt(name + ".Tokens");
	}
	public void addTokens(String name, int amount) {
		name = name.toLowerCase();
		int cur = getTokens(name);
		getUsers().set(name + ".Tokens", cur + amount);
	}
	public void removeTokens(String name, int amount) {
		name = name.toLowerCase();
		int cur = getTokens(name);
		getUsers().set(name + ".Tokens", cur - amount);
	}
	public List<String> getTopHolders() {
		HashMap<String, Integer> holders = new HashMap<String, Integer>();

		for(String s : getUsers().getKeys(false)) {
			holders.put(s, getTokens(s));
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
}
