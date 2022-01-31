package me.myself.i.commands;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Strike {
    public static void looking_at(Player player)    {
        player.getWorld().strikeLightning(player.getTargetBlock((Set<Material>) null, 200).getLocation());
    }

    public static void loc(Location loc, World world)    {
        world.strikeLightning(loc);
    }

    public static void player(Player player)    {
        player.getWorld().strikeLightning(player.getLocation());
    }
}
