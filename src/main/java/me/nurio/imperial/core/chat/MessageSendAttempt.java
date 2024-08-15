package me.nurio.imperial.core.chat;

import lombok.Getter;
import me.nurio.imperial.core.Imperial;
import me.nurio.imperial.core.organizations.Organization;
import me.nurio.imperial.core.organizations.OrganizationFactory;
import me.nurio.minecraft.worldareas.GrechAreas;
import me.nurio.minecraft.worldareas.areas.WorldAreaFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageSendAttempt {

    private static OrganizationFactory organizationFactory = Imperial.getOrganizationFactory();

    @Getter
    private Player sender;

    private Component message;

    @Getter
    private Organization senderOrganization;

    @Getter
    private Organization senderLocationOrganization;

    public MessageSendAttempt(Player sender, Component message) {
        this.sender = sender;
        this.message = message;

        List<Organization> playerOrganizations = organizationFactory.fromPlayer(sender.getPlayer());
        if (!playerOrganizations.isEmpty()) {
            senderOrganization = playerOrganizations.getFirst();
        }

        List<Organization> locationOrganization = organizationFactory.fromLocation(sender.getLocation());
        if (!locationOrganization.isEmpty()) {
            senderLocationOrganization = locationOrganization.getFirst();
        }
    }

    public void send(Player receiver) {
        Organization receiverOrganization = null;
        List<Organization> playerOrganizations = organizationFactory.fromPlayer(receiver);
        if (!playerOrganizations.isEmpty()) {
            receiverOrganization = playerOrganizations.getFirst();
        }

        Organization receiverLocationOrganization = null;
        List<Organization> locationOrganization = organizationFactory.fromLocation(receiver.getLocation());
        if (!locationOrganization.isEmpty()) {
            receiverLocationOrganization = locationOrganization.getFirst();
        }

        receiver.sendMessage(
            MessagePrefix.messagePrefix(sender)
                .appendSpace()
                .append(message.color(MessageColor.getMessageColor(
                    sender, receiver,
                    senderOrganization, senderLocationOrganization,
                    receiverOrganization, receiverLocationOrganization
                )))
        );

    }

}
