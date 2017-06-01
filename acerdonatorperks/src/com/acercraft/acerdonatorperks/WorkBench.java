package com.acercraft.acerdonatorperks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorkBench implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("workbench")){
			if (sender instanceof Player) {
				if (hasPerm((Player)sender, "acerdonatorperks.workbench")) {
					System.out.println("Command workbench works");
					((Player) sender).openWorkbench(null, true);
				} else {
					((Player)sender).sendMessage(ChatColor.RED + "Sorry you do not have permission");
				}
				return true;
			}
			return true;
		}
		return false;
	}
	
    public boolean hasPerm(Player p, String node){
    	return p.hasPermission(node);
    }

}

