package com.acercraft.AcerExplosions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class AcerExplosions extends JavaPlugin implements Listener
{

	public ArrayList<Material> disallowedBlocks = new ArrayList<Material>();
	List<String> entityList = new ArrayList<String>();

	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new AcerExplosions(),
				this);
		this.disallowedBlocks.clear();
		this.disallowedBlocks.add(Material.TNT);
		this.disallowedBlocks.add(Material.PISTON_BASE);
		this.disallowedBlocks.add(Material.PISTON_EXTENSION);
		this.disallowedBlocks.add(Material.PISTON_MOVING_PIECE);
		this.disallowedBlocks.add(Material.PISTON_STICKY_BASE);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockExplode(EntityExplodeEvent e)
	{
		if (e.isCancelled()) return;
		if (e.blockList().isEmpty()) return;
		e.setYield(0.0F);
		double x = 0.0D;
		double y = 0.0D;
		double z = 0.0D;
		Location eLoc = e.getLocation();
		World w = eLoc.getWorld();
		for (int i = 0; i < e.blockList().size(); i++)
		{
			Block b = (Block) e.blockList().get(i);
			Location bLoc = b.getLocation();
			if (!this.disallowedBlocks.contains(b.getType()))
			{
				x = bLoc.getX() - eLoc.getX();
				y = bLoc.getY() - eLoc.getY() + 0.5D;
				z = bLoc.getZ() - eLoc.getZ();
				@SuppressWarnings("deprecation")
				FallingBlock fb = w.spawnFallingBlock(bLoc, b.getType(),
						b.getData());
				fb.setDropItem(false);
				fb.setVelocity(new Vector(x, y, z));
				this.addEntityUUID(fb.getUniqueId());
			}
		}
	}

	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event)
	{
		if (event.getEntity() instanceof FallingBlock)
		{
			if (this.containsBlock(event.getEntity().getUniqueId()))
			{
				event.setCancelled(true);
				this.removeEntityBlock(event.getEntity().getUniqueId());
			}
			else
			{

			}
		}
	}

	public void addEntityUUID(UUID id)
	{
		String uuid = id.toString();
		this.entityList.add(uuid);
	}

	public void removeEntityBlock(UUID id)
	{
		String uuid = id.toString();
		if (this.entityList.contains(uuid)) this.entityList.remove(uuid);
	}

	public boolean containsBlock(UUID id)
	{
		String uuid = id.toString();
		if (this.entityList.contains(uuid))
		{
			return true;
		}
		return false;
	}
}