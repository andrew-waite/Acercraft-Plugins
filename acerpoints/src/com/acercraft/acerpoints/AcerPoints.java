package com.acercraft.acerpoints;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.skills.utilities.SkillType;

public class AcerPoints extends JavaPlugin {
	
	String prefix;
	public ConfigHandler config;
	public void onEnable() {
		this.saveDefaultConfig();
		config = new ConfigHandler(this);
		prefix = ChatColor.BLUE + "[AcerPoints] " + ChatColor.GREEN;

		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
			public void run() {
				for(Player p : getServer().getOnlinePlayers()) {
					String name = p.getName();
					if(config.getPoints(name) != 0) {
						p.sendMessage(prefix + "You have AcerPoints to spend! Use " + ChatColor.RED + "/ap " + ChatColor.GREEN + "for more info!");
					}
					if(p.hasPermission("acerpoints.redeem") && config.getRedeemedRank(name) == null) {
						p.sendMessage(prefix + "You haven't redeemed your AcerPoints for this month yet! Use " + ChatColor.RED + "/ap redeem [rank]" + ChatColor.GREEN + "!");
					}
				}
			}
		}, 20*60*5, 20*60*5);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("acerpoints") || cmd.getName().equalsIgnoreCase("ap")) {
			if(args.length == 0) {
				sendHelp((Player) sender);
			}
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("check")) {
					if(sender instanceof Player) {
						if(sender.hasPermission("acerpoints.check")) {
							Player player = (Player) sender;
							int points = config.getPoints(player.getName());
							sender.sendMessage(prefix + "You have " + ChatColor.RED + points + ChatColor.GREEN + " AcerPoints!");
						}
						else {
							sender.sendMessage(prefix + "You do not have permission to do this!");
						}
					}
					else {
						sender.sendMessage("[AcerPoints] You must be a player to use this command!");
					}
				}
				else if(args[0].equalsIgnoreCase("top")) {
					if(sender.hasPermission("acerpoints.top")) {
						List<String> top = config.getAllHolders();
						sender.sendMessage(prefix + "Top AcerPoints holders:");
						int i = 0;
						for(String s : top) {
							if(i < 5) {
								int display = i + 1;
								sender.sendMessage(prefix + ChatColor.RED + display + ". " + ChatColor.GREEN + fixPlayerName(s) + " with " + config.getPoints(s) + " AcerPoints");
								i++;
							}
						}
						i = 0;
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else if(args[0].equalsIgnoreCase("reset")) {
					if(sender.hasPermission("acerpoints.reset")) {
						sender.sendMessage(prefix + "All acerpoints and McMMO reset!");
						for(String s : config.getAllHolders()) {
							getConfig().set(s, null);
						}
						getConfig().set("Has_Redeemed", null);
						saveConfig();
						config.resetMcMMO();
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else {
					if(sender instanceof Player) {
						sender.sendMessage(prefix + "Invalid arguments or number of arguments!");
					}
					else {
						sender.sendMessage("[AcerPoints] Invalid arguments or number of arguments!");
					}
				}
			}
			else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("check")) {
					if(sender.hasPermission("acerpoints.check.others")) {
						String name = args[1];
						if(getServer().getOfflinePlayer(name).hasPlayedBefore()) {
							int points = config.getPoints(name);
							sender.sendMessage(prefix + ChatColor.RED + name + ChatColor.GREEN + " has " + ChatColor.RED + points + ChatColor.GREEN + " AcerPoints!");
						}
						else {
							sender.sendMessage(prefix + "That person has never played before!");
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do that!");
					}
				}
				else if(args[0].equalsIgnoreCase("redeem")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						int amount = config.getPointsForGroup(args[1]);
						if(amount != -1) {
							if(sender.hasPermission("acerpoints.redeem." + args[1])) {
								if(config.getRedeemedRank(p.getName()) == null) {
									config.addPoints(p.getName(), amount);
									getConfig().set("Has_Redeemed." + p.getName().toLowerCase(), args[1]);
									saveConfig();
									p.sendMessage(prefix + "You have redeemed your points for this month!");
								}
								else if(config.getRedeemedRank(p.getName()).equalsIgnoreCase(args[1])){
									p.sendMessage(prefix + "You have already redeemed your points for this month!");
								}
								else {
									int amountForPrevious = config.getPointsForGroup(config.getRedeemedRank(p.getName()));
									int newAmount = amount - amountForPrevious;
									config.addPoints(p.getName(), newAmount);
									getConfig().set("Has_Redeemed." + p.getName().toLowerCase(), args[1]);
									saveConfig();
									p.sendMessage(prefix + "You have been given the difference between your previous redeem and this one!");
								}
							}
							else {
								sender.sendMessage(prefix + "You do not have permission to redeem those points!");
							}
						}
						else {
							sender.sendMessage(prefix + "Invalid group!");
						}
					}
				}
				else {
					sender.sendMessage(prefix + "Invalid arguments or number of arguments!");
				}
			}
			else if(args.length == 3) {
				if(args[0].equalsIgnoreCase("give")) {
					if(sender.hasPermission("acerpoints.give")) {
						// /acerpoints give [name] [amount]
						try {
							int amount = Integer.parseInt(args[2]);
							OfflinePlayer player = getServer().getOfflinePlayer(args[1]);
							if(player.hasPlayedBefore()) {
								config.addPoints(args[1], amount);

								if(player.isOnline()) {
									((Player)player).sendMessage(prefix + "You have been granted " + ChatColor.RED + amount + ChatColor.GREEN + " McMMO points!");
								}

								if(sender instanceof Player) {
									sender.sendMessage(prefix + ChatColor.RED + amount + ChatColor.GREEN + " points have been granted to " + ChatColor.RED + args[1] + ChatColor.GREEN + "!");
								}
								else {
									sender.sendMessage("[AcerPoints] " + amount + " points have been granted to " + args[1] + "!");
								}
							}
							else {
								config.addPoints(args[1], amount);
								if(sender instanceof Player) {
									sender.sendMessage(prefix + ChatColor.RED + args[1] + ChatColor.GREEN + " has never played before!");
									sender.sendMessage(prefix + ChatColor.RED + amount + ChatColor.GREEN + " points have been granted anyways, please use /acerpoints sub [name] [amount] if you wish to remove them!");
								}
								else {
									sender.sendMessage("[AcerPoints] " + args[1] + " has never played before! Points were still granted.");
								}
							}
						}
						catch(NumberFormatException e) {
							sender.sendMessage(prefix + "Invalid amount!");
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else if(args[0].equalsIgnoreCase("spend")) {
					// /acerpoints spend [skill] [amount]
					if(sender instanceof Player) {
						Player player = (Player) sender;
						if(sender.hasPermission("acerpoints.spend")) {
							SkillType skill = SkillType.getSkill(args[1]);
							
								if(skill == null) {
									sender.sendMessage(prefix + "Invalid skill!");
								}else
							
							if(skill.equals(SkillType.AXES) || skill.equals(SkillType.SWORDS) || skill.equals(SkillType.UNARMED)){
								sender.sendMessage(prefix + this.config.getBannedSkillMessage());
							}
							else {
								try {
									int amount = Integer.parseInt(args[2]);
									int points = config.getPoints(player.getName());
									if(points < amount) {
										sender.sendMessage(prefix + "You do not have enough AcerPoints!");
									}
									else {
										ExperienceAPI.addLevel(player, skill.name(), amount);
										config.subPoints(player.getName(), amount);
										sender.sendMessage(prefix + "You have upgraded your skill of " + ChatColor.RED + skill.toString() + ChatColor.GREEN + " by " + ChatColor.RED + amount + ChatColor.GREEN + " levels!");
									}
								}
								catch(NumberFormatException e) {
									sender.sendMessage(prefix + "Invalid amount!");
								}
							}
						}
						else {
							sender.sendMessage(prefix + "You do not have permission to do this!");
						}
					}
					else {
						sender.sendMessage("[AcerPoints] You must be a player to use this command!");
					}
				}
				else if(args[0].equalsIgnoreCase("sub")) {
					// /acerpoints sub [name] [amount]
					if(sender.hasPermission("acerpoints.sub")) {
						try {
							int amount = Integer.parseInt(args[2]);
							config.subPoints(args[1], amount);
						}
						catch(NumberFormatException e) {
							if(sender instanceof Player) {
								sender.sendMessage(prefix + "Invalid amount!");
							}
							else {
								sender.sendMessage("[AcerPoints] Invalid amount!");
							}
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else {
					if(sender instanceof Player) {
						sender.sendMessage(prefix + "Invalid arguments or number of arguments!");
					}
					else {
						sender.sendMessage("[AcerPoints] Invalid arguments or number of arguments!");
					}
				}
			}
			else {
				if(sender instanceof Player) {
					sender.sendMessage(prefix + "Invalid arguments or number of arguments!");
				}
				else {
					sender.sendMessage("[AcerPoints] Invalid arguments or number of arguments!");
				}
			}
		}
		return true;
	}

	public void sendHelp(Player player) {		
		player.sendMessage(prefix + "/acerpoints check -- Check how many AcerPoints you have");
		player.sendMessage(prefix + "/acerpoints spend [skill] [amount] -- Spend [amount] AcerPoints on [skill] mcMMO skill");
		player.sendMessage(prefix + "/acerpoints give [name] [amount] -- Give [amount] AcerPoints to [name] player");
		player.sendMessage(prefix + "/acerpoints sub [name] [amount] -- Subtract [amount] AcerPoints from [name] player");
		player.sendMessage(prefix + "/acerpoints top -- See the players with the most AcerPoints");
		player.sendMessage(prefix + "/acerpoints redeem -- Redeem your monthly AcerPoints");
	}

	public String fixPlayerName(String s) {
		if(getServer().getPlayer(s) != null) return getServer().getPlayer(s).getName();
		return getServer().getOfflinePlayer(s).getName();
	}
}
