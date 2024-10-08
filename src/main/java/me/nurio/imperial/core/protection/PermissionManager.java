package me.nurio.imperial.core.protection;

import lombok.RequiredArgsConstructor;
import me.nurio.imperial.core.organizations.Organization;
import me.nurio.imperial.core.organizations.OrganizationFactory;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class PermissionManager {

    @NotNull private OrganizationFactory organizationFactory;

    public boolean hasPermissionsAt(Player player, Location location) {
        // Below height 20 everything is allowed.
        if (location.getBlockY() <= 20) {
            return true;
        }

        // If world is not overworld, then allow it.
        if (location.getWorld().getEnvironment() != World.Environment.NORMAL) {
            return true;
        }

        // Check organization memberships
        Organization organization = organizationFactory.fromPlayer(player);

        // Outsiders doesn't have permissions
        if (organization == null) {
            OutsiderMessager.sendOutsiderMessage(player);
            return false;
        }

        // If there is no organization or for some reason, more than one.
        List<Organization> organizationsAtLoc = organizationFactory.fromLocation(location);
        if (organizationsAtLoc.size() != 1) {
            WildernessMessager.sendMessage(player);
            return false;
        }

        // If player belongs to that organization
        Organization locationOrganization = organizationsAtLoc.getFirst();
        return organization == locationOrganization;
    }

}
