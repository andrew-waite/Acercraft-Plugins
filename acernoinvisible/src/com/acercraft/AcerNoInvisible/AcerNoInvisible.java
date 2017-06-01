package com.acercraft.AcerNoInvisible;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

public class AcerNoInvisible extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
    Bukkit.getPluginManager().registerEvents(this, this);
  }
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) && 
      (event.getPlayer().getItemInHand().getType() == Material.POTION)) {
      int dur = event.getPlayer().getItemInHand().getDurability();
      if ((dur == 16382) || (dur == 16318) || (dur == 32702) || (dur == 32766) || (dur == 8270) || (dur == 16462) || (dur == 8206) || (dur == 16398)) {
        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.GOLD + "You cannot use Invisibility Potions!");
      }
    }
  }

  @EventHandler
  public void onDispense(BlockDispenseEvent event)
  {
    ItemStack item = event.getItem();
    if (item.getType() == Material.POTION) {
      Potion potion = Potion.fromItemStack(item);
      PotionEffectType effecttype = potion.getType().getEffectType();
      if (effecttype == PotionEffectType.INVISIBILITY)
        event.setCancelled(true);
    }
  }

  public void onPotionSplash(PotionSplashEvent event)
  {
    PotionEffectType effecttype = PotionEffectType.INVISIBILITY;
    if (event.getPotion().getEffects().contains(effecttype))
      event.setCancelled(true);
  }
}