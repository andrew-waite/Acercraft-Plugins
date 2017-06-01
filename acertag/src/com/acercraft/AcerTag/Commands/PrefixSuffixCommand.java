package com.acercraft.AcerTag.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acercraft.AcerTag.AcerTag;
import com.acercraft.AcerTag.Util.Util;

public class PrefixSuffixCommand implements CommandExecutor{
	
	AcerTag plugin;
	
	public PrefixSuffixCommand(AcerTag instance){
		this.plugin = instance;
	}
	

	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		if (c.getName().equalsIgnoreCase("acertag"))
		{
			Player p = (Player)s;
			if (args.length < 2 || args.length > 2) {
				p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " Invalid arguments, correct format is " + ChatColor.AQUA + "/acertag [Prefix|Suffix] [TAG]");
				
				return true;
			}

			List<String> blacklist = this.plugin.config.getBlackList();

			for (int i = 0; i < blacklist.size(); i++) {
				String blacklistmessage = (String)blacklist.get(i).toLowerCase();
				if(blacklistmessage.contains(args[1].toLowerCase())){
					p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " You may not use a word that has been blacklisted");
					return true;
				}

			}


					if (args[0].equalsIgnoreCase("prefix")) {
						if (Util.hasPerm(p, "acertag.modify.prefix")) {
							String argslength = Util.stripColors(args[1]);
							if (argslength.length() < 4) {
								p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " Your preifx must contain 4 letters or more");
							} else
								if(argslength.length()> 10){
									p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " Your preifx must not contain 10 letters or more");
								}
								else {
									Bukkit.dispatchCommand(this.plugin.getServer().getConsoleSender(), "pex user " + p.getName() + " prefix " + "&0["+args[1]+"&0]");
									p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.GREEN + " You succesfuully set your prefix to " + Util.parseColors(args[1]));
									
								}
						}
						else p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " You do not have the permission to run this command");

					}

			if (args[0].equalsIgnoreCase("suffix")) {
				if (Util.hasPerm(p, "acertag.modify.suffix")) {
					String argslength = Util.stripColors(args[1]);
					if (argslength.length() < 4) {
						p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " Your suffix must contain 4 letters or more");

					}else
						if(argslength.length() > 10){
							p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " Your suffix must not contain 10 letters or more");
						}
						else {
							Bukkit.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), "pex user " + p.getName() + " suffix " + "&0["+args[1]+"&0]");
							p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.GREEN + " You succesfuully set your suffix to " + Util.parseColors(args[1]));
							return true;
						}
				}
				else p.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " You do not have the permission to run this command");

			}

			return true;
		}

		return false;
	}

}
