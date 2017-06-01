package com.acercraft.AcerGun;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ThrownObject extends Projectile
{
  public Item ItemInWorld;
  public int releaseTimer;
  public boolean released;

  public ThrownObject(Player player, Vector dir, Gun gun, int id)
  {
    this.ItemInWorld = player.getWorld().dropItem(player.getLocation().add(0.0D, 1.0D, 0.0D), new ItemStack(id, 1));

    this.shooter = player;
    this.gun = gun;
    this.baseloc = this.ItemInWorld.getLocation();
    this.originalDir = dir;
    this.dir = dir.multiply(gun.bulletSpeed);

    this.ItemInWorld.setVelocity(dir);
    this.ItemInWorld.setPickupDelay(9999999);
    this.releaseTimer = 56;

  }

  public void move()
  {
    this.releaseTimer -= 1;
    if (!this.dead) {
      if ((this.releaseTimer < 0) && (!this.released)) {
        this.released = true;
        release();
      }
    }
    else this.ItemInWorld.remove();
  }

  public void release()
  {
    if ((this.gun.explode) || (this.gun.tinyexplode))
      try {
        
        System.out.println(this.gun.fireradius);
        if (this.gun.fireradius > 0) {
          burn();
        }
        if (this.gun.explode)
          explode(this.ItemInWorld, 0.0D, 1.0D);
        if (this.gun.tinyexplode)
          explode(this.ItemInWorld, 0.0D, 0.2D);
        this.ItemInWorld.remove();
        this.dead = true;
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  public void burn()
  {
    Location loc = this.ItemInWorld.getLocation().clone();
    int x = loc.getBlockX();
    int y = loc.getBlockY();
    int z = loc.getBlockZ();
    for (int i = -this.gun.fireradius; i < this.gun.fireradius; i++)
      for (int ii = -this.gun.fireradius; ii < this.gun.fireradius; ii++)
        for (int iii = this.gun.fireradius; iii > -this.gun.fireradius; iii--) {
          Location loc2 = new Location(this.ItemInWorld.getWorld(), x + i, y + ii, z + iii);
          if (Util.point_distance(loc, loc2) <= this.gun.fireradius) {
            Location loc3 = new Location(this.ItemInWorld.getWorld(), x + i, y + ii + 1, z + iii);
            this.ItemInWorld.getWorld().getBlockAt(loc2);
            this.ItemInWorld.getWorld().getBlockAt(loc3);
         
             
          }
        }
  }
}