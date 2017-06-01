package com.acercraft.AcerToggleChat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AcerToggleChat extends JavaPlugin implements Listener {
	String prefix;
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		String[] default1 = {};
		getConfig().addDefault("Players", Arrays.asList(default1));
		prefix = getConfig().getString("Prefix");
		prefix = ChatColor.translateAlternateColorCodes('&', prefix);
	}

	public void addPlayer(String name) {
		reloadConfig();
		if(getConfig().getStringList("Players") == null) {
			String[] default1 = {};
			getConfig().set("Players", Arrays.asList(default1));
		}
		List<String> players = getConfig().getStringList("Players");
		if(!players.contains(name)) {
			players.add(name);
			getConfig().set("Players", players);
		}
		saveConfig();
	}

	public void removePlayer(String name) {
		reloadConfig();
		if(getConfig().getStringList("Players") == null) {
			String[] default1 = {};
			getConfig().set("Players", Arrays.asList(default1));
		}
		List<String> players = getConfig().getStringList("Players");
		if(players.contains(name)) {
			players.remove(name);
			getConfig().set("Players", players);
		}
		saveConfig();
	}

	public boolean isInList(String name) {
		reloadConfig();
		if(getConfig().getStringList("Players") == null || !getConfig().getStringList("Players").contains(name)) {
			return false;
		}
		return true;
	}

	public List<String> getPlayers() {
		reloadConfig();
		if(getConfig().getStringList("Players") == null) {
			String[] default1 = {};
			getConfig().set("Players", Arrays.asList(default1));
		}
		List<String> players = getConfig().getStringList("Players");
		saveConfig();
		return players;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent event) {
		Set<Player> toBeRemoved = new HashSet<Player>();
		for(Player p : event.getRecipients()) {
			String name = p.getName();
			if(isInList(name)) {
				toBeRemoved.add(p);
			}
		}
		for(Player p : toBeRemoved) {
			event.getRecipients().remove(p);
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if(isInList(event.getPlayer().getName())) {
			removePlayer(event.getPlayer().getName());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("togglechat") || cmd.getName().equalsIgnoreCase("tc")) {
			if(args.length == 0) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					if(isInList(player.getName())) {
						removePlayer(player.getName());
						String message = "&aYou are now recieving chat again!";
						message = ChatColor.translateAlternateColorCodes('&', message);
						sender.sendMessage(prefix + message);
					}
					else {
						addPlayer(player.getName());
						String message = "&aYou will no longer recieve chat!";
						message = ChatColor.translateAlternateColorCodes('&', message);
						sender.sendMessage(prefix + message);
					}
				}
			}
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("list")) {
					if(sender.hasPermission("togglechat.list")) {
						StringBuilder players = new StringBuilder();
						for(String name : getPlayers()) {
							Player p = getServer().getPlayer(name);
							String dName = p.getDisplayName();
							players.append(dName + ", ");
						}
						if(!players.toString().isEmpty()) {
							String message = "&aPlayers with chat disabled:";
							message = ChatColor.translateAlternateColorCodes('&', message);
							sender.sendMessage(prefix + message);
							String list = players.toString().trim(); // Will be left with just a comma on end
							String list1 = list.substring(0, list.length() - 1); // Will remove comma
							sender.sendMessage(list1);
						}
						else {
							String message = "&aNo players have chat disabled";
							message = ChatColor.translateAlternateColorCodes('&', message);
							sender.sendMessage(prefix + message);
						}
					}
					else {
						String message = "&aYou do not have permission to do this!";
						message = ChatColor.translateAlternateColorCodes('&', message);
						sender.sendMessage(prefix + message);
					}
				}
				else {
					if(sender.hasPermission("togglechat.others")) {
						OfflinePlayer player = getServer().getOfflinePlayer(args[0]);
						if(player.isOnline()) {
							if(isInList(player.getName())) {
								removePlayer(player.getName());
								String message = "&a" + player.getName() + " will now recieve chat!";
								message = ChatColor.translateAlternateColorCodes('&', message);
								sender.sendMessage(prefix + message);
							}
							else {
								addPlayer(player.getName());
								String message = "&a" + player.getName() + " will no longer recieve chat!";
								message = ChatColor.translateAlternateColorCodes('&', message);
								sender.sendMessage(prefix + message);
							}
						}
						else {
							String message = "&aThat player is not online!";
							message = ChatColor.translateAlternateColorCodes('&', message);
							sender.sendMessage(prefix + message);
						}
					}
					else {
						String message = "&aYou do not have permission to do this!";
						message = ChatColor.translateAlternateColorCodes('&', message);
						sender.sendMessage(prefix + message);
					}
				}
			}
			else {
				String message = "&aUnknown AcerToggleChat command!";
				message = ChatColor.translateAlternateColorCodes('&', message);
				sender.sendMessage(prefix + message);
			}
		}
		return true;
	}
}
