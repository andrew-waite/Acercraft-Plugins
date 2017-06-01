package com.acercraft.AcerGun;

import org.bukkit.Material;

public class Throwable extends Gun
{
  public Throwable(main plugin, Material ammoMat)
  {
    super(plugin, ammoMat, ammoMat);
  }

  public Throwable(main main) {
    super(main);
  }

  public Gun clone()
  {
    Throwable g = new Throwable(this.plugin, Material.getMaterial(this.ammoMat));
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
    g.needsPermission = this.needsPermission;
    g.boltaction = this.boltaction;
    g.cockable = this.cockable;
    g.fireradius = this.fireradius;
    g.tinyexplode = this.tinyexplode;
    g.name = this.name;
    g.node = this.node;
    g.rightclickfire = this.rightclickfire;
    g.explodeBlocks = this.explodeBlocks;
    return g;
  }
}