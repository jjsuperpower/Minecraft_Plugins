package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class Pvp implements Listener {
    Boolean pvp_on = true;

    public Pvp(Plugin p) {
        Bukkit.getServer().getPluginManager().registerEvents(this, p);
    }

    public boolean cmd(String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            this.pvp_on = true;
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            this.pvp_on = false;
            return true;
        }
        return false;
    }


    public void onDamage(EntityDamageByEntityEvent event) {
		if (!this.pvp_on) {
			Entity entity = event.getEntity();
			if (entity instanceof Player) {
				event.setCancelled(true);
			}
		}
    }

}

