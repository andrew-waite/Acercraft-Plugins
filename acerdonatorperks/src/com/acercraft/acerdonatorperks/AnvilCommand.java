package com.acercraft.acerdonatorperks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnvilCommand implements CommandExecutor{


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("anvil")) {
			if(hasPerm((Player)sender, "acerdonatorperks.anvil")){
			Player player =  (Player)sender;
		    AnvilUtil au = new AnvilUtils().getPlayer(player);
		    au.openAnvil();
			}
			else{
				sender.sendMessage(ChatColor.RED + "You do not have permission to run that command");
			}
		}
		return false;

	}
	
	public boolean hasPerm(Player player, String node){
		return player.hasPermission(node);
		
	}
}
