package com.acercraft.acertokens;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerListener implements Listener {
	AcerTokens plugin;
	public PlayerListener(AcerTokens plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player) {
			Player p = (Player) event.getEntity().getKiller();
			String name = p.getName();
			if(!plugin.mobs.isSpawnedMob(event.getEntity().getUniqueId())) {
				if(plugin.config.getMobsList().contains(event.getEntityType().getName())) {
					String mob = event.getEntityType().getName();
					if(plugin.config.getMobTokens(mob) != null) {
						List<Integer> amounts = plugin.config.getMobTokens(mob);
						if(plugin.config.getMobChance(mob) != null && isValidDouble(plugin.config.getMobChance(mob))) {
							double chance = Double.parseDouble(plugin.config.getMobChance(mob));
							Random r = new Random();
							double random = r.nextDouble();
							if(random <= chance) {
								int pick = r.nextInt(amounts.size());
								int amount = amounts.get(pick);
								plugin.users.addTokens(name, amount);
								p.sendMessage(plugin.prefix + "You have recieved " + amount + " AcerTokens for killing a mob!");
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getSpawnReason() == SpawnReason.SPAWNER || event.getSpawnReason() == SpawnReason.SLIME_SPLIT) {
			plugin.mobs.addUUID(event.getEntity().getUniqueId());
		}
	}

	public boolean isValidDouble(String number) {
		try {
			Double.parseDouble(number);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
}
