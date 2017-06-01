package com.acercraft.acerpetnames;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin
{
	public static List<RenamingPlayer> rplayers = new ArrayList<RenamingPlayer>();
	public static HashMap<String, Integer> cooldowns = new HashMap<String, Integer>();

	public void onDisable()
	{
	}

	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new ListenerClass(), this);
		setupConfig();
		
		Bukkit.getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() { 
			public void run() {
				for(String s : cooldowns.keySet()) {
					cooldowns.put(s, cooldowns.get(s) - 1);
					
					if(cooldowns.get(s) == 0){
						cooldowns.remove(s);
					}
				}
			}
		}, 1*20, 1*20);
	}

	public static void rename(Entity e, String name)
	{
		((LivingEntity)e).setCustomName(name);
		((LivingEntity)e).setCustomNameVisible(true);

	}

	public void setupConfig(){
		if(! new File(getDataFolder(), "config.yml").exists()){
			getConfig().options().copyDefaults(true);
			saveConfig();
			reloadConfig();

			List<String> names = getConfig().getStringList("blacklist");
			names.add("mineacer");
			getConfig().set("blacklist", names);
			saveConfig();
			reloadConfig();
			
			getConfig().set("command_cooldown", Integer.valueOf(60));
			saveConfig();
			reloadConfig();
		}
	}

	public boolean hasPerm(Player p, String perm){
		return p.hasPermission(perm);
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player)sender;
		if (commandLabel.equalsIgnoreCase("acerpetname")) {
			if(hasPerm(player, "acerpetnames.name")){
				if(!cooldowns.containsKey(player.getName()) || cooldowns.get(player.getName()) == 0) {
					if (args.length == 0) {
						player.sendMessage(ChatColor.RED + "Use like this: /acerpetname {petname}");
						return false;
					}
					String name = "";

					int i = 0;
					for (String arg : args) {
						if (i > 0) {
							name = name + " ";
						}
						name = name + arg;
						i++;
					}

					String list = name;

					list = list.replaceAll("&0", "");
					list = list.replaceAll("&1", "");
					list = list.replaceAll("&2", "");
					list = list.replaceAll("&3", "");
					list = list.replaceAll("&4", "");
					list = list.replaceAll("&5", "");
					list = list.replaceAll("&6", "");
					list = list.replaceAll("&7", "");
					list = list.replaceAll("&8", "");
					list = list.replaceAll("&9", "");
					list = list.replaceAll("&a", "");
					list = list.replaceAll("&b", "");
					list = list.replaceAll("&c", "");
					list = list.replaceAll("&d", "");
					list = list.replaceAll("&e", "");
					list = list.replaceAll("&f", "");

					List<String> blacklist = getConfig().getStringList("blacklist");
					if(blacklist.contains(list)){
						player.sendMessage(ChatColor.RED + "You can not name your pet that");
						return false;
					}

					name = name.replaceAll("&0", ChatColor.BLACK.toString());
					name = name.replaceAll("&1", ChatColor.DARK_BLUE.toString());
					name = name.replaceAll("&2", ChatColor.DARK_GREEN.toString());
					name = name.replaceAll("&3", ChatColor.DARK_AQUA.toString());
					name = name.replaceAll("&4", ChatColor.DARK_RED.toString());
					name = name.replaceAll("&5", ChatColor.DARK_PURPLE.toString());
					name = name.replaceAll("&6", ChatColor.GOLD.toString());
					name = name.replaceAll("&7", ChatColor.GRAY.toString());
					name = name.replaceAll("&8", ChatColor.DARK_GRAY.toString());
					name = name.replaceAll("&9", ChatColor.BLUE.toString());
					name = name.replaceAll("&a", ChatColor.GREEN.toString());
					name = name.replaceAll("&b", ChatColor.AQUA.toString());
					name = name.replaceAll("&c", ChatColor.GREEN.toString());
					name = name.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString());
					name = name.replaceAll("&e", ChatColor.YELLOW.toString());
					name = name.replaceAll("&f", ChatColor.WHITE.toString());



					if (name.length() > 20) {
						player.sendMessage(ChatColor.RED + "Name too long!");
						player.sendMessage(ChatColor.RED + "Tip: Color codes count as 2 characters!");
						return false;
					}

					for (int a = 0; a < rplayers.size(); a++) {
						if (((RenamingPlayer)rplayers.get(a)).player == player) {
							rplayers.remove(a);
						}
					}
					rplayers.add(new RenamingPlayer(player, name));
					player.sendMessage(ChatColor.GREEN + "Please right click your pet!");
					
					cooldowns.put(player.getName(), getConfig().getInt("command_cooldown")); // The 60 is seconds until they can use it again
				}
				else{
					player.sendMessage(ChatColor.RED + "Please wait " + cooldowns.get(player.getName()) + ChatColor.RED + " seconds untill you can use this command again");
				}
			}else{
				player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			}
		}

		return false;
	}
}