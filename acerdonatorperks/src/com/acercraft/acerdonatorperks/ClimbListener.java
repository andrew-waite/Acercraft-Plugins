package com.acercraft.acerdonatorperks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ClimbListener
  implements Listener
{
  Map<String, ArrayList<Block>> vineMap = new HashMap<String, ArrayList<Block>>();

  public Main plugin;

  public ClimbListener(Main instance)
  {
    this.plugin = instance;
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event)
  {
    Player player = event.getPlayer();

    if ((Main.climbingPlayers.contains(player.getName()))) {
      BlockFace bf = yawToFace(player.getLocation().getYaw());
      Block block = player.getLocation().getBlock().getRelative(bf);

      if (block.getType() != Material.AIR) {
        for (int i = 0; i < 300; i++) {
          Block temp = block.getLocation().add(0.0D, i, 0.0D).getBlock();
          Block opp = player.getLocation().add(0.0D, i, 0.0D).getBlock();
          Block aboveOpp = opp.getLocation().add(0.0D, 1.0D, 0.0D).getBlock();
          int counter = 0;
          for (int k = 0; k < Main.noVineBlocks.size(); k++) {
            if ((temp.getType() != Material.AIR) && (temp.getTypeId() != ((Integer)Main.noVineBlocks.get(k)).intValue()))
              counter++;
          }
          if ((counter != Main.noVineBlocks.size()) || ((opp.getType() != Material.AIR) && (opp.getType() != Material.LONG_GRASS) && (opp.getType() != Material.YELLOW_FLOWER) && (opp.getType() != Material.RED_ROSE))) break;
          if (aboveOpp.getType() == Material.AIR) {
            player.sendBlockChange(opp.getLocation(), Material.VINE, (byte)0);
            addVines(player, opp);
          }
          player.setFallDistance(0.0F);
        }

      }
      else
      {
        for (int i = 0; i < getVines(player).size(); i++) {
          player.sendBlockChange(((Block)getVines(player).get(i)).getLocation(), Material.AIR, (byte)0);
        }
        getVines(player).clear();
      }
    }
  }

  public ArrayList<Block> getVines(Player player)
  {
    if (this.vineMap.containsKey(player.getName())) {
      return (ArrayList<Block>)this.vineMap.get(player.getName());
    }
    ArrayList<Block> temp = new ArrayList<Block>();
    return temp;
  }

  public void setVines(Player player, ArrayList<Block> vines)
  {
    this.vineMap.put(player.getName(), vines);
  }

  public void addVines(Player player, Block vine)
  {
    ArrayList<Block> updated = new ArrayList<Block>();
    updated = getVines(player);
    updated.add(vine);
    setVines(player, updated);
  }

  public BlockFace yawToFace(float yaw) {
    BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
    return axis[(java.lang.Math.round(yaw / 90.0F) & 0x3)];
  }
}