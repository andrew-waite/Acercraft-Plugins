package com.acercraft.acerdonatorperks;

import net.minecraft.server.v1_5_R3.ContainerAnvil;
import net.minecraft.server.v1_5_R3.EntityHuman;
import net.minecraft.server.v1_5_R3.EntityPlayer;
import net.minecraft.server.v1_5_R3.Packet100OpenWindow;
import net.minecraft.server.v1_5_R3.PlayerInventory;

import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class AnvilUtil {
	
	  private EntityPlayer ep;
	  private PlayerInventory pi;
	
	public AnvilUtil(Player player){
		this.ep = ((CraftPlayer) player).getHandle();
		this.pi = ((CraftPlayer)player).getHandle().inventory;
	}
	
	
	  public void openAnvil() {
		    AnvilContainer container = new AnvilContainer(this.ep);

		    int c = this.ep.nextContainerCounter();
		    this.ep.playerConnection.sendPacket(new Packet100OpenWindow(c, 8, "Repairing", 9, true));
		    this.ep.activeContainer = container;
		    this.ep.activeContainer.windowId = c;
		    this.ep.activeContainer.addSlotListener(this.ep);
		  }
	  
	  public class AnvilContainer extends ContainerAnvil
	  {
	    public AnvilContainer(EntityHuman entity)
	    {
	      super(pi, entity.world, 0, 0, 0, entity);
	    }

	    public boolean a(EntityHuman entityhuman)
	    {
	      return true;
	    }
	  }

}


