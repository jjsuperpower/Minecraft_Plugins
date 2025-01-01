
package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class AntiGravityGame {
    private BukkitTask gravity_game_id;
    private Plugin plugin;
    private boolean gravity = true;
    private int counter = 0;
    private int rand_int = ThreadLocalRandom.current().nextInt(0, 60);

    public AntiGravityGame(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean cmd(String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            start();
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            stop();
            return true;
        }
        return false;
    }

    public void start() {
        if (this.gravity_game_id != null) {
            this.gravity_game_id.cancel();
        }

        this.gravity_game_id = new BukkitRunnable() {
            World over_world = Bukkit.getWorlds().get(0);
            World nether_world = Bukkit.getWorlds().get(1);
            World end_world = Bukkit.getWorlds().get(2);

            public void run() {
                if (gravity && (counter < rand_int)) {
                    counter += 1;
                } else if (gravity && (counter >= rand_int)) {
                    counter = 0;
                    gravity = false;
                    rand_int = ThreadLocalRandom.current().nextInt(0, 60);
                    Bukkit.getServer().broadcastMessage("Gravity Flipped");
                }

                if (!gravity && (counter < rand_int)) {
                    counter += 1;

                    for (Entity e : over_world.getEntities()) {
                        e.setGravity(false);
                        e.setVelocity(e.getVelocity().add(new Vector(0.0, 0.2, 0.0)));
                    }

                    for (Entity e : nether_world.getEntities()) {
                        e.setGravity(false);
                        e.setVelocity(e.getVelocity().add(new Vector(0.0, 0.2, 0.0)));
                    }

                    for (Entity e : end_world.getEntities()) {
                        e.setGravity(false);
                        e.setVelocity(e.getVelocity().add(new Vector(0.0, 0.2, 0.0)));
                    }

                } else if (!gravity && (counter >= rand_int)) {
                    counter = 0;
                    gravity = true;

                    for (Entity e : over_world.getEntities()) {
                        e.setGravity(true);
                    }

                    for (Entity e : nether_world.getEntities()) {
                        e.setGravity(true);
                    }

                    for (Entity e : end_world.getEntities()) {
                        e.setGravity(true);
                    }
                }
            }
        }.runTaskTimer(this.plugin, 0, 20);
    }

    public void stop() {
        if (this.gravity_game_id != null) {
            this.gravity_game_id.cancel();
        }

        World over_world = Bukkit.getWorlds().get(0);
        World nether_world = Bukkit.getWorlds().get(1);
        World end_world = Bukkit.getWorlds().get(2);

        for (Player p : over_world.getPlayers()) {
            p.setGravity(true);
        }

        for (Player p : nether_world.getPlayers()) {
            p.setGravity(true);
        }

        for (Player p : end_world.getPlayers()) {
            p.setGravity(true);
        }
    }
}
