package com.acercraft.AcerPerks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acercraft.AcerPerks.AcerPerks;
import com.acercraft.AcerPerks.inventory.InventoryManager;
import com.acercraft.AcerPerks.utils.Utils;

public class ToggleInventoryCommand implements CommandExecutor {
	
	private InventoryManager inventoryManager;
	public AcerPerks plugin;
	
	public ToggleInventoryCommand(AcerPerks plugin, InventoryManager invmanager)
	{
		this.inventoryManager = invmanager;
		this.plugin = plugin;
	}
	
	 public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
	        // make sure the sender is a Player before casting
	        Player player = null;
	        String playerName = null;
	        if (sender instanceof Player) {
	            player = (Player)sender;
	            playerName = player.getName();
	         } else {
	            sender.sendMessage("You must be a player!");
	            return false;
	         }

	        // implement /togglei and /toggleir command.
	        boolean isToggleInvCommand = cmd.getName().equalsIgnoreCase("ti");
	        boolean isReverse = cmd.getName().equalsIgnoreCase("it");

	        if(isToggleInvCommand || isReverse){
	            if(!player.hasPermission("toggle_inventory.toggle")){
	                   Utils. outputError("You don't have permission to toggle inventory.", player);
	                return true;
	            }

	            // help command
	            if (args.length >= 1 && args[0].length() > 0) {
	                                if (args[0].startsWith("h")) {
	                                        player.sendMessage("USAGE1: /ti - toggle inventory like a ring");
	                                        player.sendMessage("USAGE2: /it - toggle inventory like a ring (reverse)");
	                                        player.sendMessage("Advanced: /ti [enable|disable] gamemode - (you can toggle with gamemode)");
	                                        return true;
	                                }
	            }
	            // toggle inventory (/ti or /it, and /ti [n])
	            try {
	                    if(inventoryManager.getSpecialInventoryUsingStatus(playerName)){
	                            inventoryManager.restoreInventory(player);
	                            inventoryManager.setSpecialInventoryUsingStatus(playerName, false);
	                    }else if (args.length >= 1 && args[0].length() > 0) {
	                                    int index = Integer.parseInt(args[0]);
	                                    inventoryManager.toggleInventory(player, index);
	                            }
	                            else{
	                                    inventoryManager.toggleInventory(player, !isReverse);
	                            }
	                                player.sendMessage(inventoryManager.makeInventoryMessage(player) + " inventory toggled.");
	                        } catch(NumberFormatException e){
	                                Utils.outputError("Index must be a number.", player);
	                        } catch (Exception e) {
	                               Utils. outputError(e.getMessage(), player);
	                        }
	            return true;
	        }
			return false;
	 }

}
