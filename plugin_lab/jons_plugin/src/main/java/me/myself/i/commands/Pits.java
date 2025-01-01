package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Pits implements Listener {
    Plugin p;
    Boolean pits_on = false;

    public Pits(Plugin p) {
        Bukkit.getServer().getPluginManager().registerEvents(this, p);
        this.p = p;
    }

    public boolean cmd(String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            this.pits_on = true;
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            this.pits_on = false;
            return true;
        }
        return false;
    }

    @EventHandler
	public void onMove(PlayerMoveEvent event) {
        if (this.pits_on) {
			Location loc = event.getPlayer().getLocation();
			int player_loc_y = (int) loc.getY();
			loc.setY((double) -59);

			//this took a while to get right
			new BukkitRunnable() {
				public void run() {
					loc.add(0, 1, 0);
					if (loc.getBlock().getType() != Material.OBSIDIAN) {
						loc.getBlock().setType(Material.AIR);
					}
					if ((int) loc.getY() >= player_loc_y) {
						cancel();
					}
				}
			}.runTaskTimer(p, 15, 5);
		}
    }

}

