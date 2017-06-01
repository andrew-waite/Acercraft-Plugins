package com.acercraft.AcerGun;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class Bullet extends Projectile
{
  public Item ItemInWorld;
  public int releaseTimer;
  public boolean released;

  public Bullet(Player player, Vector dir, Gun gun)
  {
    this.bullet = ((Snowball)player.launchProjectile(Snowball.class));
    this.bullet.setShooter(player);
    this.shooter = player;
    this.gun = gun;
    this.baseloc = this.bullet.getLocation();
    this.originalDir = dir;
    this.dir = dir.multiply(gun.bulletSpeed);
    move();

   
  }
}