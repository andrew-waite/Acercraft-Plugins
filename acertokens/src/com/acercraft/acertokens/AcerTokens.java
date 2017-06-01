package com.acercraft.acertokens;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AcerTokens extends JavaPlugin {
	ConfigHandler config;
	UserHandler users;
	PlayerListener listener;
	MobsHandler mobs;
	LogFilesHandler log;
	String prefix = ChatColor.GREEN + "[" + ChatColor.BLUE + "AcerTokens" + ChatColor.GREEN + "] ";
	private static AcerTokens instance;
	public void onEnable() {
		saveDefaultConfig();
		instance = this;
		config = new ConfigHandler(this);
		users = new UserHandler(this);
		listener = new PlayerListener(this);
		mobs = new MobsHandler();
		log = new LogFilesHandler();
		getServer().getPluginManager().registerEvents(listener, this);

		// Save users every 5 mins
		getServer().getScheduler().runTaskTimer(this, new Runnable() {
			public void run() {
				users.saveUsers();
			}
		}, 300*20, 300*20);
	}

	public static AcerTokens getInstance() {
		return instance;
	}

	public void onDisable() {
		users.saveUsers();
		for(World w : getServer().getWorlds()) {
			for(Chunk c : w.getLoadedChunks()) {
				for(Entity e : c.getEntities()) {
					if(e instanceof LivingEntity) {
						e.remove();
					}
				}
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("acertokens") || cmd.getName().equalsIgnoreCase("atokens") || cmd.getName().equalsIgnoreCase("at")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.BLUE + "====" + ChatColor.GREEN + "AcerTokens" + ChatColor.BLUE + "====");
				sender.sendMessage(ChatColor.BLUE + "Use " + ChatColor.RED + "/atokens" + ChatColor.BLUE + " before any of the following: ");
				sender.sendMessage(ChatColor.RED + "list" + ChatColor.BLUE + " -- See all packages");
				sender.sendMessage(ChatColor.RED + "info [id]" + ChatColor.BLUE + " -- See detailed info on one package");
				sender.sendMessage(ChatColor.RED + "balance (bal) [optional name]" + ChatColor.BLUE + " -- See your AcerTokens balance or see another player's");
				sender.sendMessage(ChatColor.RED + "buy [id]" + ChatColor.BLUE + " -- Buy a package");
				sender.sendMessage(ChatColor.RED + "give [name] [amount]" + ChatColor.BLUE + " -- Give a player some AcerTokens");
				sender.sendMessage(ChatColor.RED + "remove [name] [amount]" + ChatColor.BLUE + " -- Remove some AcerTokens from a player");
				sender.sendMessage(ChatColor.RED + "reload " + ChatColor.BLUE + " -- Reload the config and user files");
				sender.sendMessage(ChatColor.RED + "top " + ChatColor.BLUE + " -- See the players with the most AcerTokens");
				sender.sendMessage(ChatColor.RED + "send [name] [amount] " + ChatColor.BLUE + " -- Send some AcerTokens to another player");
				sender.sendMessage(ChatColor.RED + "lookup [name] [page] " + ChatColor.BLUE + " -- See recent AcerToken related events performed by this player");
			}
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("list")) {
					if(sender.hasPermission("acertokens.list")) {
						List<String> packages = config.getPackages();
						sender.sendMessage(prefix + "Use " + ChatColor.RED + "/atokens info [id] " + ChatColor.GREEN + "for more details on one package");
						sender.sendMessage(prefix + "These are the available packages:");
						sender.sendMessage(ChatColor.RED + "ID" + ChatColor.BLUE + " -- " + ChatColor.GREEN + "Name");
						for(String s : packages) {
							sender.sendMessage(ChatColor.RED + Integer.toString(config.getID(s)) + ChatColor.BLUE + " -- " + ChatColor.GREEN + s);
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")) {
					if(sender.hasPermission("acertokens.balance")) {
						if(sender instanceof Player) {
							Player player = (Player) sender;
							int tokens = users.getTokens(player.getName());
							player.sendMessage(prefix + "You have " + ChatColor.RED + tokens + ChatColor.GREEN + " AcerTokens!");
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else if(args[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("acertokens.reload")) {
						reloadConfig();
						users.reloadUsers();
						sender.sendMessage(prefix + "Reloaded!");
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else if(args[0].equalsIgnoreCase("top")) {
					if(sender.hasPermission("acertokens.top")) {
						List<String> top = users.getTopHolders();
						sender.sendMessage(prefix + "Top AcerTokens holders:");
						int i = 0;
						for(String s : top) {
							if(i < 5) {
								int display = i + 1;
								sender.sendMessage(prefix + ChatColor.RED + display + ". " + ChatColor.GREEN + fixPlayerName(s) + " with " + users.getTokens(s) + " AcerTokens");
								i++;
							}
						}
						i = 0;
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else {
					sender.sendMessage(prefix + "Invalid arguments!");
				}
			}
			else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("info")) {
					if(sender.hasPermission("acertokens.info")) {
						if(isValidInt(args[1]) && isValidPackage(Integer.parseInt(args[1]))) {
							int id = Integer.parseInt(args[1]);
							sender.sendMessage(ChatColor.BLUE + "====" + ChatColor.GREEN + "AcerTokens" + ChatColor.BLUE + "====");
							sender.sendMessage(prefix + "Use " + ChatColor.RED + "/atokens buy [id] " + ChatColor.GREEN + "to buy this package");
							sender.sendMessage(ChatColor.RED + "ID: " + ChatColor.BLUE + id);
							sender.sendMessage(ChatColor.RED + "Name: " + ChatColor.BLUE + config.getName(id));
							sender.sendMessage(ChatColor.RED + "Description: " + ChatColor.BLUE + config.getDescription(id));
							sender.sendMessage(ChatColor.RED + "Cost: " + ChatColor.BLUE + config.getCost(id));
						}
						else {
							sender.sendMessage(prefix + "You must enter a valid package ID!");
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")) {
					if(sender.hasPermission("acertokens.balance.others")) {
						OfflinePlayer player = getServer().getOfflinePlayer(args[1]);
						if(player.hasPlayedBefore()) {
							int cur = users.getTokens(args[1]);
							sender.sendMessage(prefix + ChatColor.RED + args[1] + ChatColor.GREEN + " has " + ChatColor.RED + cur + ChatColor.GREEN + " AcerTokens!");
						}
						else {
							sender.sendMessage(prefix + "That person has never played on this server before!");
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else if(args[0].equalsIgnoreCase("buy")) {
					if(isValidInt(args[1]) && isValidPackage(Integer.parseInt(args[1]))) {
						if(sender instanceof Player) {
							if(sender.hasPermission("acertokens.buy")) {
								String name = ((Player) sender).getName();
								int cost = config.getCost(Integer.parseInt(args[1]));
								int cur = users.getTokens(name);
								if(cur >= cost) {
									users.removeTokens(name, cost);
									List<String> commands = config.getCommands(Integer.parseInt(args[1]));
									for(String s : commands) {
										s = s.replaceAll("\\{name}", name);
										getServer().dispatchCommand(getServer().getConsoleSender(), s);
									}
									sender.sendMessage(prefix + "You have successfully purchased " + ChatColor.RED + config.getName(Integer.parseInt(args[1])));
									log.getTokenUser(name).logSpend(cost, config.getName(Integer.parseInt(args[1])));
								}
								else {
									sender.sendMessage(prefix + "You cannot afford this!");
								}
							}
							else {
								sender.sendMessage(prefix + "You do not have permission to do this!");
							}
						}
						else {
							sender.sendMessage(prefix + "You must be a player to use this command!");
						}
					}
					else {
						sender.sendMessage(prefix + "You must enter a valid package ID!");
					}
				}
				else if(args[0].equalsIgnoreCase("lookup")) {
					if(sender.hasPermission("acertokens.lookup")) {
						TokensUser user = log.getTokenUser(args[1]);
						if(user.getPage(1).size() != 1) {
							for(String s : user.getPage(1)) {
								sender.sendMessage(s);
							}
						}
						else {
							sender.sendMessage(prefix + "That user has no AcerToken events logged!");
					}
				}
				else {
					sender.sendMessage(prefix + "You do not have permission to do this!");
				}
			}
			else {
				sender.sendMessage(prefix + "Invalid arguments!");
			}
		}
		else if(args.length == 3) {
			if(args[0].equalsIgnoreCase("give")) {
				if(isValidInt(args[2])) {
					if(sender.hasPermission("acertokens.give")) {
						users.addTokens(args[1], Integer.parseInt(args[2]));
						sender.sendMessage(prefix + ChatColor.RED + args[1] + ChatColor.GREEN + " has been given " + ChatColor.RED + args[2] + ChatColor.GREEN + " AcerTokens!");
						if(sender instanceof Player) {
							Player p = (Player) sender;
							log.getTokenUser(p.getName()).logGiveTo(Integer.parseInt(args[2]), args[1]);
							log.getTokenUser(args[1]).logGive(Integer.parseInt(args[2]), p.getName());
						}
						else {
							log.getTokenUser(args[1]).logGive(Integer.parseInt(args[2]), "console");
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else {
					sender.sendMessage(prefix +"You must enter a valid amount!");
				}
			}
			else if(args[0].equalsIgnoreCase("remove")) {
				if(isValidInt(args[2])) {
					if(sender.hasPermission("acertokens.remove")) {
						users.removeTokens(args[1], Integer.parseInt(args[2]));
						sender.sendMessage(prefix + ChatColor.RED + args[1] + ChatColor.GREEN + " has had " + ChatColor.RED + args[2] + ChatColor.GREEN + " AcerTokens removed!");
						if(sender instanceof Player) {
							Player p = (Player) sender;
							log.getTokenUser(p.getName()).logRemoveFrom(Integer.parseInt(args[2]), args[1]);
							log.getTokenUser(args[1]).logRemove(Integer.parseInt(args[2]), p.getName());
						}
						else {
							log.getTokenUser(args[1]).logRemove(Integer.parseInt(args[2]), "console");
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else {
					sender.sendMessage(prefix +"You must enter a valid amount!");
				}
			}
			else if(args[0].equalsIgnoreCase("send")) {
				if(sender instanceof Player) {
					if(sender.hasPermission("acertokens.send")) {
						if(isValidInt(args[2])) {
							if(getServer().getPlayer(args[1]) != null) {
								Player p = (Player) sender;
								Player p1 = getServer().getPlayer(args[1]);
								if(users.getTokens(p.getName()) >= Integer.parseInt(args[2])) {
									if(Integer.parseInt(args[2]) < 0){
										sender.sendMessage(prefix + "You can not give negative ammount of acertokens");
										return true;
									}
									users.removeTokens(p.getName(), Integer.parseInt(args[2]));
									users.addTokens(p1.getName(), Integer.parseInt(args[2]));
									p.sendMessage(prefix + "You have sent " + ChatColor.RED + args[2] + ChatColor.GREEN + " AcerTokens to " + ChatColor.RED + args[1] + ChatColor.GREEN + "!");
									p1.sendMessage(prefix + "You have been given " + ChatColor.RED + args[2] + ChatColor.GREEN + " AcerTokens by " + ChatColor.RED + p.getName() + ChatColor.GREEN + "!");
									log.getTokenUser(p.getName()).logTrade(Integer.parseInt(args[2]), p1.getName());
									log.getTokenUser(p1.getName()).logTradedTo(Integer.parseInt(args[2]), p.getName());
								}
								else {
									sender.sendMessage(prefix + "You do not have enough AcerTokens!");
								}
							}
							else {
								sender.sendMessage(prefix + "That person is not online!");
							}
						}
						else {
							sender.sendMessage(prefix +"You must enter a valid amount!");
						}
					}
					else {
						sender.sendMessage(prefix + "You do not have permission to do this!");
					}
				}
				else {
					sender.sendMessage(prefix + "Invalid arguments!");
				}
			}
			else if(args[0].equalsIgnoreCase("lookup")) {
				if(sender.hasPermission("acertokens.lookup")) {
					if(isValidInt(args[2])) {
						TokensUser user = log.getTokenUser(args[1]);
						if(user.getPage(Integer.parseInt(args[2])).size() != 1) {
							for(String s : user.getPage(Integer.parseInt(args[2]))) {
								sender.sendMessage(s);
							}
						}
						else {
							sender.sendMessage(prefix + "That user does not have that many pages!");
						}
					}
					else {
						sender.sendMessage(prefix +"You must enter a valid page!");
					}
				}
				else {
					sender.sendMessage(prefix + "You do not have permission to do this!");
				}
			}
			else {
				sender.sendMessage(prefix + "Invalid number of arguments!");
			}
		}
		else {
			sender.sendMessage(prefix + "You must be a player to do this!");
		}
	}
	return true;
}

public boolean isValidInt(String number) {
	try {
		Integer.parseInt(number);
		return true;
	}
	catch(NumberFormatException e) {
		return false;
	}
}

public boolean isValidPackage(int id) {
	if(config.getName(id) == null) {
		return false;
	}
	return true;
}

public String fixPlayerName(String s) {
	if(getServer().getPlayer(s) != null) return getServer().getPlayer(s).getName();
	return getServer().getOfflinePlayer(s).getName();
}
}