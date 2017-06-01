package com.acercraft.acerdonatorperks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.neatmonster.nocheatplus.checks.moving.MovingData;

public class ClimbCommand implements CommandExecutor{
	
	Main plugin;
	
	public ClimbCommand(Main instance){
		this.plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command only works in-game.");
			return false;
		}
		Player player = (Player)sender;

		if ((!player.hasPermission("acerdonatorperks.climb"))) {
			sender.sendMessage(ChatColor.DARK_RED + "You are not authorized to do that.");
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("climb")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("on")) {
					if (!Main.climbingPlayers.contains(player.getName())) {
						Main.climbingPlayers.add(player.getName());
						player.sendMessage(ChatColor.GRAY + "You have turned on wall climbing.");
					}
					else {
						player.sendMessage(ChatColor.RED + "You already have wall climbing turned on.");
					}
				} else if (args[0].equalsIgnoreCase("off")) {
					if (Main.climbingPlayers.contains(player.getName())) {
						Main.climbingPlayers.remove(player.getName());
						for (int i = 0; i < plugin.alisten.getVines(player).size(); i++) {
							player.sendBlockChange(((Block)plugin.alisten.getVines(player).get(i)).getLocation(), Material.AIR, (byte)0);
							MovingData.removeData(player.getName());
						}
						plugin.alisten.getVines(player).clear();
						player.sendMessage(ChatColor.GRAY + "You have turned off wall climbing.");
					}
					else {
						player.sendMessage(ChatColor.RED + "You already have wall climbing turned off.");
					}
				} else if (args[0].equalsIgnoreCase("dev")) {
					player.sendMessage(ChatColor.GREEN + "WallClimber was made by Tooner101.");
					player.sendMessage(ChatColor.WHITE + "For more cool plugins go to " + ChatColor.AQUA + "http://dev.bukkit.org/profiles/Tooner101/");
				}
				else {
					sender.sendMessage(ChatColor.RED + "You can either turn climbing on or off.");
				}
			} else { if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Not enough arguments.");
				return true;
			}

			sender.sendMessage(ChatColor.RED + "Too many arguments.");
			}
			return true;
		}
		return false;
	}

}
