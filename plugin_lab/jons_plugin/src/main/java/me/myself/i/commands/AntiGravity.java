package me.myself.i.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AntiGravity implements CommandExecutor {
    private BukkitTask gravity_id;
    private Plugin plugin;

    public AntiGravity(Plugin plugin) {
        this.plugin = plugin;
        this.gravity_id = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("antigravity")) {
            if (args.length == 0) {
                return false;
            }
            if (args[0].equalsIgnoreCase("on")) {
                if (this.gravity_id != null) {
                    this.gravity_id.cancel();
                }
                this.gravity_id = new BukkitRunnable() {
                    World w = player.getWorld();
                    int arg = Integer.parseInt(args[1]);

                    public void run() {
                        for (Entity e : w.getEntities()) {
                            e.setGravity(false);
                            if (arg != 0)
                                e.setVelocity(new Vector(0.0, arg * 0.01, 0.0));
                        }
                    }
                }.runTaskTimer(this.plugin, 2, 10);
                return true;
            }
            if (args[0].equalsIgnoreCase("off")) {
                if (this.gravity_id != null) {
                    this.gravity_id.cancel();
                }
                for (Player p : Bukkit.getWorlds().get(0).getPlayers()) {
                    p.setGravity(true);
                }
                for (Player p : Bukkit.getWorlds().get(1).getPlayers()) {
                    p.setGravity(true);
                }
                for (Player p : Bukkit.getWorlds().get(2).getPlayers()) {
                    p.setGravity(true);
                }
                return true;
            }
        }
        return false;
    }
}