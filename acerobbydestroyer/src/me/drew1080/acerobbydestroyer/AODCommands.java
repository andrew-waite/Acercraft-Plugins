package me.drew1080.acerobbydestroyer;

import java.util.HashMap;
import java.util.Set;
import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class AODCommands implements CommandExecutor {

	private AcerObbyDestroyer plugin;
	
	public AODCommands(AcerObbyDestroyer plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args != null) {
			if (sender instanceof Player) 
			{
					usePermissionsStructure((Player) sender, cmd, commandLabel, args);
			}
			 else {
				sender.sendMessage(ChatColor.RED + "Sorry, you are not a player!");
			}
		}

		return true;
	}

	private void usePermissionsStructure(Player sender, Command cmd, String commandLabel, String[] args) {

		if (args.length == 0) {
			// show help
			if (sender.hasPermission("AcerObbyDestroyer.help")) {
				showHelp(sender);
			} else {
				sender.sendMessage(ChatColor.RED + "You are not authorized to use this command.");
			}
		} else if (args.length == 1) {
			// commands with 0 arguments
			String command = args[0];

			if (command.equalsIgnoreCase("reload")) {
				// reload
				if (sender.hasPermission("AcerObbyDestroyer.config.reload")) {
					reloadPlugin(sender);
				} else {
					sender.sendMessage(ChatColor.RED + "You are not authorized to use this command.");
				}
			} else if (command.equalsIgnoreCase("info")) {
				// info
				if (sender.hasPermission("AcerObbyDestroyer.config.info")) {
					getConfigInfo(sender);
				} else {
					sender.sendMessage(ChatColor.RED + "You are not authorized to use this command.");
				}
			} else if (command.equalsIgnoreCase("reset")) {
				// reset durabilities
				if (sender.hasPermission("AcerObbyDestroyer.durability.reset")) {
					resetDurability(sender);
				} else {
					sender.sendMessage(ChatColor.RED + "You are not authorized to use this command.");
				}
			}
		}
	}

	private void showHelp(Player sender) {
		sender.sendMessage(ChatColor.YELLOW + "Available commands:");
		sender.sendMessage(ChatColor.GOLD + "/aod reload - reloads the plugin's config file");
		sender.sendMessage(ChatColor.GOLD + "/aod info - shows the currently loaded config");
	}

	private void reloadPlugin(Player sender) {
		Log.info("'" + sender.getName() + "' requested reload of AcerObbyDestroyer");
		sender.sendMessage(ChatColor.GREEN + "Reloading AcerObbyDestroyer!");

		if (plugin.reload()) {
			sender.sendMessage(ChatColor.GREEN + "Success!");
		}
	}

	private void getConfigInfo(Player sender) {
		AODConfig config = plugin.getAODConfig();
		sender.sendMessage(ChatColor.YELLOW + "Currently loaded config of AcerObbyDestroyer:");
		sender.sendMessage(ChatColor.YELLOW + "---------------------------------------------");

		if (config.getConfigFile().exists()) {
			for (String s : config.printLoadedConfig()) {
				sender.sendMessage(ChatColor.YELLOW + s);
			}
		} else {
			sender.sendMessage(ChatColor.RED + "None - Config file deleted - please reload");
		}
	}

	private void resetDurability(Player sender) {
		AODEntityListener listener = plugin.getListener();

		listener.setObsidianDurability(new HashMap<Integer, Integer>());

		Set<Integer> set = listener.getObsidianTimer().keySet();

		for (Integer i : set) {
			Timer t = listener.getObsidianTimer().get(i);

			if (t != null) {
				t.cancel();
			}
		}

		listener.setObsidianTimer(new HashMap<Integer, Timer>());

		Log.info("'" + sender.getName() + "' requested reset of Obsidian durabilities");
		sender.sendMessage(ChatColor.GREEN + "Reset all Obsidian durabilities!");
	}
}
