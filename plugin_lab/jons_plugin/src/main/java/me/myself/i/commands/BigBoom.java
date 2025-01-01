package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.Plugin;

public class BigBoom implements Listener {
    Boolean bigBoom_on = false;

    public BigBoom(Plugin p) {
        Bukkit.getServer().getPluginManager().registerEvents(this, p);
    }

    public boolean cmd(String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            this.bigBoom_on = true;
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            this.bigBoom_on = false;
            return true;
        }
        return false;
    }


    @EventHandler
	public void onPrime(ExplosionPrimeEvent event) {
		if(this.bigBoom_on)	{
			event.setRadius(128);
			event.setFire(true);
		}
	}

}

