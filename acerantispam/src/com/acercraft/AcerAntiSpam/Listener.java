package com.acercraft.AcerAntiSpam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Listener implements org.bukkit.event.Listener {
	AcerAntiSpam plugin;
	public Listener(AcerAntiSpam plugin) {
		this.plugin = plugin;
	}

	HashMap<String, Long> sentRecent = new HashMap<String, Long>();
	HashMap<String, Integer> timesSpammed = new HashMap<String, Integer>();
	HashMap<String, Long> lastSpam = new HashMap<String, Long>();
	HashMap<String, Long> muted = new HashMap<String, Long>();
	HashMap<String, List<String>> lastMessage = new HashMap<String, List<String>>();

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if(!p.hasPermission("aspam.bypass")) {
			String name = p.getName();
			if(muted.containsKey(name)) {
				event.setCancelled(true);
				p.sendMessage(plugin.prefix + plugin.muted);
				return;
			}
			if(plugin.messageCooldownEnabled) {
				if(sentRecent.containsKey(name)) {
					long lastSent = sentRecent.get(name);
					if(System.currentTimeMillis() - lastSent < plugin.messageCooldownTime) {
						event.setCancelled(true);
						p.sendMessage(plugin.prefix + plugin.messageSpam);
						if(!timesSpammed.containsKey(name)) timesSpammed.put(name, 0);
						if(plugin.messageCooldownMute) {
							timesSpammed.put(name, timesSpammed.get(name) + 1);
							int spammed = timesSpammed.get(name);
							if(spammed >= plugin.messageCooldownChances) {
								muted.put(name, System.currentTimeMillis());
								p.sendMessage(plugin.prefix + plugin.mutedForChat);
							}
						}
						lastSpam.put(name, System.currentTimeMillis());
					}
				}
				sentRecent.put(name, System.currentTimeMillis());
			}

			if(plugin.isCapsTooHigh(event.getMessage())) {
				event.setCancelled(true);
				p.sendMessage(plugin.prefix + plugin.tooManyCaps);
			}
			if(lastMessage.containsKey(name) && lastMessage.get(name).size() >= plugin.repBlockAfter + 1) {
				List<String> lastMessages = lastMessage.get(name);
				List<String> mostRecentMessages = new ArrayList<String>();

				for(int i = 0; i < plugin.repBlockAfter; i++) {
					int get = lastMessages.size() - 1 - i;
					mostRecentMessages.add(lastMessages.get(get));
				}
				List<Integer> same = new ArrayList<Integer>();
				for(int i = 0; i < mostRecentMessages.size(); i++) {
					if(i < mostRecentMessages.size() - 1) {
						String next = mostRecentMessages.get(i + 1);
						int difference = plugin.getLevenshteinDistance(mostRecentMessages.get(i), next);
						if(difference < plugin.repPercent) {
							same.add(i);
						}
					}
					else {
						int difference = plugin.getLevenshteinDistance(mostRecentMessages.get(i), event.getMessage());
						if(difference < plugin.repPercent) {
							same.add(i);
						}
					}
				}
				boolean allSame = true;
				for(int i = 0; i < plugin.repBlockAfter; i++) {
					if(!same.contains(i)) {
						allSame = false;
					}
				}
				if(allSame) {
					event.setCancelled(true);
					p.sendMessage(plugin.prefix + plugin.tooManyReps);
				}
			}
			if(!event.isCancelled()) {
				if(lastMessage.get(name) == null) {
					lastMessage.put(name, new ArrayList<String>());
				}
				lastMessage.get(name).add(event.getMessage());
			}
		}
	}

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if(!event.getPlayer().hasPermission("aspam.bypass")) {
			String name = event.getPlayer().getName();
			if(muted.containsKey(name)) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(plugin.prefix + plugin.muted);
				return;
			}
			if(plugin.commandCooldownEnabled){
				if(sentRecent.containsKey(name)) {
					long lastSent = sentRecent.get(name);
					if(System.currentTimeMillis() - lastSent < plugin.messageCooldownTime) {
						if(!timesSpammed.containsKey(name)) timesSpammed.put(name, 0);
						event.setCancelled(true);
						event.getPlayer().sendMessage(plugin.prefix + plugin.cmdSpam);
						if(plugin.messageCooldownMute) {
							timesSpammed.put(name, timesSpammed.get(name) + 1);
							int spammed = timesSpammed.get(name);
							if(spammed >= plugin.messageCooldownChances) {
								muted.put(name, System.currentTimeMillis());
								event.getPlayer().sendMessage(plugin.prefix + plugin.mutedForCmds);
							}
						}
						lastSpam.put(name, System.currentTimeMillis());
					}
				}
				sentRecent.put(name, System.currentTimeMillis());
			}
		}
	}
}
