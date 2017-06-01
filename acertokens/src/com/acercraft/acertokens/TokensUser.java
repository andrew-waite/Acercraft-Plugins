package com.acercraft.acertokens;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TokensUser {
	private AcerTokens plugin = AcerTokens.getInstance();
	private String name;
	private FileConfiguration yml;
	private File file;
	public TokensUser(String name) {
		this.name = name;
		file = new File("plugins/AcerTokens/users/" + name + ".yml");
		loadFromFile();
	}

	public void loadFromFile() {
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			}
			catch(IOException e) {
				Bukkit.getLogger().severe("Failed to save log file for: " + name);
				e.printStackTrace();
			}
		}
		yml = YamlConfiguration.loadConfiguration(file);
	}

	public void saveToFile() {
		try {
			yml.save(file);
		}
		catch(IOException e) {
			plugin.getLogger().severe("Error saving log.yml for " + name + "! Data may be lost!");
		}
	}

	public int getNextOpenValue() {
		boolean found = false;
		int open = 1;
		for(int i = open; !found; i++) {
			if(yml.getConfigurationSection("" + i) == null) {
				open = i;
				found = true;
			}
		}
		return open;
	}

	public String getDate() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter= 
				new SimpleDateFormat("MMM dd");
		return formatter.format(currentDate.getTime());
	}

	public void logSpend(int amount, String packageName) {
		int id = getNextOpenValue();
		yml.set(id + ".date", getDate());
		yml.set(id + ".action", "spent");
		yml.set(id + ".amount", amount);
		yml.set(id + ".name", packageName);
		saveToFile();
	}

	public void logTrade(int amount, String with) {
		int id = getNextOpenValue();
		yml.set(id + ".date", getDate());
		yml.set(id + ".action", "sent");
		yml.set(id + ".amount", amount);
		yml.set(id + ".name", with);
		saveToFile();
	}

	public void logTradedTo(int amount, String from) {
		int id = getNextOpenValue();
		yml.set(id + ".date", getDate());
		yml.set(id + ".action", "traded");
		yml.set(id + ".amount", amount);
		yml.set(id + ".name", from);
		saveToFile();
	}

	public void logGive(int amount, String from) {
		int id = getNextOpenValue();
		yml.set(id + ".date", getDate());
		yml.set(id + ".action", "given");
		yml.set(id + ".amount", amount);
		yml.set(id + ".name", from);
		saveToFile();
	}

	public void logRemove(int amount, String by) {
		int id = getNextOpenValue();
		yml.set(id + ".date", getDate());
		yml.set(id + ".action", "removed");
		yml.set(id + ".amount", amount);
		yml.set(id + ".name", by);
		saveToFile();
	}

	public void logGiveTo(int amount, String who) {
		int id = getNextOpenValue();
		yml.set(id + ".date", getDate());
		yml.set(id + ".action", "gave to");
		yml.set(id + ".amount", amount);
		yml.set(id + ".name", who);
		saveToFile();
	}

	public void logRemoveFrom(int amount, String who) {
		int id = getNextOpenValue();
		yml.set(id + ".date", getDate());
		yml.set(id + ".action", "remove");
		yml.set(id + ".amount", amount);
		yml.set(id + ".name", who);
		saveToFile();
	}

	public int getAmountOfPages() {
		int highest = 1;
		boolean found = false;
		for(int i = 1; !found; i++) {
			if(yml.getConfigurationSection("" + (i + 1)) == null) {
				highest = i;
				found = true;
			}
		}
		int pages = highest / 5;
		if(highest % 5 != 0) pages++;
		return pages;
	}

	public List<String> getPage(int page) {
		int highest = 0;
		boolean found = false;
		for(int i = 0; !found; i++) {
			if(yml.getConfigurationSection("" + (i + 1)) == null) {
				highest = i;
				found = true;
			}
		}
		int first = (page - 1) * 5;
		int last = (page * 5) - 1;
		List<String> entries = new ArrayList<String>();
		entries.add(plugin.prefix + "Page " + page + " of " + getAmountOfPages() + " for " + name + ":");
		for(int i = first;i <= last; i++) {
			if(yml.getConfigurationSection("" + (highest - i)) != null) {
				entries.add(parseString(yml.getConfigurationSection("" + (highest - i))));
			}
		}
		return entries;
	}

	public String parseString(ConfigurationSection entry) {
		int display = (getNextOpenValue() - Integer.parseInt(entry.getName()));
		String s = "" + ChatColor.RED + display + ". ";
		s = s + ChatColor.AQUA + entry.getString("date") + ": ";
		switch (entry.getString("action")) {
		case "spent":
			s = s + ChatColor.GREEN + "Spent " + entry.getInt("amount") + " on " + entry.getString("name");
			break;
		case "sent":
			s = s + ChatColor.GREEN + "Sent " + entry.getInt("amount") + " to " + entry.getString("name");
			break;
		case "traded":
			s = s + ChatColor.GREEN + "Received " + entry.getInt("amount") + " from " + entry.getString("name");
			break;
		case "given":
			s = s + ChatColor.GREEN + "Given " + entry.getInt("amount") + " by " + entry.getString("name");
			break;
		case "removed":
			s = s + ChatColor.GREEN + "Had " + entry.getInt("amount") + " removed by " + entry.getString("name");
			break;
		case "gave to":
			s = s + ChatColor.GREEN + "Gave " + entry.getInt("amount") + " to " + entry.getString("name");
			break;
		case "remove":
			s = s + ChatColor.GREEN + "Removed " + entry.getInt("amount") + " from " + entry.getString("name");
			break;
		default:
			s = s + ChatColor.GREEN + "Unknown event!";
			System.out.println(s);
			break;
		}
		return s;
	}
}
