package com.acercraft.AcerGun;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin
{
  public PluginPlayerListener playerListener = new PluginPlayerListener(this);
  public PluginEntityListener entityListener = new PluginEntityListener(this);
  public String pluginName = "PVPGun";
  public ArrayList<Projectile> bullets = new ArrayList<Projectile>();
  public ArrayList<Gun> gun = new ArrayList<Gun>();
  public ArrayList<GunPlayer> gunplayers = new ArrayList<GunPlayer>();
  public int ExecuteMove;
  public Timer UpdateTimer;
  public static boolean zoneGuns = false;
  public static final Logger log = Logger.getLogger("Minecraft");
  
  public void onEnable()
  {
    System.out.println(this.pluginName + " enabled");
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(this.playerListener, this);
    pm.registerEvents(this.entityListener, this);
    Util.Initialize(this);
    
    if(pm.getPlugin("Factions") != null){
    log.info("[AcerGun] AcerGun found factions succesfully");
    }else{
    	log.info("[AcerGun] AcerGun could not find Factions");
    	log.info("[AcerGun] AcerGun needs Factions to function disabling now...");
    	pm.disablePlugin(this);
    }

    startup();
  }

  public void onDisable()
  {
    System.out.println(this.pluginName + " disabled");
    this.bullets.clear();
    this.gun.clear();
    this.gunplayers.clear();

    getServer().getScheduler().cancelTask(this.ExecuteMove);
    this.UpdateTimer.cancel();
  }

  public String getMcWar() {
    return getDataFolder().getAbsolutePath();
  }

  public void startup() {
    this.UpdateTimer = new Timer();
    this.UpdateTimer.schedule(new UpdateTimer(), 10L, 80L);

    this.ExecuteMove = getServer().getScheduler().scheduleSyncRepeatingTask(this, new ExecuteMove(), 20L, 1L);

    File dir = getDataFolder();
    if (!dir.exists()) {
      dir.mkdir();
    }

    File dir2 = new File(getMcWar() + "/guns");
    if (!dir2.exists()) {
      dir2.mkdir();
    }

    dir2 = new File(getMcWar() + "/projectile");
    if (!dir2.exists()) {
      dir2.mkdir();
    }

    loadGuns();
    loadProjectile();

    getOnlinePlayers();
  }

  public void reload() {
    onDisable();
    startup();
  }

  public void loadGuns() {
    String path = getMcWar() + "/guns";
    File dir = new File(path);
    String[] children = dir.list();
    if (children != null)
      for (int i = 0; i < children.length; i++) {
        String filename = children[i];
        weaponReader f = new weaponReader(this, new File(path + "/" + filename), "gun");
        if (f.loaded) {
          System.out.println("[AcerGun] Loaded gun " + filename + "  can rightclick fire?" + f.ret.rightclickfire);
          f.ret.name = filename.toLowerCase();
          f.ret.node = ("pvpgun." + filename.toLowerCase());
          this.gun.add(f.ret);
        }
      }
  }

  public void loadProjectile()
  {
    String path = getMcWar() + "/projectile";
    File dir = new File(path);
    String[] children = dir.list();
    if (children != null)
      for (int i = 0; i < children.length; i++) {
        String filename = children[i];
        weaponReader f = new weaponReader(this, new File(path + "/" + filename), "projectile");
        if (f.loaded) {
          System.out.println("[Acergun] Loaded projectile " + filename);
          f.ret.node = ("pvpgun." + filename.toLowerCase());
          f.ret.name = filename.toLowerCase();
          this.gun.add(f.ret);
        }
      }
  }

  public void getOnlinePlayers()
  {
    Player[] plist = Util.server.getOnlinePlayers();
    for (int i = 0; i < plist.length; i++) {
      GunPlayer g = new GunPlayer(this, plist[i]);
      this.gunplayers.add(g);
    }
  }

  public Gun getGun(String str) {
    for (int i = 0; i < this.gun.size(); i++) {
      if (((Gun)this.gun.get(i)).name.equals(str)) {
        return (Gun)this.gun.get(i);
      }
    }
    return null;
  }

  public GunPlayer getGunPlayer(String str)
  {
    for (int i = 0; i < this.gunplayers.size(); i++) {
      if (((GunPlayer)this.gunplayers.get(i)).player.equals(str)) {
        return (GunPlayer)this.gunplayers.get(i);
      }
    }
    return null;
  }

  public Projectile getBullet(Snowball attacker) {
    for (int i = 0; i < this.bullets.size(); i++) {
      Projectile t = (Projectile)this.bullets.get(i);
      if (t != null) {
        try {
          if ((t.bullet != null) && (
            (t.bullet.getEntityId() == attacker.getEntityId()) || (attacker.equals(t.bullet)))) {
            return t;
          }
        }
        catch (Exception localException)
        {
        }
      }
    }
    return null;
  }



  class ExecuteMove
    implements Runnable
  {
    public ExecuteMove()
    {
    }

    public void run()
    {
      try
      {
        for (int i = main.this.bullets.size() - 1; i >= 0; i--) {
          Projectile t = (Projectile)main.this.bullets.get(i);

          t.move();
        }

        for (int i = main.this.bullets.size() - 1; i >= 0; i--) {
          Projectile t = (Projectile)main.this.bullets.get(i);
          if (t.removed)
            main.this.bullets.remove(i);
        }
      } catch (Exception localException) {
      }
    }
  }

  class UpdateTimer extends TimerTask {
    UpdateTimer() {
    }

    public void run() {
      try {
        synchronized (main.this.gunplayers) {
          for (int i = 0; i < main.this.gunplayers.size(); i++) {
            GunPlayer gp = (GunPlayer)main.this.gunplayers.get(i);
            for (int ii = 0; ii < gp.guns.size(); ii++) {
              ((Gun)gp.guns.get(ii)).canUpate = true;
            }
          }
          for (int i = 0; i < main.this.gunplayers.size(); i++) {
            GunPlayer gp = (GunPlayer)main.this.gunplayers.get(i);
            gp.update();
          }
        }
      }
      catch (Exception localException)
      {
      }
    }
  }
}