package me.drew1080.acerobbydestroyer;

import java.util.HashMap;
import java.util.TimerTask;

public final class AODTimerTask extends TimerTask {
	
	private AcerObbyDestroyer plugin;
	private final Integer duraID;
	
	public AODTimerTask(AcerObbyDestroyer plugin, Integer duraID) {
		this.plugin = plugin;
		this.duraID = duraID;
	}
	
	@Override
	public void run() {
		resetDurability(duraID);
		resetEnchantDurability(duraID);
	}

	private void resetDurability(Integer id) {
		if (id == null) {
			return;
		}
		
		HashMap<Integer, Integer> map = plugin.getListener().getObsidianDurability();
		
		if (map == null) {
			return;
		}
		
		
		map.remove(id);
	}
	
	private void resetEnchantDurability(Integer id) {
		if (id == null) {
			return;
		}
		
		HashMap<Integer, Integer> map = plugin.getListener().getEnchantmentDurability();

		if (map == null) {
			return;
		}
		
		map.remove(id);
	}
}