package com.acercraft.acerothers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerListener implements Listener{
	
	AcerOthers plugin;
	final Plugin instance = AcerOthers.main;
	  BukkitScheduler scheduler = this.instance.getServer().getScheduler();
	  Map<String, Integer> playerTable = new HashMap<String, Integer>();
	  long playerDelay = this.instance.getConfig().getInt("player-bars.hide-delay-seconds") * 20L;
	  Boolean fixTabNames = true;
	
	public PlayerListener(AcerOthers instance){
		this.plugin = instance;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if ((event.getItem() != null) && (event.getItem().getType() == Material.ENDER_PEARL)){
				if(!plugin.cooldowns.containsKey(player.getName())){
					plugin.cooldowns.put(player.getName(), this.plugin.config.getEnderpearlCooldown());
				}
				else{
					player.sendMessage(parseColors(this.plugin.messages.getPrefix() + this.plugin.messages.getEnderPearlFail(this.plugin.cooldowns.get(player.getName()))));
					event.setCancelled(true);
				}
			}
		}
	

	  
	public String parseColors(String message){
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
