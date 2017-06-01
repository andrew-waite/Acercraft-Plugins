package com.acercraft.acerdonatorperks;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AcerNearCommand implements CommandExecutor{
	
	Main plugin;

	public AcerNearCommand(Main instance){
		this.plugin = instance;
	}
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("acernear")) {
			Player p = (Player)sender;
			if (!hasPerm(p, "acerdonatorperks.acernear")) {
				p.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command is only available to players!");
				return true;
			}
			if (args.length < 1) {
				double radius = this.plugin.getConfig().getDouble("defaultNear");
				int amount = 0;
				ArrayList<Player> nearbyPlayers = new ArrayList<Player>();

				for (Entity entity : p.getNearbyEntities(radius, radius, radius)) {
					if (!(entity instanceof Player)) continue;
					Player newPlayer = (Player) entity;

					if(newPlayer.getName().equals("Drew1080") 
							|| newPlayer.getName().equals("Mineacer") 
							|| newPlayer.getName().equals("Moushell") 
							|| newPlayer.getName().equals("aussieandy") 
							|| newPlayer.getName().equals("MLG_Guns982")
							|| newPlayer.getName().equals("Dead_Night398")){
				
					}else{
						nearbyPlayers.add(newPlayer);
						amount++;
					}
				}

				for (int i = 0; i < nearbyPlayers.size(); i++) {
					Player playerOne = nearbyPlayers.get(i);
					double dist = p.getLocation().distanceSquared(playerOne.getLocation());
					p.sendMessage(ChatColor.GRAY + playerOne.getDisplayName() + ": " + ChatColor.WHITE + Math.sqrt(dist));
				}

				nearbyPlayers.clear();


				if (amount == 0) {
					p.sendMessage(ChatColor.RED + "No one nearby!");
					return true;
				}
				return true;
			}
		}
		return false;
	}

	public boolean hasPerm(Player p, String node){
		return p.hasPermission(node);
	}


}
