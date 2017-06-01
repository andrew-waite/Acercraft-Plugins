package com.acercraft.acertokens;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;


public class ConfigHandler {
	AcerTokens plugin;

	public ConfigHandler(AcerTokens plugin) {
		this.plugin = plugin;
	}

	public List<String> getPackages() {
		List<String> packages = new ArrayList<String>();
		for(String s : plugin.getConfig().getKeys(false)) {
			if(!s.equalsIgnoreCase("Mobs")) {
				packages.add(plugin.getConfig().getString(s + ".Name"));
			}
		}
		return packages;
	}

	public String getName(int ID) {
		return plugin.getConfig().getString(ID + ".Name");
	}

	public String getDescription(int ID) {
		return plugin.getConfig().getString(ID + ".Description");
	}

	public int getCost(int ID) {
		return plugin.getConfig().getInt(ID + ".Cost");
	}

	public List<String> getCommands(int ID) {
		return plugin.getConfig().getStringList(ID + ".Commands");
	}

	public int getID(String Package) {
		for(String s : plugin.getConfig().getKeys(false)) {
			if(!s.equalsIgnoreCase("Mobs")) {
				if(plugin.getConfig().getString(s + ".Name").equalsIgnoreCase(Package)) {
					return Integer.parseInt(s);
				}
			}
		}
		return (Integer) null;
	}

	public List<String> getMobsList() {
		ConfigurationSection section = plugin.getConfig().getConfigurationSection("Mobs");
		List<String> list = new ArrayList<String>();
		for(String s : section.getKeys(false)) {
			list.add(s);
		}
		return list;
	}

	public List<Integer> getMobTokens(String mob) {
		List<Integer> list = new ArrayList<Integer>();
		String amounts = plugin.getConfig().getString("Mobs." + mob + ".Amounts");
		if(amounts.contains(",")) {
			String[] allAmounts = amounts.split(",");
			for(String s : allAmounts) {
				try {
					int i = Integer.parseInt(s);
					list.add(i);
				}
				catch(NumberFormatException e) {

				}
			}
		}
		else {
			try {
				int i = Integer.parseInt(amounts);
				list.add(i);
			}
			catch(NumberFormatException e) {

			}
		}
		return list;
	}

	public String getMobChance(String mob) {
		return plugin.getConfig().getString("Mobs." + mob + ".Chance");
	}
}
