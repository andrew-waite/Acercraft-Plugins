package com.acercraft.AcerPerks.utils;

import java.util.HashMap;

public class Cooldowns {
	
	public static HashMap<String, Long> healCommandCooldown = new HashMap<String, Long>();
	public static HashMap<String, Long> feedCommandCooldown = new HashMap<String, Long>();
	public static HashMap<String, Long> speedCommandCooldown = new HashMap<String, Long>();
	public static HashMap<String, Long> strengthCommandCooldown = new HashMap<String, Long>();
	
	public static boolean hasHealCommandCooldown(String playerName, int time)
	{
		if(healCommandCooldown.get(playerName) < (System.currentTimeMillis() - time * 1000))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean hasFeedCommandCooldown(String playerName, int time)
	{
		if(feedCommandCooldown.get(playerName) < (System.currentTimeMillis() - time * 1000))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean hasSpeedCommandCooldown(String playerName, int time)
	{
		if(speedCommandCooldown.get(playerName) < (System.currentTimeMillis() - time * 1000))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean hasStrengthCommandCooldown(String playerName, int time)
	{
		if(strengthCommandCooldown.get(playerName) < (System.currentTimeMillis() - time * 1000))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static void startHealCommandCoolDown(String playerName)
	{
		healCommandCooldown.put(playerName, System.currentTimeMillis());
	}
	
	public static void startFeedCommandCoolDown(String playerName)
	{
		feedCommandCooldown.put(playerName, System.currentTimeMillis());
	}
	
	public static void startSpeedCommandCoolDown(String playerName)
	{
		speedCommandCooldown.put(playerName, System.currentTimeMillis());
	}
	
	public static void startStrengthCommandCoolDown(String playerName)
	{
		strengthCommandCooldown.put(playerName, System.currentTimeMillis());
	}
}
