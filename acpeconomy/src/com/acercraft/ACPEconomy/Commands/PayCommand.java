package com.acercraft.ACPEconomy.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.acercraft.ACPEconomy.API.Economy;
import com.acercraft.ACPEconomy.Util.User;
import com.acercraft.ACPEconomy.Util.Util;

public class PayCommand implements CommandExecutor{

    public boolean onCommand(CommandSender s, Command c, String l, String[] args)
    {
	if(c.getName().equalsIgnoreCase("pay"))
	{
	    if(args.length < 2)
	    {
		s.sendMessage("/pay player ammount");
		return true;
	    }

	    if(args.length == 2)
	    {
		if(s instanceof Player)
		{

		    if(Util.isValidDouble(args[2]))
		    {
			Economy.payPlayer(new User(args[0]), Double.parseDouble(args[2]), s.getName());
		    }
		    else
		    {
			s.sendMessage("Please enter a valid integer");
		    }
		}
		else
		{
		    s.sendMessage("You have to be a player to run this command");
		}
	    }
	}
	return false;
    }
}
