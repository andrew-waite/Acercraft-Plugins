package com.acercraft.AcerGun;

import java.util.List;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class Projectile
{
  protected Player shooter;
  protected Vector dir;
  protected Vector originalDir;
  protected Location baseloc;
  protected Location lastLocation;
  protected Snowball bullet;
  protected Gun gun;
  protected Block lastCollide;
  protected LivingEntity hit;
  protected boolean can = true;
  protected boolean dead = false;
  protected boolean removed = false;
  protected int alive = 128;
  protected double damage_multiplier = 1.0D;

  public void move() {
    if (this.can) {
      this.bullet.setVelocity(this.dir);
    }
    int gundist = this.gun.maxDistance;

    this.alive -= 1;
    if (this.alive <= 0) {
      this.dead = true;
      this.removed = true;
    }
    if ((this.gun.smokeTrail) && (!this.dead) && (this.can)) {
      Util.playEffect(Effect.SMOKE, this.bullet.getLocation(), 0);
      Util.playEffect(Effect.SMOKE, this.bullet.getLocation(), 0);
      Util.playEffect(Effect.SMOKE, this.bullet.getLocation(), 0);
    }

    if ((Util.point_distance(this.baseloc, this.bullet.getLocation()) > gundist) && (this.can)) {
      this.dir = this.originalDir.multiply(0.25D);
      this.bullet.setVelocity(this.dir);
      this.damage_multiplier = 0.75D;
      this.can = false;
    }

    if ((Util.point_distance(this.baseloc, this.bullet.getLocation()) > gundist * 2) && 
      (!this.dead)) {
      this.dead = true;
    }

    if (!this.dead) {
      if (this.lastLocation != null)
        this.lastCollide = this.lastLocation.getBlock();
      else {
        this.lastCollide = this.baseloc.getBlock();
      }
      if (this.bullet.isDead()) {
        onImpact(1.0D, null);
      }
      if (this.gun.firebullet) {
        this.bullet.setFireTicks(16);
      }

      this.lastLocation = this.bullet.getLocation();
    }

    if ((this.dead) && 
      (!this.removed))
      this.removed = true;
  }

  public void propel(Location loc, LivingEntity ee, double mult)
  {
    double distToShooter = Util.point_distance(loc, ee.getLocation());
    if (distToShooter < 3.25D) {
      double dis = 3.0D;
      double maxspeed = 4.0D;
      double ndiv = dis / maxspeed;
      double directionto = Util.point_direction(loc.getX(), loc.getZ(), ee.getLocation().getX(), ee.getLocation().getZ());
      double speed = distToShooter / (ndiv + 0.05D);
      double zspeed = distToShooter / 2.0D * 1.25D;
      double vx = Util.lengthdir_x(1.0D, directionto) * speed;
      double vy = Util.lengthdir_y(1.0D, directionto) * speed;
      if (zspeed > 1.0D)
        zspeed = 1.0D;
      if (zspeed < -1.0D)
        zspeed = -1.0D;
      Vector v = new Vector(vx / 3.0D, zspeed, vy / 3.0D).multiply(mult);
      ee.setVelocity(ee.getVelocity().add(v));
    }
  }

  public void onImpact(double mult, EntityDamageByEntityEvent event) {
    if (!this.dead) {
      this.dead = true;
      if (this.gun.explode)
        explode(this.bullet, 4.0D, 1.0D);
      if (this.gun.tinyexplode)
        explode(this.bullet, 1.5D, 1.0D);
    }

    if (this.lastCollide != null) {
      if (this.lastCollide.getType().equals(Material.LONG_GRASS)) {
        this.lastCollide.setType(Material.AIR);
      }

      if ((this.gun.firebullet) && 
        (this.lastCollide.getType().equals(Material.AIR))) {
        setBlock(Util.MatchPlayer(this.gun.player), this.lastCollide, Material.FIRE);
        this.lastCollide.setType(Material.FIRE);
      }

    }

    getHit(mult, event);
  }

  private void setBlock(Player player, Block b, Material mat) {
    BlockPlaceEvent e = new BlockPlaceEvent(b, b.getState(), b.getRelative(1, 0, 0), new ItemStack(mat, 1), player, true);
    Util.server.getPluginManager().callEvent(e);
  }

  public void getHit(double mult, EntityDamageByEntityEvent event) {
    if (this.hit != null) {
      boolean dead = false;
      if (this.hit.getHealth() <= 0) {
        dead = true;
      }
      if ((this.hit instanceof LivingEntity)) {
        int nhealth = (int)(this.hit.getHealth() - this.gun.armorPenetration);
        if (nhealth > 255) {
          nhealth = 255;
        }
        if (nhealth <= 0) {
          nhealth = 0;
          this.hit.damage(9999, this.shooter);
          dead = true;
        } else {
          try {
            EntityDamageByEntityEvent e = new EntityDamageByEntityEvent(this.shooter, this.hit, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0);
            Util.server.getPluginManager().callEvent(e);
            if (!e.isCancelled())
              this.hit.setHealth(nhealth);
            else
              e.setCancelled(true);
          }
          catch (Exception e) {
            e.printStackTrace();
            System.out.println("[PVPGUN] Error damaging");
          }
        }
      }
      if (!dead) {
        if ((this.hit instanceof Player)) {
          GunPlayer gp = this.gun.plugin.getGunPlayer(this.shooter.getName());
          if (gp != null) {
            gp.shotBullet = true;
          }
        }

        if (this.gun.firebullet) {
          this.hit.setFireTicks(400);
        }
        if (event != null) {
          int damage = (int)Math.ceil(this.gun.damage * mult * this.damage_multiplier);
          event.setDamage(damage);
          this.hit.setLastDamage(0);
        }
      }
    }
  }

  public void explode(Entity e, double val, double mult) {
    try {
      Location eloc = e.getLocation();
      propel(eloc, this.shooter, mult);
      List<Entity> ent = e.getNearbyEntities(4.0D, 4.0D, 4.0D);
      for (int i = 0; i < ent.size(); i++) {
        if ((ent.get(i) instanceof LivingEntity)) {
          propel(eloc, (LivingEntity)ent.get(i), 1.0D);
          LivingEntity li = (LivingEntity)ent.get(i);
          int nhealth = (int)(li.getHealth() - this.gun.damage * 1.5D);
          if (li.equals(this.shooter)) {
            nhealth = (int)(li.getHealth() - this.gun.damage / 1.5D);
          }

          EntityDamageByEntityEvent ev = new EntityDamageByEntityEvent(this.shooter, (LivingEntity)ent.get(i), EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0);
          if (!ev.isCancelled()) {
            if (nhealth <= 0)
              ((LivingEntity)ent.get(i)).damage(9999, this.shooter);
            else {
              ((LivingEntity)ent.get(i)).setHealth(nhealth);
            }
          }
        }
      }
      if (!this.gun.explodeBlocks) {
        val = 0.0D;
      }
      e.remove();
      e.getLocation().getWorld().createExplosion(eloc, (float)val);
    } catch (Exception ee) {
      ee.printStackTrace();
    }
  }
}