/**
 * 
 */
package com.acercraft.acerdonatorperks.acercrzyfeet;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.acercraft.acerdonatorperks.Main;
import com.acercraft.acerdonatorperks.acercrzyfeet.Util.Files.CrazyAutoFireFile;
import com.acercraft.acerdonatorperks.acercrzyfeet.Util.Files.CrazyAutoMagicFile;
import com.acercraft.acerdonatorperks.acercrzyfeet.Util.Files.CrazyAutoPearlFile;
import com.acercraft.acerdonatorperks.acercrzyfeet.Util.Files.CrazyAutoSmokeFile;

/**
 * @author Pete Wicken
 *
 */
public class CrazyFeetJoinListener implements Listener {
	
	ChatColor green = ChatColor.GREEN;
	
	@EventHandler
	public void onCrazyFireJoin(PlayerJoinEvent pJE) {	
		Player player = pJE.getPlayer();	
		if(CrazyAutoFireFile.cFPlayers.contains(player.getName())) {
			Main.CrazyFire.add(player);
			player.sendMessage(green+"You have joined the game with automatic CrazyFire. To disable this, type /crazyautofire");
		} else {
			//doNothing
		}
	}
	
	@EventHandler
	public void onCrazySmokeJoin(PlayerJoinEvent pJE) {
		Player player = pJE.getPlayer();	
		if(CrazyAutoSmokeFile.cSPlayers.contains(player.getName())) {
			Main.CrazySmoke.add(player);
			player.sendMessage(green+"You have joined the game with automatic CrazySmoke. To disable this, type /crazyautosmoke");
		} else {
			//doNothing
		}
	}
	
	@EventHandler
	public void onCrazyPearlJoin(PlayerJoinEvent pJE) {
		Player player = pJE.getPlayer();
		if(CrazyAutoPearlFile.cPPlayers.contains(player.getName())) {
			Main.CrazySmoke.add(player);
			player.sendMessage(green+"You have joined the game with automatic CrazyPearl. To disable this, type /crazyautopearl");
		} else {
			//doNothing
		}
	}
	
	@EventHandler
	public void onCrazyMagicJoin(PlayerJoinEvent pJE) {
		Player player = pJE.getPlayer();
		if(CrazyAutoMagicFile.cMPlayers.contains(player.getName())) {
			Main.CrazyMagic.add(player);
			player.sendMessage(green+"You have joined the game with automatic CrazyMagic. To disable this, type /crazyautomagic");
		} else {
			//doNothing
		}
	}
}
