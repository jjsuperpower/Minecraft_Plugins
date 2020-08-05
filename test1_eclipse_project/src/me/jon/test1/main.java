package me.jon.test1;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin	{
	
	public void OnEnable()	{
		System.out.println("test1 enabled");
//		this.getCommand("test1").setExecutor((CommandExecutor)new commands());
	}
	
	public void OnDisable()	{
		System.out.println("test1 disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test1")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
			// doSomething
			Player player = (Player)sender;
			player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
			return true;
		} //If this has happened the function will return true. 
	        // If this hasn't happened the value of false will be returned.
		return false; 
	}

}
