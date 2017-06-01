package com.acercraft.AcerAntiSpam;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class AcerAntiSpam extends JavaPlugin {
	boolean messageCooldownEnabled;
	boolean messageCooldownMute;
	int messageCooldownChances;
	int messageCooldownMuteTime;
	int messageCooldownTime;
	boolean commandCooldownEnabled;
	int commandCooldownTime;
	boolean commandCooldownMute;
	int commandCooldownMuteTime;
	int commandCooldownChances;
	boolean capsEnabled;
	int capsPercent;
	boolean capsSpecialChars;
	int capsSkipAmnt;
	boolean repEnabled;
	int repBlockAfter;
	int repSkipAmnt;
	int repPercent;
	String prefix;
	String noPerms;
	String tooManyCaps;
	String tooManyReps;
	String mutedForCmds;
	String mutedForChat;
	String muted;
	String noLongerMuted;
	String notMuted;
	String reloaded;
	String unknown;
	String messageSpam;
	String cmdSpam;
	String playerOff;
	List<String> blacklist;
	Listener listener;
	String blacklistedMessage;
	public void onEnable() {
		saveDefaultConfig();
		messageCooldownEnabled = getConfig().getBoolean("Message_Cooldown.Enabled");
		messageCooldownMute = getConfig().getBoolean("Message_Cooldown.Mute");
		messageCooldownChances = getConfig().getInt("Message_Cooldown.Chances");
		messageCooldownMuteTime = getConfig().getInt("Message_Cooldown.Mute_time");
		messageCooldownTime = getConfig().getInt("Message_Cooldown.Message_cooldown_time");
		commandCooldownEnabled = getConfig().getBoolean("Command_Cooldown.Enabled");
		commandCooldownTime = getConfig().getInt("Command_Cooldown.Command_cooldown_time");
		commandCooldownMute = getConfig().getBoolean("Command_Cooldown.Mute");
		commandCooldownMuteTime = getConfig().getInt("Command_Cooldown.Mute_time");
		commandCooldownChances = getConfig().getInt("Command_Cooldown.Chances");
		capsEnabled = getConfig().getBoolean("Caps_Lock.Enabled");
		capsPercent = getConfig().getInt("Caps_Lock.Allowed_caps_percent");
		capsSpecialChars = getConfig().getBoolean("Caps_Lock.Special_characters_as_uppercase");
		capsSkipAmnt = getConfig().getInt("Caps_Lock.Skip_cap_check");
		repEnabled = getConfig().getBoolean("Message_repitition.Enabled");
		repBlockAfter = getConfig().getInt("Message_repetition.Block_after");
		repSkipAmnt = getConfig().getInt("Message_repetition.Skip_check");
		repPercent = getConfig().getInt("Message_repetition.Percentage_difference");
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix"));
		noPerms = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPerms"));
		tooManyCaps = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.TooManyCaps"));
		tooManyReps = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.TooManyReps"));
		mutedForCmds = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.MutedForCmds"));
		mutedForChat = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.MutedForChat"));
		muted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Muted"));
		noLongerMuted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoLongerMuted"));
		notMuted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NotMuted"));
		reloaded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Reloaded"));
		unknown = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Unknown"));
		playerOff = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.PlayerOff"));
		blacklist = getConfig().getStringList("Blacklisted_Words");
		blacklistedMessage = getConfig().getString("Messages.Blacklisted");
		messageSpam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.MessageSpam", "Do not spam!"));
		cmdSpam = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.CmdSpam", "Do not spam!"));
		listener = new Listener(this);
		Bukkit.getPluginManager().registerEvents(listener, this);
		getServer().getScheduler().runTaskTimer(this, new BukkitRunnable() {
			public void run() {
				List<String> toBeRemoved = new ArrayList<String>();
				for(String s : listener.muted.keySet()) {
					long time = System.currentTimeMillis();
					if(time - listener.muted.get(s) >= messageCooldownMuteTime*1000) {
						toBeRemoved.add(s);
						if(getServer().getPlayer(s) != null) {
							getServer().getPlayer(s).sendMessage(prefix + noLongerMuted);
						}
					}
				}
				for(String s : toBeRemoved) {
					listener.muted.remove(s);
				}
				toBeRemoved.clear();
				for(String s : listener.timesSpammed.keySet()) {
					if(listener.lastSpam.containsKey(s)) {
						long sent = listener.lastSpam.get(s);
						long time = System.currentTimeMillis();
						if(time - sent >= 15*60*1000) { // 15 min chances reset
							toBeRemoved.add(s);
						}
					}
				}
				for(String s : toBeRemoved) {
					listener.timesSpammed.remove(s);
				}
				toBeRemoved.clear();
			}
		}, 20, 20);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("aspam")) {
			if(sender instanceof Player) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("reload")) {
						if(sender.hasPermission("aspam.reload")) {
							reloadPlugin();
							sender.sendMessage(prefix + reloaded);
						}
						else {
							sender.sendMessage(prefix + noPerms);
						}
					}
					else {
						sender.sendMessage(prefix + unknown);
					}
				}
				else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("unmute")) {
						if(sender.hasPermission("aspam.unmute")) {
							if(getServer().getPlayer(args[1]) != null) {
								if(listener.muted.containsKey(args[1])) {
									listener.muted.remove(args[1]);
									getServer().getPlayer(args[1]).sendMessage(prefix + noLongerMuted);
								}
								else {
									sender.sendMessage(prefix + notMuted);
								}
							}
							else {
								sender.sendMessage(prefix + playerOff);
							}
						}
						else {
							sender.sendMessage(prefix + noPerms);
						}
					}
					else {
						sender.sendMessage(prefix + unknown);
					}
				}
				else {
					sender.sendMessage(prefix + unknown);
				}
			}
			else {
				sender.sendMessage("You must be a player to use this command!");
			}
		}
		return true;
	}

	public boolean isCapsTooHigh(String s) {
		if(capsEnabled) {
			int chars = 0;
			for(int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if(!Character.isSpaceChar(c)) {
					chars++;
				}
			}
			if(!(s.length() < capsSkipAmnt)) {
				int caps = 0;
				for(int i = 0; i < s.length(); i++) {
					char c = s.charAt(i);
					if(Character.isUpperCase(c)) {
						caps++;
						continue;
					}
					if(capsSpecialChars) {
						if(!Character.isDigit(c) && !Character.isLetter(c) && !Character.isSpaceChar(c)) {
							caps++;
						}
					}
				}
				double percent = (caps / chars) * 100;
				if(percent >= capsPercent) {
					return true;
				}
			}
		}
		return false;
	}

	public int getLevenshteinDistance (String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		int n = s.length();
		int m = t.length();

		if (n == 0) {
			return m;
		}

		else if (m == 0) {
			return n;
		}

		int p[] = new int[n+1];
		int d[] = new int[n+1];
		int _d[];
		int i;
		int j;

		char t_j;

		int cost;

		for (i = 0; i<=n; i++) {
			p[i] = i;
		}

		for (j = 1; j<=m; j++) {
			t_j = t.charAt(j-1);
			d[0] = j;

			for (i=1; i<=n; i++) {
				cost = s.charAt(i-1)==t_j ? 0 : 1;

				d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
			}
			_d = p;
			p = d;
			d = _d;
		}

		//Determine percentage difference
		double levNum = (double)p[n];
		double percent = (levNum/Math.max(s.length(),t.length()))*100;
		int percentDiff = (int)percent;

		return percentDiff;
	}
	
	public void reloadPlugin() {
		reloadConfig();
		messageCooldownEnabled = getConfig().getBoolean("Message_Cooldown.Enabled");
		messageCooldownMute = getConfig().getBoolean("Message_Cooldown.Mute");
		messageCooldownChances = getConfig().getInt("Message_Cooldown.Chances");
		messageCooldownMuteTime = getConfig().getInt("Message_Cooldown.Mute_time");
		messageCooldownTime = getConfig().getInt("Message_Cooldown.Message_cooldown_time");
		commandCooldownEnabled = getConfig().getBoolean("Command_Cooldown.Enabled");
		commandCooldownTime = getConfig().getInt("Command_Cooldown.Command_cooldown_time");
		commandCooldownMute = getConfig().getBoolean("Command_Cooldown.Mute");
		commandCooldownMuteTime = getConfig().getInt("Command_Cooldown.Mute_time");
		commandCooldownChances = getConfig().getInt("Command_Cooldown.Chances");
		capsEnabled = getConfig().getBoolean("Caps_Lock.Enabled");
		capsPercent = getConfig().getInt("Caps_Lock.Allowed_caps_percent");
		capsSpecialChars = getConfig().getBoolean("Caps_Lock.Special_characters_as_uppercase");
		capsSkipAmnt = getConfig().getInt("Caps_Lock.Skip_cap_check");
		repEnabled = getConfig().getBoolean("Message_repitition.Enabled");
		repBlockAfter = getConfig().getInt("Message_repetition.Block_after");
		repSkipAmnt = getConfig().getInt("Message_repetition.Skip_check");
		repPercent = getConfig().getInt("Message_repetition.Percentage_difference");
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix"));
		noPerms = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPerms"));
		tooManyCaps = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.TooManyCaps"));
		tooManyReps = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.TooManyReps"));
		mutedForCmds = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.MutedForCmds"));
		mutedForChat = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.MutedForChat"));
		muted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Muted"));
		noLongerMuted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoLongerMuted"));
		notMuted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NotMuted"));
		reloaded = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Reloaded"));
		unknown = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Unknown"));
		playerOff = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.PlayerOff"));
		blacklist = getConfig().getStringList("Blacklisted_Words");
	}
}
