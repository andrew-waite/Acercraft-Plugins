package com.acercraft.acerpetnames;

import org.bukkit.entity.Player;

public class RenamingPlayer
{
  public Player player;
  public String name;

  public RenamingPlayer(Player player, String list)
  {
    this.player = player;
    this.name = list;
  }
 
}