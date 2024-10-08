package me.nurio.imperial.core.protection;

import me.nurio.imperial.core.Imperial;
import me.nurio.imperial.core.organizations.OrganizationFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class ProtectionPlayerListener implements Listener {

    private final OrganizationFactory organizationFactory = Imperial.getOrganizationFactory();
    private final PermissionManager permissionManager = new PermissionManager(organizationFactory);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreakEvent(BlockBreakEvent eve) {
        boolean hasPermissionsAt = permissionManager.hasPermissionsAt(eve.getPlayer(), eve.getBlock().getLocation());

        // Outsiders can't
        if (!hasPermissionsAt) {
            eve.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockPlaceEvent(BlockPlaceEvent eve) {
        boolean hasPermissionsAt = permissionManager.hasPermissionsAt(eve.getPlayer(), eve.getBlock().getLocation());

        // Outsiders can't
        if (!hasPermissionsAt) {
            eve.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockCanEvent(BlockCanBuildEvent eve) {
        boolean hasPermissionsAt = permissionManager.hasPermissionsAt(eve.getPlayer(), eve.getBlock().getLocation());

        // Outsiders can't
        if (!hasPermissionsAt) {
            eve.setBuildable(false);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent eve) {
        Location interactionPoint = eve.getInteractionPoint();
        if (interactionPoint == null) {
            return;
        }

        boolean hasPermissionsAt = permissionManager.hasPermissionsAt(eve.getPlayer(), interactionPoint);
        if (!hasPermissionsAt) {
            eve.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void farmJump(PlayerInteractEvent eve) {
        // Skip other cases
        if (eve.getAction() != Action.PHYSICAL) return;
        Block block = eve.getClickedBlock();
        if (block == null) return;
        if (block.getType() != Material.FARMLAND) return;

        // Check permissions
        boolean hasPermissionsAt = permissionManager.hasPermissionsAt(eve.getPlayer(), block.getLocation());
        if (!hasPermissionsAt) {
            eve.setCancelled(true);
        }
    }

    @EventHandler
    public void entityFarmJump(EntityInteractEvent eve) {
        Block block = eve.getBlock();
        if (block.getType() != Material.FARMLAND) return;
        eve.setCancelled(true);
    }

}
