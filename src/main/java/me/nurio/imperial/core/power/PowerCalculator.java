package me.nurio.imperial.core.power;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class PowerCalculator {

    private static final float UNDERGROUND_MULTIPLIER = 0.5F;
    private static final float SKY_MULTIPLIER = 1.5F;

    public static Map<Material, Float> dynamicPowerBlocksMap = new HashMap<>() {{
        put(Material.COBBLESTONE, 1F);
        put(Material.TORCH, 1F);

        // TODO add more wood types
        put(Material.OAK_PLANKS, 3F);
        put(Material.DARK_OAK_PLANKS, 3F);
        put(Material.ACACIA_PLANKS, 3F);
    }};

    public static Map<Material, Float> staticPowerBlocksMap = new HashMap<>() {{
        // Building blocks
        put(Material.STONE_BRICKS, 10F);
        put(Material.BRICKS, 20F);
        put(Material.DEEPSLATE_BRICKS, 5F);
        put(Material.TUFF_BRICKS, 5F);

        // Decoration
        put(Material.DARK_OAK_FENCE, 10F);
        put(Material.OAK_FENCE, 10F);
        put(Material.ACACIA_FENCE, 10F);

        // Working blocks
        put(Material.LOOM, 20F);
        put(Material.JUKEBOX, 100F);
        put(Material.CHEST, 100F);

        // Infrastructure
        put(Material.RAIL, 10F);

        // Farms
        put(Material.WHEAT, 10F);
        put(Material.CARROTS, 20F);
        put(Material.POTATOES, 20F);

        // TODO Add more wood types
        put(Material.OAK_SIGN, 10F);
        put(Material.DARK_OAK_SIGN, 10F);
        put(Material.ACACIA_SIGN, 10F);
    }};

    public static boolean isPowerBlock(Location location) {
        return powerFromLocation(location) > 0;
    }

    public static int powerFromLocation(Location location) {
        Material material = location.getBlock().getType();

        float dynamicPower = dynamicPowerBlocksMap.getOrDefault(material, 0F);
        float staticPower = staticPowerBlocksMap.getOrDefault(material, 0F);

        int y = location.getBlockY();

        // Underground
        if (y <= 60) {
            return Math.round(
                (dynamicPower * UNDERGROUND_MULTIPLIER) + staticPower
            );
        }

        // Overworld
        if (y <= 100) {
            return Math.round(
                dynamicPower + staticPower
            );
        }

        // Sky
        return Math.round(
            (dynamicPower * SKY_MULTIPLIER) + staticPower
        );

    }

}
