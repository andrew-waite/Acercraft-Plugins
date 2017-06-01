package com.acercraft.ACPAdmin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acercraft.ACPAdmin.Config.Message;
import com.acercraft.ACPAdmin.Util.BanType;
import com.acercraft.ACPAdmin.Util.User;
import com.acercraft.ACPAdmin.Util.Util;

public class BanCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command command, String l, String[] args){
		if(command.getName().equalsIgnoreCase("ban")){
			if(Util.hasPerm((Player)sender, "ACPAdmin.ban")){
			    User user = new User(args[0]);
			    StringBuilder b = new StringBuilder();
			        for (int i = 1; i < args.length; i++) {
			          if (i != 1)
			            b.append(" ");
			          b.append(args[i]);
			        }
			        String banReason = b.toString();
			    user.banPlayer(ChatColor.GOLD + "[Ban] " + ChatColor.GREEN + banReason, sender);
			    user.setBanReason(banReason);
			    user.setBanType(BanType.Ban);
			    
			    for (Player player : Bukkit.getServer().getOnlinePlayers()){
				    if(Util.hasPerm(player, "ACPAdmin.ban.recieve")){
					player.sendMessage(ChatColor.RED + sender.getName() + " banned " + ChatColor.RED + args[0] + " for: " + 
					" Reason: " + ChatColor.GREEN + banReason);
				    }
				}
			}
			else
			{
			sender.sendMessage(Message.NO_PERM.toString());
			}
			return true;
		}
		return false;
	}

}