package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.event.Listener;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SkyItems implements Listener {
    Boolean name_on = false;
    BukkitTask sky_items_id;
    Plugin p;

    public SkyItems(Plugin p) {
        this.p = p;
    }

    public boolean cmd(String[] args) {
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
            }.runTaskTimer(this.p,  200,  1200);

            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            if (this.sky_items_id != null) {
                this.sky_items_id.cancel();
            }
            return true;
        }

        return false;
    }
}

