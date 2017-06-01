package com.acercraft.acerdonatorperks;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class AnvilUtils
{
  public static Map<String, AnvilUtil> players = new HashMap<String, AnvilUtil>();

  public AnvilUtil getPlayer(Player player) {
    if (players.containsKey(player.getName())) {
      return (AnvilUtil)players.get(player.getName());
    }

    AnvilUtil p = new AnvilUtil(player);
    players.put(player.getName(), p);
    return p;
  }
}