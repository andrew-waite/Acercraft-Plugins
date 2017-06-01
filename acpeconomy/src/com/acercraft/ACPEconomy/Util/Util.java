package com.acercraft.ACPEconomy.Util;

public class Util {
    
    public static boolean isValidDouble(String number) 
    {
	try 
	{
		Double.parseDouble(number);
		return true;
	}
	catch(NumberFormatException e) 
	{
		return false;
	}
    }
    
    public static boolean isValidInt(String number)
    {
	try
	{
	    Integer.parseInt(number);
	    return true;
	}
	catch(NumberFormatException e)
	{
	    return false;
	}
    }

}
