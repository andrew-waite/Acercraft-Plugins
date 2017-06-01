package com.acercraft.acertokens;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MobsHandler {
	AcerTokens plugin;
	List<String> mobsList = new ArrayList<String>();
	
	public void addUUID(UUID id) {
		String uuid = id.toString();
		mobsList.add(uuid);
	}
	
	public boolean isSpawnedMob(UUID id) {
		String uuid = id.toString();
		if(mobsList.contains(uuid)) {
			return true;
		}
		return false;
	}
	
	public void removeMob(UUID id) {
		String uuid = id.toString();
		if(mobsList.contains(uuid)) {
			mobsList.remove(uuid);
		}
	}
}
