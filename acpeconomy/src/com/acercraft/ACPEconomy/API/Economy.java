package com.acercraft.ACPEconomy.API;

import com.acercraft.ACPEconomy.Util.User;

public class Economy {
    
    public static double getBalance(User user)
    {
	return user.getBalance();
    }
    
    public static void addMoney(User user, double ammount)
    {
	user.addMoney(user, ammount);
    }
    
    public static void takeMoney(User user, double ammount)
    {
	user.withDrawMoney(user, ammount);
    }
    
    public static void setMoney(User user, double ammount)
    {
	user.setMoney(ammount);
    }
    
    public static void payPlayer(User user, double ammount, String originalPlayer)
    {
	user.payPlayer(user, ammount, originalPlayer);
    }
}
