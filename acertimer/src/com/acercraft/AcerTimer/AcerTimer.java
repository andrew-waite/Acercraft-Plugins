package com.acercraft.AcerTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;

public class AcerTimer extends JavaPlugin implements Listener {

	HashMap<String, Integer> protectedPlayers = new HashMap<String, Integer>();
	ArrayList<String> pausedPlayers = new ArrayList<String>();

	String prefix;
	String rejoinMessage;
	String protectedMessage;
	String underProtectionMessage;

	public void onEnable() {
		this.saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);

		prefix = getConfig().getString("Prefix");
		prefix = ChatColor.translateAlternateColorCodes('&', prefix);

		rejoinMessage = getConfig().getString("RejoinMessage");
		rejoinMessage = ChatColor.translateAlternateColorCodes('&', rejoinMessage);

		protectedMessage = getConfig().getString("ProtectedMessage");
		protectedMessage = ChatColor.translateAlternateColorCodes('&', protectedMessage);

		underProtectionMessage = getConfig().getString("UnderProtectionMessage");
		underProtectionMessage = ChatColor.translateAlternateColorCodes('&', underProtectionMessage);

		Bukkit.getScheduler().runTaskTimer(this,
				new BukkitRunnable() {
			public void run() {
				Set<String> toBeRemoved = new HashSet<String>();
				HashMap<String, Integer> toBeChanged = new HashMap<String, Integer>();
				for(String s : protectedPlayers.keySet()) {
					int i = protectedPlayers.get(s);
					if(!pausedPlayers.contains(s)) {
						i = i - 1;
						if(i == 0) {
							toBeRemoved.add(s);
							getServer().getPlayer(s).sendMessage(prefix + "You are no longer protected!");
						}
						else {
							toBeChanged.put(s, i);
						}
					}
				}
				for(String s : toBeRemoved) {
					protectedPlayers.remove(s);
				}
				for(String s : toBeChanged.keySet()) {
					int newTime = toBeChanged.get(s);
					protectedPlayers.put(s, newTime);
				}
				toBeRemoved.clear();
				toBeChanged.clear();
			}
		}, 0, 20);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if(protectedPlayers.containsKey(player.getName()) && pausedPlayers.contains(player.getName())) {
			player.sendMessage(prefix + rejoinMessage);
			pausedPlayers.remove(player.getName());
		}
		
		if(!player.hasPlayedBefore()) {
				protectedPlayers.put(player.getName(), getConfig().getInt("Time"));
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {

		Player player = event.getPlayer();

		if(protectedPlayers.containsKey(player.getName())) {
			pausedPlayers.add(player.getName());
		}
	}

	@EventHandler
	public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			Player player = (Player) event.getEntity();
			String name = player.getName();
			if(protectedPlayers.containsKey(name)) {
				event.setCancelled(true);
				damager.sendMessage(prefix + protectedMessage);
			}
			else if(protectedPlayers.containsKey(damager.getName())) {
				event.setCancelled(true);
				damager.sendMessage(prefix + underProtectionMessage);
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("acertimer")) {
			if(args.length == 0) {
				sender.sendMessage(prefix + "Not a valid command, Type '/acertimer remove' to remove your PvP protection.");
			}
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("remove")) {
					if(sender.hasPermission("acertimer.remove")) {
						Player player = (Player) sender;
						String name = player.getName();
						if(protectedPlayers.containsKey(name)) {
							protectedPlayers.remove(name);
							sender.sendMessage(prefix + "You are now able to PVP!");
						}
						else {
							sender.sendMessage(prefix + "Your PVP timer is not active!");
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else {
					sender.sendMessage(prefix + "Only valid command is " + ChatColor.RED + "/acertimer remove");
				}
			}
			else {
				sender.sendMessage(prefix + "Invalid number of arguments!");
			}
		}
		return true;
	}
}
