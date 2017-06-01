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

public class NickNameCommand implements CommandExecutor {

	private AcerTag plugin;

	public NickNameCommand(AcerTag instance){
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String l, String[] args){
		if(command.getName().equalsIgnoreCase("acernick")){
			if(Util.hasPerm((Player)sender, "acernick.*")){
			if(args.length == 0){
				sender.sendMessage(Util.parseColors("&6Change your nickname with &c/acernick {nickname}."));
				sender.sendMessage(Util.parseColors("&6Please see the rules with &c/acernick rules."));
			}

			if(args.length == 1){
				if((sender instanceof Player)){
					
					Player player = (Player)sender;
					
					if(args[0].equalsIgnoreCase("rules")){
						sender.sendMessage(this.plugin.config.getRulesLineOne());
						sender.sendMessage(this.plugin.config.getRulesLineTwo());
						sender.sendMessage(this.plugin.config.getRulesLineThree());
						sender.sendMessage(this.plugin.config.getRulesLineFour());
						return true;
					}
					
					List<String> BlackList = this.plugin.config.getBlackList();
					
					for (int i = 0; i < BlackList.size(); i++) {
						String blacklistmessage = BlackList.get(i).toLowerCase();
						
						if(blacklistmessage.contains(args[0].toLowerCase())){
							player.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " You may not use a word that has been blacklisted");
							return true;
						}
					}
						
						String strippedColors = Util.stripColors(args[0]);
						
						if(strippedColors.length() < this.plugin.config.getMinimumNickNameLength()){
							player.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " Your nickname must not contain " + String.valueOf(this.plugin.config.getMinimumNickNameLength()) + " letters or more");
						}
						else if(strippedColors.length() > this.plugin.config.getMaximumNickNameLength()){
								player.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.RED + " Your nickname must not contain " + String.valueOf(this.plugin.config.getMaximumNickNameLength()) + " letters or more");
							}
							else {
								Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "nick " + player.getName() + " " + args[0]);
								player.sendMessage(this.plugin.config.getMessagePrefix() + ChatColor.GREEN + " You succesfuully set your nickname to " + args[0]);
								
							}

					
				}
				else{
					this.plugin.log.info("You have to be a player to run this command");
					return false;
				}
			}
		}else{
			sender.sendMessage(ChatColor.RED + "Sorry you dont have permission to run this command");
		}
	
		}
		return false;
	}
}
