package com.acercraft.AcerGun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class Util
{
  public static main plugin;
  public static World world;
  public static Server server;

  public static void Initialize(main plugin)
  {
    Util.plugin = plugin;
    server = plugin.getServer();
    world = (World)server.getWorlds().get(0);
  }

  public static Player MatchPlayer(String player) {
    if (player == null) {
      return null;
    }
    List<Player> players = server.matchPlayer(player);

    if (players.size() == 1) {
      return (Player)players.get(0);
    }
    return null;
  }

  public static void playEffect(Effect e, Location l, int num)
  {
    for (int i = 0; i < server.getOnlinePlayers().length; i++)
      server.getOnlinePlayers()[i].playEffect(l, e, num);
  }

  public static List<Player> Who()
  {
    Player[] players = server.getOnlinePlayers();
    List<Player> players1 = new ArrayList<Player>();
    for (int i = 0; i < players.length; i++) {
      players1.add(players[i]);
    }
    return players1;
  }

  public static double magnitude(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z) {
    double xdist = p1x - p2x;
    double ydist = p1y - p2y;
    double zdist = p1z - p2z;
    return Math.sqrt(xdist * xdist + ydist * ydist + zdist * zdist);
  }

  public static double point_distance(Location loc1, Location loc2) {
    double p1x = loc1.getX();
    double p1y = loc1.getY();
    double p1z = loc1.getZ();

    double p2x = loc2.getX();
    double p2y = loc2.getY();
    double p2z = loc2.getZ();
    return magnitude(p1x, p1y, p1z, p2x, p2y, p2z);
  }

  public static int random(int x) {
    Random rand = new Random();
    return rand.nextInt(x);
  }

  public static double lengthdir_x(double len, double dir) {
    return len * Math.cos(Math.toRadians(dir));
  }

  public static double lengthdir_y(double len, double dir) {
    return -len * Math.sin(Math.toRadians(dir));
  }

  public static double point_direction(double x1, double y1, double x2, double y2) {
    double d;
    try {
      d = Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
    }
    catch (Exception e)
    {
  
      d = 0.0D;
    }
    if ((x1 > x2) && (y1 > y2))
    {
      return -d + 180.0D;
    }
    if ((x1 < x2) && (y1 > y2))
    {
      return -d;
    }
    if (x1 == x2)
    {
      if (y1 > y2)
        return 90.0D;
      if (y1 < y2)
        return 270.0D;
    }
    if ((x1 > x2) && (y1 < y2))
    {
      return -d + 180.0D;
    }
    if ((x1 < x2) && (y1 < y2))
    {
      return -d + 360.0D;
    }
    if (y1 == y2)
    {
      if (x1 > x2)
        return 180.0D;
      if (x1 < x2)
        return 0.0D;
    }
    return 0.0D;
  }

  public static LivingEntity getNearestLivingEntity(Entity thrown, double d) {
    double distance = 10.0D;
    LivingEntity mreturn = null;
    List<Entity> entities = thrown.getNearbyEntities(d, d, d);
    for (int i = 0; i < entities.size(); i++) {
      Entity e = (Entity)entities.get(i);
      if (((e instanceof LivingEntity)) && (!(e instanceof Projectile))) {
        LivingEntity ret = (LivingEntity)e;
        double mdis = point_distance(thrown.getLocation(), ret.getLocation().add(0.0D, 1.0D, 0.0D));
        if (mdis < distance) {
          mreturn = ret;
          distance = mdis;
        }
      }
    }
    return mreturn;
  }
}