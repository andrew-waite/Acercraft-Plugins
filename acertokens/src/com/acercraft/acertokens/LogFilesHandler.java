package com.acercraft.acertokens;

import java.io.File;
import java.util.HashMap;

public class LogFilesHandler {
	HashMap<String, TokensUser> users = new HashMap<String, TokensUser>();
	
	public TokensUser getTokenUser(String name) {
		if(users.containsKey(name)) return users.get(name);
		TokensUser user = new TokensUser(name);
		user.loadFromFile();
		users.put(name, user);
		return user;
	}
	
	public void loadAllUsers() {
		File file = new File("plugins/AcerTokens/users");
		for(File f : file.listFiles()) {
			String name = f.getName();
			name = name.substring(0, f.getName().length() - 4); // Remove .yml from name
			TokensUser user = new TokensUser(name);
			user.loadFromFile();
			users.put(name, user);
		}
	}
}
