package com.acercraft.AcerLimit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AcerLimit extends JavaPlugin
  implements Listener
{
  int radius;
  int allowedSpawners;
  int allowedEntities;

  public void onEnable()
  {
    saveDefaultConfig();
    getServer().getPluginManager().registerEvents(this, this);
    this.radius = getConfig().getInt("radius");
    this.allowedSpawners = getConfig().getInt("spawners");
    this.allowedEntities = getConfig().getInt("entities");
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void onBlockPlace(BlockPlaceEvent event) {
    if (event.getBlock().getType() == Material.MOB_SPAWNER) {
      int spawners = getNearbySpawners(event.getBlock(), this.radius);
      if (spawners > this.allowedSpawners) {
        event.setCancelled(true);
        String message = "&c You are not allowed to place more than 5 spawners in a 31x31x31 region";
        message = ChatColor.translateAlternateColorCodes('&', message);
        event.getPlayer().sendMessage(message);
      }
    }
  }

  @EventHandler(priority=EventPriority.LOWEST)
  public void onSpawnerChange(PlayerInteractEvent event) {
    if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && 
      (event.getItem() != null) && (event.getItem().getTypeId() == 383) && 
      (event.getItem().getData().getData() == 99) && 
      (getNearbyGolemSpawners(event.getClickedBlock(), 15) >= 3)) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to have more than 3 Iron Golem spawners in a 31x31x31 region");
    }
  }

  public int getNearbySpawners(Block center, int radius)
  {
    int spawners = 0;
    for (int x = -radius; x < radius; x++) {
      for (int y = -radius; y < radius; y++) {
        for (int z = -radius; z < radius; z++) {
          if (center.getRelative(x, y, z).getType() == Material.MOB_SPAWNER) {
            spawners++;
          }
        }
      }
    }
    return spawners;
  }

  public int getNearbyGolemSpawners(Block center, int radius) {
    int spawners = 0;
    for (int x = -radius; x < radius; x++) {
      for (int y = -radius; y < radius; y++) {
        for (int z = -radius; z < radius; z++) {
          if (center.getRelative(x, y, z).getType() == Material.MOB_SPAWNER) {
            CreatureSpawner spawner = (CreatureSpawner)center.getRelative(x, y, z).getState();
            if (spawner.getSpawnedType() == EntityType.IRON_GOLEM) {
              spawners++;
            }
          }
        }
      }
    }
    return spawners;
  }

  public boolean isInArea(Location loc1, Location loc2, Location location) {
    int x1 = loc1.getBlockX();
    int x2 = loc2.getBlockX();
    int y1 = loc1.getBlockY();
    int y2 = loc2.getBlockY();
    int z1 = loc1.getBlockZ();
    int z2 = loc2.getBlockZ();

    double px = location.getX();
    double pz = location.getZ();
    double py = location.getY();

    if (((px >= x1) && (px <= x2)) || ((px <= x1) && (px >= x2) && (
      ((pz >= z1) && (pz <= z2)) || ((pz <= z1) && (pz >= z2) && (
      ((py >= y1) && (py <= y2)) || ((py <= y1) && (py >= y2))))))) {
      return true;
    }

    return false;
  }
}