package com.acercraft.AcerGun;

import java.util.Random;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Gun
{
  public int ammoNeeded;
  public int repeat;
  public int roundsPerBurst;
  public int reloadTime;
  public int bulletsPerClick = 1;
  public int timer;
  public int damage;
  public int maxDistance;
  public int gunMat;
  public int ammoMat;
  public int fireradius = 0;
  public double accuracy;
  public double bulletSpeed;
  public double armorPenetration;
  public boolean explode;
  public boolean smokeTrail;
  public boolean needsPermission;
  public boolean isFiring;
  public boolean reloading = false;
  public boolean canUpate = true;
  public boolean cockable = false;
  public boolean boltaction = false;
  public boolean firebullet = false;
  public boolean tinyexplode = false;
  public boolean explodeBlocks = false;
  public boolean rightclickfire = false;
  public main plugin;
  public String name;
  public String node;
  public String gunName;
  public String player;

  public Gun(main main, Material ironHoe, Material flint)
  {
    this.plugin = main;
    this.gunMat = ironHoe.getId();
    this.ammoMat = flint.getId();
  }

  public Gun(main main) {
    this.plugin = main;
  }

  public Gun clone() {
    Gun g = new Gun(this.plugin, Material.getMaterial(this.gunMat), Material.getMaterial(this.ammoMat));
    g.ammoNeeded = this.ammoNeeded;
    g.repeat = this.repeat;
    g.roundsPerBurst = this.roundsPerBurst;
    g.reloadTime = this.reloadTime;
    g.bulletsPerClick = this.bulletsPerClick;
    g.damage = this.damage;
    g.maxDistance = this.maxDistance;
    g.accuracy = this.accuracy;
    g.bulletSpeed = this.bulletSpeed;
    g.armorPenetration = this.armorPenetration;
    g.explode = this.explode;
    g.smokeTrail = this.smokeTrail;
    g.tinyexplode = this.tinyexplode;
    g.needsPermission = this.needsPermission;
    g.boltaction = this.boltaction;
    g.cockable = this.cockable;
    g.firebullet = this.firebullet;
    g.name = this.name;
    g.node = this.node;
    g.rightclickfire = this.rightclickfire;
    g.explodeBlocks = this.explodeBlocks;
    return g;
  }

  public boolean checkShoot() {
    if (this.isFiring) {
      if (this.repeat < this.roundsPerBurst) {
        shoot();
        this.repeat += 1;
        return true;
      }
      this.repeat = 0;
      this.timer = this.reloadTime;
      this.isFiring = false;
      this.reloading = true;
    }

    return false;
  }

  public void shoot() {
    final Gun gun = this;
    final Player fire = Util.MatchPlayer(this.player);
    for (int i = 0; i < this.bulletsPerClick; i++)
      synchronized (this.plugin.bullets)
      {
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
          public void run() {
            try {
              Location ploc = fire.getLocation();
              Random rand = new Random();
              double dir = -ploc.getYaw() - 90.0F;
              double pitch = -ploc.getPitch();
              double xwep = (rand.nextInt((int)(Gun.this.accuracy * 100.0D)) - rand.nextInt((int)(Gun.this.accuracy * 100.0D)) + 0.5D) / 100.0D;
              double ywep = (rand.nextInt((int)(Gun.this.accuracy * 100.0D)) - rand.nextInt((int)(Gun.this.accuracy * 100.0D)) + 0.5D) / 100.0D;
              double zwep = (rand.nextInt((int)(Gun.this.accuracy * 100.0D)) - rand.nextInt((int)(Gun.this.accuracy * 100.0D)) + 0.5D) / 100.0D;
              double xd = Math.cos(Math.toRadians(dir)) * Math.cos(Math.toRadians(pitch)) + xwep;
              double yd = Math.sin(Math.toRadians(pitch)) + ywep;
              double zd = -Math.sin(Math.toRadians(dir)) * Math.cos(Math.toRadians(pitch)) + zwep;
              Vector vec = new Vector(xd, yd, zd);
              Bullet bullet = new Bullet(fire, vec, gun);
              Gun.this.plugin.bullets.add(bullet);

              double px = fire.getLocation().getX();
              double pz = fire.getLocation().getZ();
              double pdir = -fire.getLocation().getYaw() - 90.0F;
              double nx = px + Util.lengthdir_x(1.0D, pdir);
              double nz = pz + Util.lengthdir_y(1.0D, pdir);
              Location nloc = new Location(fire.getWorld(), nx, fire.getLocation().getY() + 1.0D, nz);

              Util.playEffect(Effect.SMOKE, nloc, 256);
              Util.playEffect(Effect.SMOKE, nloc, 256);
              Util.playEffect(Effect.GHAST_SHOOT, fire.getLocation(), 1);
            }
            catch (Exception localException)
            {
            }
          }
        });
      }
  }

  public void throw_item() {
    final Gun gun = this;
    final Player fire = Util.MatchPlayer(this.player);
    synchronized (this.plugin.bullets) {
      this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
        public void run() {
          try {
            Location ploc = fire.getLocation();
            double dir = -ploc.getYaw() - 90.0F;
            double pitch = -ploc.getPitch();
            double xd = Math.cos(Math.toRadians(dir)) * Math.cos(Math.toRadians(pitch));
            double yd = Math.sin(Math.toRadians(pitch));
            double zd = -Math.sin(Math.toRadians(dir)) * Math.cos(Math.toRadians(pitch));
            Vector vec = new Vector(xd, yd, zd);
            ThrownObject bullet = new ThrownObject(fire, vec, gun, Gun.this.ammoMat);
            Gun.this.plugin.bullets.add(bullet);
            Gun.this.timer = Gun.this.reloadTime;
          }
          catch (Exception localException) {
          }
        }
      });
    }
  }

  public void step() {
    Player fire = Util.MatchPlayer(this.player);
    if ((this.boltaction) && (
      (this.timer == 4) || (this.timer == 2))) {
      Util.playEffect(Effect.DOOR_TOGGLE, fire.getLocation(), 1);
    }

    if ((this.cockable) && (
      (this.timer == 6) || (this.timer == 5)))
      Util.playEffect(Effect.DOOR_TOGGLE, fire.getLocation(), 1);
  }
}