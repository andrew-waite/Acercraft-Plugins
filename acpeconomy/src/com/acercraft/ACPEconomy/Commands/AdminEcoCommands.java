package com.acercraft.ACPEconomy.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.acercraft.ACPEconomy.API.Economy;
import com.acercraft.ACPEconomy.Util.User;
import com.acercraft.ACPEconomy.Util.Util;

public class AdminEcoCommands implements CommandExecutor {

    public boolean onCommand(CommandSender s, Command c, String l, String[] args)
    {
	if(c.getName().equalsIgnoreCase("economy"))
	{
	    if(args.length < 3)
	    {
		s.sendMessage("/eco take|give|set <Player> <Ammount>");
		return true;
	    }

	    if(args.length == 3)
	    {
		if(args[0].equalsIgnoreCase("give"))
		{
		    if(Util.isValidDouble(args[2]))
		    {
			Economy.addMoney(new User(args[1]), Double.parseDouble(args[2]));
			return true;
		    }
		    else
		    {
			s.sendMessage("Please enter a valid number");
			return true;
		    }
		}

		if(args[0].equalsIgnoreCase("take"))
		{
		    if(Util.isValidDouble(args[2]))
		    {
			Economy.takeMoney(new User(args[1]), Double.parseDouble(args[2]));
			return true;
		    }
		    else
		    {
			s.sendMessage("Please enter a valid number");
			return true;
		    }
		}

		if(args[0].equalsIgnoreCase("set"))
		{
		    if(Util.isValidDouble(args[2]))
		    {
			Economy.setMoney(new User(args[1]), Double.parseDouble(args[2]));
			return true;
		    }
		    else
		    {
			s.sendMessage("Please enter a valid number");
		    }
		}
	    }
	}
	return false;
    }

}
