package com.acercraft.AcerPerks.utils;

import com.acercraft.AcerPerks.AcerPerks;

public class Messages {
	
	public static String getPrefix()
	{
		return AcerPerks.getInstance().configHandler().getPrefix();
	}

}
