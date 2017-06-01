package com.acercraft.AcerGun;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;

public class PluginPlayerListener
implements Listener
{
	private main plugin;

	public PluginPlayerListener(main plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		GunPlayer gp = this.plugin.getGunPlayer(event.getPlayer().getName());
		if (gp == null)
			this.plugin.gunplayers.add(new GunPlayer(this.plugin, event.getPlayer()));
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		GunPlayer gp = this.plugin.getGunPlayer(event.getPlayer().getName());
		if (gp == null)
			this.plugin.gunplayers.remove(gp);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Action action = event.getAction();
		Player player = event.getPlayer();
		if ((event.getAction() == Action.LEFT_CLICK_AIR) || (action.equals(Action.LEFT_CLICK_BLOCK)) || (action.equals(Action.RIGHT_CLICK_AIR)) || (action.equals(Action.RIGHT_CLICK_BLOCK))) {
			String clickType = "left";
			if ((action.equals(Action.RIGHT_CLICK_AIR)) || (action.equals(Action.RIGHT_CLICK_BLOCK)))
				clickType = "right";
			Faction otherFaction = Board.getFactionAt(new FLocation(player.getLocation()));
			
			if(!otherFaction.isSafeZone()){
				GunPlayer gp = this.plugin.getGunPlayer(player.getName());
				if ((gp != null) && 
						(gp.globalcooldown <= 1)) {
					gp.fire(clickType);
					try {
						gp.globalcooldown = gp.fireWith.reloadTime;
					} catch (Exception e) {
						gp.globalcooldown = 6;
					}
				}
			}else{
				if(player.getItemInHand() == new ItemStack(Material.DIAMOND_HOE, 1))
					player.sendMessage(ChatColor.GRAY + "["+ChatColor.AQUA + "AcerGun"+ChatColor.GRAY+"] " + ChatColor.RED + "You can not fire a gun/grenade while in safezone!");
				
			}

		}
	}

}