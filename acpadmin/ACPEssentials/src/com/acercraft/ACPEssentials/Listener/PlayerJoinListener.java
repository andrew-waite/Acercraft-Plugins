package com.acercraft.ACPEssentials.Listener;

import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.acercraft.ACPEssentials.API.UserData;

public class PlayerJoinListener implements Listener{
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) throws IOException{
			UserData.hasPlayedBefore(event.getPlayer());
	}

}

