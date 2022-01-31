package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SkyItems implements Listener {
    Boolean name_on = false;
    BukkitRunnable sky_items_id;
    Plugin p;

    public SkyItems(Plugin p) {
        this.p = p;
    }

    public boolean set(String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            if (this.sky_items_id != null) {
                this.sky_items_id.cancel();
            }
            this.sky_items_id = new BukkitRunnable() {
                public void run() {
                    for (Player a_player : Bukkit.getOnlinePlayers()) {
                        ItemStack rand_item = new ItemStack(Material.values()[new Random().nextInt(Material.values().length)]);
                        int rand_int = ThreadLocalRandom.current().nextInt(32, 128);
                        for(int i = rand_int; i < 128; i++)	{
                            a_player.getWorld().dropItem(a_player.getLocation().add(0, 30, 0), rand_item);
                        }
                    }
                    Bukkit.getServer().broadcastMessage("Dropping items!");
                }
            }.runTaskTimer(p, 200, 1200);
        }
        if (args[0].equalsIgnoreCase("off")) {
            if (this.sky_items_id != null) {
                this.sky_items_id.cancel();
            }
        }
        return false;
    }


    @EventHandler

}

