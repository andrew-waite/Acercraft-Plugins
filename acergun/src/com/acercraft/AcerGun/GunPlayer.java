package com.acercraft.AcerGun;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GunPlayer
{
  public main plugin;
  public String player;
  public boolean firing = false;
  public String whatFiring = "";
  public Gun fireWith;
  public ArrayList<Gun> guns = new ArrayList<Gun>();
  public int globalcooldown;
  public Player fire = Util.MatchPlayer(this.player);
  public boolean shotBullet = false;

  public GunPlayer(main plugin, Player player) {
    this.plugin = plugin;
    this.player = player.getName();
    for (int i = 0; i < plugin.gun.size(); i++)
      this.guns.add(((Gun)plugin.gun.get(i)).clone());
  }

  public Gun getGun(Material mat, String preference)
  {
    if (preference.equals("")) {
      for (int i = 0; i < this.guns.size(); i++) {
        if (((Gun)this.guns.get(i)).gunMat == mat.getId())
          return (Gun)this.guns.get(i);
      }
    }
    else {
      for (int i = 0; i < this.guns.size(); i++) {
        if ((((Gun)this.guns.get(i)).gunMat == mat.getId()) && (((Gun)this.guns.get(i)).name.equals(preference))) {
          return (Gun)this.guns.get(i);
        }
      }
    }
    return null;
  }

  public void fire(String clickType) {
    if (this.fire == null) {
      return;
    }
    if (this.fire.getItemInHand() == null) {
      return;
    }
    for (int i = 0; i < this.guns.size(); i++) {
      ((Gun)this.guns.get(i)).player = this.player;
    }

    String preference = "";

    this.fireWith = getGun(this.fire.getItemInHand().getType(), preference);
    if (this.fireWith != null) {
      this.whatFiring = this.fireWith.gunName;
      boolean can = true;
      if ((!this.fireWith.rightclickfire) && (clickType.equals("right")))
        can = false;
      if (can)
        if ((this.fireWith.needsPermission) && (!this.fire.hasPermission(this.fireWith.node))) {
          this.fire.sendMessage(ChatColor.GRAY + "["+ChatColor.AQUA + "AcerGun"+ChatColor.GRAY+"] " + ChatColor.RED + "You do not have permission to fire this gun!");
        }
        else if ((this.fireWith instanceof Throwable))
          item_throw();
        else
          shoot();
    }
  }

  public boolean checkAmmo()
  {
    ItemStack ammo = getAmmo();
    if (ammo != null) {
      int amtAmmo = ammo.getAmount();
      if (amtAmmo >= this.fireWith.ammoNeeded)
        return true;
    }
    else {
      this.fire.sendMessage(ChatColor.GRAY + "["+ChatColor.AQUA + "AcerGun"+ChatColor.GRAY+"] " + ChatColor.GRAY + "This gun needs " + ChatColor.AQUA + Material.getMaterial(this.fireWith.ammoMat).toString());
    }
    return false;
  }

  public ItemStack getAmmo() {
    return InventoryHelper.getFirstItemStack(this.fire.getInventory(), Material.getMaterial(this.fireWith.ammoMat));
  }

  public void loseAmmo() {
    ItemStack ammo = getAmmo();
    if (ammo != null) {
      int amtAmmo = ammo.getAmount();
      if (amtAmmo > this.fireWith.ammoNeeded)
        ammo.setAmount(ammo.getAmount() - this.fireWith.ammoNeeded);
      else
        this.fire.getInventory().remove(ammo);
    }
  }

  public void item_throw()
  {
    try
    {
      boolean canFire = false;
      if (this.fireWith != null) {
        if ((this.fireWith.timer <= 0) && (!this.fireWith.reloading) && (!this.fireWith.isFiring))
          canFire = true;
        if ((checkAmmo()) && (canFire)) {
          loseAmmo();
          this.fireWith.throw_item();
        }
      }
    }
    catch (Exception localException) {
    }
  }

  @SuppressWarnings("deprecation")
public void shoot() {
    try {
      boolean canFire = false;
      if (this.fireWith != null) {
        if ((this.fireWith.timer <= 0) && (!this.fireWith.reloading) && (!this.fireWith.isFiring)) {
          canFire = true;
        }
        if ((canFire) && 
          (checkAmmo())) {
          this.fireWith.isFiring = true;
          ExecuteFire(this.fireWith);
          Player p = Util.MatchPlayer(this.player);
          p.updateInventory();
        }
      }
    }
    catch (Exception localException)
    {
    }
  }

  public void ExecuteFire(Gun g) {
    if (!g.checkShoot())
      return;
    if (checkAmmo()) {
      loseAmmo();
      this.fire.setSprinting(false);
    }
  }

  public void update() {
    this.globalcooldown -= 1;
    this.fire = Util.MatchPlayer(this.player);
    for (int i = 0; i < this.guns.size(); i++)
      if (((Gun)this.guns.get(i)).canUpate) {
        ((Gun)this.guns.get(i)).canUpate = false;
        ((Gun)this.guns.get(i)).timer -= 1;
        ((Gun)this.guns.get(i)).step();
        if (((Gun)this.guns.get(i)).timer < 0) {
          ((Gun)this.guns.get(i)).reloading = false;
          ExecuteFire((Gun)this.guns.get(i));
        }
      }
  }
}