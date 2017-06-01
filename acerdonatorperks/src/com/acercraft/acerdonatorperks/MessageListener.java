package com.acercraft.acerdonatorperks;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MessageListener implements Listener{
	
	private Main plugin;
	
	public MessageListener(Main instance){
		this.plugin = instance;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		
		if (event.getPlayer().hasPermission("acerdonatorperks.join.lighting"))
		{
			Location l = event.getPlayer().getLocation();
			l.getWorld().strikeLightningEffect(l);
		}
		
		String joinmsg = this.plugin.getConfig().getString("Join");
		joinmsg = joinmsg.replaceAll("%player%", event.getPlayer().getName());
		event.setJoinMessage(convertColors(joinmsg));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
	
		String quitmsg = this.plugin.getConfig().getString("Leave");
		quitmsg = quitmsg.replaceAll("%player%", event.getPlayer().getName());
		event.setQuitMessage(convertColors(quitmsg));
	}
	
	public String convertColors(String message){
		return ChatColor.translateAlternateColorCodes('&', message);
	}

}
