package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class FartTnt implements Listener {
    Boolean fartTnt_on = false;

    public FartTnt(Plugin p) {
        Bukkit.getServer().getPluginManager().registerEvents(this, p);
    }

    public boolean cmd(String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            this.fartTnt_on = true;
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            this.fartTnt_on = false;
            return true;
        }
        return false;
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (this.fartTnt_on) {
            if (event.getPlayer().isSneaking()) {
                Entity tnt = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), TNTPrimed.class);
                ((TNTPrimed) tnt).setFuseTicks(100);

            }
        }
    }

}

