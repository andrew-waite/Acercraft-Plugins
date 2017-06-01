package com.acercraft.AcerCombat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AcerCombat extends JavaPlugin {
	EventListener events;
	public HashMap<String, Long> taggedPlayers = new HashMap<String, Long>();
	public HashMap<String, Long> bannedPlayers = new HashMap<String, Long>();
	int combatTime;
	String prefix;
	String combatMessage;
	String outOfCombatMessage;
	boolean leaverBan;
	boolean broadcast;
	int banLength;
	String reason;
	String teleportBlockedMessage;
	String broadcastMessage;
	public void onEnable() {
		saveDefaultConfig();
		events = new EventListener(this);
		Bukkit.getPluginManager().registerEvents(events, this);
		combatTime = getConfig().getInt("combatTime");
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
		combatMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("combatMessage"));
		outOfCombatMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("outOfCombatMessage"));
		leaverBan = getConfig().getBoolean("leaverBan.enabled");
		banLength = getConfig().getInt("leaverBan.length");
		reason = ChatColor.translateAlternateColorCodes('&', getConfig().getString("leaverBan.reason"));
		teleportBlockedMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("teleportBlockedMessage"));
		broadcast = getConfig().getBoolean("leaverBan.broadcast.enabled");
		broadcastMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("leaverBan.broadcast.message"));
		
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
			@SuppressWarnings("rawtypes")
			public void run() {
				for(Iterator it = taggedPlayers.entrySet().iterator(); it.hasNext();) {
					Map.Entry entry = (Map.Entry)it.next();
					long time = getSystemTime();
					long tagTime = (Long) entry.getValue();
					if((time - tagTime) > combatTime*1000) {
						it.remove();
						Player p = getServer().getPlayer((String)entry.getKey());
						if(p != null) {
							p.sendMessage(prefix + outOfCombatMessage);
						}
					}
				}
			}
		}, 0, 20);
	}

	public void tagPlayer(Player player) {
		String name = player.getName();
		taggedPlayers.put(name, getSystemTime());
	}

	public void banPlayer(Player player) {
		String name = player.getName();
		bannedPlayers.put(name, getSystemTime());
	}

	public boolean isTagged(Player player) {
		String name = player.getName();
		return taggedPlayers.containsKey(name);
	}

	public boolean isBanned(Player player) {
		String name = player.getName();
		if(bannedPlayers.containsKey(name)) {
			long banTime = bannedPlayers.get(name);
			long time = getSystemTime();
			if((time - banTime) > banLength*60*1000) {
				bannedPlayers.remove(name);
			}
			return bannedPlayers.containsKey(name);
		}
		return false;
	}

	public void DropInventory(Player ePlayer) {
		ItemStack[] stacks = (ItemStack[])ePlayer.getInventory().getContents().clone();
		ePlayer.getInventory().clear();
		for (ItemStack stack : stacks)
			if ((stack != null) && (stack.getType() != Material.AIR))
			{
				ePlayer.getLocation().getWorld().dropItemNaturally(ePlayer.getLocation(), stack);
			}
	}

	public void DropArmor(Player ePlayer) {
		ItemStack[] armorstacks = (ItemStack[])ePlayer.getInventory().getArmorContents().clone();
		ePlayer.getInventory().setArmorContents(new ItemStack[4]);
		for (ItemStack stack : armorstacks)
			if ((stack != null) && (stack.getType() != Material.AIR))
			{
				ePlayer.getLocation().getWorld().dropItemNaturally(ePlayer.getLocation(), stack);
			}
	}

	public void DropExp(Player ePlayer) {
		ePlayer.setExp(0.0F);
		for (int i = 0; i < ePlayer.getLevel(); i++) {
			((ExperienceOrb)ePlayer.getLocation().getWorld().spawn(ePlayer.getLocation(), ExperienceOrb.class)).setExperience(2 * i + 1);
		}
		ePlayer.setLevel(0);
	}

	public long getSystemTime() {
		return System.currentTimeMillis();
	}
}
