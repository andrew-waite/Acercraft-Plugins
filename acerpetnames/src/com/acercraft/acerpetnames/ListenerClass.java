package com.acercraft.acerpetnames;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerClass
  implements Listener
{
  @EventHandler
  void entitySelect(PlayerInteractEntityEvent event)
  {
    Player player = event.getPlayer();
    Entity entity = event.getRightClicked();

    for (int i = 0; i < Main.rplayers.size(); i++)
      if (((RenamingPlayer)Main.rplayers.get(i)).player == player)
        if ((entity.getType() == EntityType.WOLF) || (entity.getType() == EntityType.OCELOT)) {  	  
        	  if(((Tameable)entity).getOwner() == player)
          {
            Main.rename(entity, ((RenamingPlayer)Main.rplayers.get(i)).name);

            Main.rplayers.remove(i);
            player.sendMessage(ChatColor.GREEN + "Animal Renamed!");
            event.setCancelled(true);
          } else {
            player.sendMessage(ChatColor.RED + "This isn't your pet!");
          }
        }
        else player.sendMessage(ChatColor.RED + "You must be looking at a wolf or ocelot!");
  }

  @EventHandler
  void playerLeave(PlayerQuitEvent event)
  {
    Player player = event.getPlayer();
    for (int i = 0; i < Main.rplayers.size(); i++)
      if (((RenamingPlayer)Main.rplayers.get(i)).player == player)
        Main.rplayers.remove(i);
  }
  
  public String parseColors(String message){
	return ChatColor.translateAlternateColorCodes('&', message);
	  
  }
}