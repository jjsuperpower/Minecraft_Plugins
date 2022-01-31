package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class SharedDamage implements Listener {
    Boolean shared_damage_on = false;
    Boolean damage_self_triggered = false; // don't create a recursive loop of pain

    public SharedDamage(Plugin p) {
        Bukkit.getServer().getPluginManager().registerEvents(this, p);
    }

    public Boolean set(String[] args)   {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            this.shared_damage_on = true;
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            this.shared_damage_on = false;
            return true;
        }
        return false;
    }

    @EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (this.shared_damage_on) {
			Entity entity = event.getEntity();
			if (!this.damage_self_triggered && entity instanceof Player) {
				this.damage_self_triggered = true;
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.damage(event.getDamage());
				}
				this.damage_self_triggered = false;
				event.setCancelled(true);
			}
		}

		//not usefull but good to know
		//Bukkit.getPluginManager().callEvent(new EntityDamageEvent((Entity) player, null, event.getDamage()));
		//Bukkit.getServer().broadcastMessage("Damage event called " + String.valueOf(this.damage_self_triggered));
		//Bukkit.getServer().broadcastMessage(event.getEntity().getName());
		//player.sendMessage(String.valueOf(event.getDamage()));

	}
}
