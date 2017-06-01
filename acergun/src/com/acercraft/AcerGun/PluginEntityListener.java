package com.acercraft.AcerGun;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PluginEntityListener
  implements Listener
{
  main plugin;

  public PluginEntityListener(main plugin)
  {
    this.plugin = plugin;
  }

  @EventHandler(priority=EventPriority.NORMAL)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    try {
      if (!(event.getEntity() instanceof LivingEntity)) {
        return;
      }

      if (event.isCancelled()) {
        return;
      }
      LivingEntity defender = (LivingEntity)event.getEntity();
      Entity p = event.getDamager();

      if ((p instanceof Player)) {
        GunPlayer gp = this.plugin.getGunPlayer(((Player)p).getName());
        if ((gp != null) && 
          (gp.shotBullet)) {
          gp.shotBullet = false;
          event.setCancelled(true);
        }
      }
      else if ((p instanceof Snowball)) {
        Snowball attacker = (Snowball)p;
        try {
          Projectile b = this.plugin.getBullet(attacker);
          if (b != null) {
            Gun g = b.gun;
            if (g != null) {
              b.hit = defender;
              double mult = 1.0D;
              if (isNear(attacker.getLocation(), defender.getEyeLocation(), 0.25D)) {
                Util.playEffect(Effect.ZOMBIE_DESTROY_DOOR, defender.getLocation(), 3);
                mult = 2.0D;
              }
              b.onImpact(mult, event);
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("[AcerGun] ERROR DAMAGING!");
        }
        return;
      }
      return;
    }
    catch (Exception localException1) {
    }
  }

  private boolean isNear(Location location, Location eyeLocation, double d) {
    return Math.abs(location.getY() - eyeLocation.getY()) <= d;
  }
}