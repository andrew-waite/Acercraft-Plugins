package com.acercraft.ACPAdmin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acercraft.ACPAdmin.Util.BanType;
import com.acercraft.ACPAdmin.Util.User;
import com.acercraft.ACPAdmin.Util.Util;

public class TempBanCommand implements CommandExecutor
{

	public boolean onCommand(CommandSender sender, Command command, String l, String[] args)
	{
		if (command.getName().equalsIgnoreCase("tempban"))
		{
			
			if (args.length < 3)
			{
				sender.sendMessage("/tempban <Player> <Time> <Reason>");
				return true;
			}
			
			User user = new User(args[0]);
			
			String time = args[1];
			
		        StringBuilder b = new StringBuilder();
		        for (int i = 2; i < args.length; i++) {
		          if (i != 1)
		            b.append(" ");
		          b.append(args[i]);
		        }
		        
		        String banReason = b.toString();	
			
			long banTimestamp = Util.parseDateDiff(time, true);

			String senderName = (sender instanceof Player) || (sender instanceof OfflinePlayer) ? sender.getName() : "Console";	
			
			String banTime = Util.formatDateDiff(banTimestamp);
			
			user.setBanTimeStamp(banTimestamp);
			
			user.banPlayer(ChatColor.GOLD + "[TempBan] " + ChatColor.GREEN + banReason, sender);
			
			user.setBanReason(banReason);
			
			user.setBanType(BanType.Temp_Ban);
			
			for (Player player : Bukkit.getServer().getOnlinePlayers()){
			    if(Util.hasPerm(player, "ACPAdmin.ban.recieve")){
				player.sendMessage(ChatColor.RED + senderName + " banned " + ChatColor.RED + args[0] + " for: " + 
				ChatColor.GREEN + banTime + " Reason: " + ChatColor.GREEN + banReason);
			    }
			}
			
			return true;
		}
		
		return false;
	}
}
