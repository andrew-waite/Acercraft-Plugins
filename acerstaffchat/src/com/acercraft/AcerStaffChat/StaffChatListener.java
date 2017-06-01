package com.acercraft.AcerStaffChat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffChatListener implements Listener
{
	
	public AcerStaffChat plugin;
	
	public StaffChatListener(AcerStaffChat instance)
	{
		this.plugin = instance;
	}
	
	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent event)
	{
		if(plugin.getAdminList().contains(event.getPlayer().getName()))
		{
			String message = event.getMessage();
			
			event.setCancelled(true);
			
			for(Player player : Bukkit.getServer().getOnlinePlayers())
			{
				if(plugin.hasPermission(player, "acercraft.adminchat.admin"))
				{
					player.sendMessage(message);
				}
			}
		}
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event)
	{
		if(plugin.getAdminList().contains(event.getPlayer().getName()))
		{
			plugin.removeAdminList(event.getPlayer().getName());
		}
	}
	
}
