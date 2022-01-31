package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;

public class BoomArrows implements Listener {
    Boolean boom_arrows = false;

    public BoomArrows(Plugin p) {
        Bukkit.getServer().getPluginManager().registerEvents(this, p);
    }

    public boolean set(String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            this.boom_arrows = true;
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            this.boom_arrows = false;
            return true;
        }
        return false;
    }


    @EventHandler
	public void OnProjectileHitEvent(ProjectileHitEvent event) {
		if (this.boom_arrows) {
            if(event.getEntity() instanceof Arrow)  {
                Block block = event.getHitBlock();
                if (block != null) {
                    block.getWorld().createExplosion(block.getLocation(), (float) 1.0);
                } else {
                    Entity entity = event.getHitEntity();
                    entity.getWorld().createExplosion(entity.getLocation(), (float) 1.0);
                }
                event.getEntity().remove();
            }
		}
	}
}
