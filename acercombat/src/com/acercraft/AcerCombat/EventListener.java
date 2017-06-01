package com.acercraft.AcerCombat;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class EventListener implements Listener {
	AcerCombat plugin;
	public EventListener(AcerCombat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if(plugin.isTagged(event.getPlayer())) {
			if(plugin.getConfig().getBoolean("killPlayersOnLeave")) {
				event.getPlayer().setHealth(0);
			}
			if(plugin.getConfig().getBoolean("dropItemsOnLeave")) {
				plugin.DropInventory(event.getPlayer());
			}
			if(plugin.getConfig().getBoolean("dropArmorOnLeave")) {
				plugin.DropArmor(event.getPlayer());
			}
			if(plugin.getConfig().getBoolean("dropExpOnLeave")) {
				plugin.DropExp(event.getPlayer());
			}

			if(plugin.leaverBan) {
				plugin.banPlayer(event.getPlayer());
				if(plugin.broadcast) {
					plugin.getServer().broadcastMessage(plugin.prefix + plugin.broadcastMessage.replace("{name}", event.getPlayer().getName()));
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerLoginEvent event) {
		if(plugin.isBanned(event.getPlayer())) {
			event.disallow(Result.KICK_OTHER, plugin.reason);
		}
	}
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN || event.getCause() == TeleportCause.UNKNOWN) {
			if(plugin.isTagged(event.getPlayer())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(plugin.prefix + plugin.teleportBlockedMessage);
			}
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPVP(EntityDamageByEntityEvent event) {
		if(!event.isCancelled()) {
			if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
				if(!plugin.isTagged((Player) event.getEntity())) {
					((Player) event.getEntity()).sendMessage(plugin.prefix + plugin.combatMessage);
				}
				if(!plugin.isTagged((Player) event.getDamager())) {
					((Player) event.getDamager()).sendMessage(plugin.prefix + plugin.combatMessage);
				}
				plugin.tagPlayer((Player) event.getDamager());
				plugin.tagPlayer((Player) event.getEntity());
			}
			else if(event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) event.getDamager();
				if(arrow.getShooter() instanceof Player) {
					if(!plugin.isTagged((Player) event.getEntity())) {
						((Player) event.getEntity()).sendMessage(plugin.prefix + plugin.combatMessage);
					}
					if(!plugin.isTagged((Player) arrow.getShooter())) {
						((Player) arrow.getShooter()).sendMessage(plugin.prefix + plugin.combatMessage);
					}
					plugin.tagPlayer((Player) event.getEntity());
					plugin.tagPlayer((Player) arrow.getShooter());
				}
			}
			else if(event.getEntity() instanceof Player && event.getDamager() instanceof ThrownPotion) {
				ThrownPotion potion = (ThrownPotion) event.getDamager();
				if(potion.getShooter() instanceof Player) {
					if(!plugin.isTagged((Player) event.getEntity())) {
						((Player) event.getEntity()).sendMessage(plugin.prefix + plugin.combatMessage);
					}
					if(!plugin.isTagged((Player) potion.getShooter())) {
						((Player) potion.getShooter()).sendMessage(plugin.prefix + plugin.combatMessage);
					}
					plugin.tagPlayer((Player) event.getEntity());
					plugin.tagPlayer((Player) potion.getShooter());
				}
			}
		}
	}
}
