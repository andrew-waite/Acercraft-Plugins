package com.acercraft.acerothers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class AcerResidentCommand implements CommandExecutor{
	
	AcerOthers plugin;
	public AcerResidentCommand(AcerOthers instance){
		this.plugin = instance;
	}
	
	String PREFIX = ChatColor.GREEN + "["+ChatColor.BLUE + "AcerRes" + ChatColor.GREEN + "] ";
	
	public boolean onCommand(CommandSender sender, Command c, String l, String[] args){
		if(c.getName().equalsIgnoreCase("acerresident")){
			if(args.length == 0){
				sender.sendMessage(this.PREFIX + ChatColor.RED + "Correct format: /acerresident <player>");			
			}


			if(args.length == 1){

				if(Bukkit.getServer().getOfflinePlayer(args[0]).hasPlayedBefore()){

					if(Bukkit.getServer().getPlayer(args[0]) != null){
						Player player = Bukkit.getServer().getPlayer(args[0]);
						FPlayer fplayer = FPlayers.i.get(player);
						

						
						int playerbalance = (int)plugin.economy.getBalance(player.getName());
						double playerspower = fplayer.getPower();
						
						String[] group = plugin.permission.getPlayerGroups(player);

						if(fplayer.getFaction().isNone()){
							sender.sendMessage(this.PREFIX + ChatColor.DARK_PURPLE + "Faction: " + ChatColor.GREEN + "This player is not in a faction");
						}else{
							sender.sendMessage(this.PREFIX + ChatColor.DARK_PURPLE + "Faction: " + ChatColor.GREEN + fplayer.getFaction().getTag());
						}

						if(player.isOnline()){
							sender.sendMessage(this.PREFIX + ChatColor.DARK_PURPLE + "LastOnline: " +ChatColor.GREEN + "This player is online now :D");
						}
						
						sender.sendMessage(this.PREFIX + ChatColor.DARK_PURPLE + "Balance: " + ChatColor.GREEN + String.valueOf(playerbalance));
						sender.sendMessage(this.PREFIX + ChatColor.DARK_PURPLE + "Power: " + ChatColor.GREEN + (int)playerspower);
						
						StringBuilder b = new StringBuilder();
						for(String string : group){
							b.append(string);
							b.append(", ");
						}
						
						sender.sendMessage(this.PREFIX + ChatColor.DARK_PURPLE + "Rank: " + ChatColor.GREEN + b);
					}
					else{
						OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[0]);
						long playerlastonline = plugin.ess.getOfflineUser(player.getName()).getLastLogout();
							sender.sendMessage(this.PREFIX + ChatColor.DARK_PURPLE + "LastOnline: " + ChatColor.GREEN + String.valueOf(Util.formatDateDiff(playerlastonline) + " ago"));
							sender.sendMessage(this.PREFIX + ChatColor.GREEN + "To see more info about the player, check this when they are online");
						
					
					}

				}
				
				else{
					sender.sendMessage(this.PREFIX + ChatColor.RED + "That person has never played before");
				}
			}
			
		}
		return false;
	}

}
