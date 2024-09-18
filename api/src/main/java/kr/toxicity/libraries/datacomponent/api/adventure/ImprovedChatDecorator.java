package kr.toxicity.libraries.datacomponent.api.adventure;

import io.papermc.paper.event.player.AsyncChatCommandDecorateEvent;
import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.concurrent.CompletableFuture;

@DefaultQualifier(NonNull.class)
public final class ImprovedChatDecorator implements ChatDecorator {
    private final MinecraftServer server;

    public ImprovedChatDecorator(final MinecraftServer server) {
        this.server = server;
    }

    @Override
    public CompletableFuture<Component> decorate(final @Nullable ServerPlayer sender, final Component message) {
        return decorate(this.server, sender, null, message);
    }

    @Override
    public CompletableFuture<Component> decorate(final @Nullable ServerPlayer sender, final @Nullable CommandSourceStack commandSourceStack, final Component message) {
        return decorate(this.server, sender, commandSourceStack, message);
    }

    private static CompletableFuture<Component> decorate(final MinecraftServer server, final @Nullable ServerPlayer player, final @Nullable CommandSourceStack commandSourceStack, final Component originalMessage) {
        return CompletableFuture.supplyAsync(() -> {
            final net.kyori.adventure.text.Component initialResult = PaperAdventure.asAdventure(originalMessage);

            final @Nullable CraftPlayer craftPlayer = player == null ? null : player.getBukkitEntity();

            final AsyncChatDecorateEvent event;
            if (commandSourceStack != null) {
                // TODO more command decorate context
                event = new AsyncChatCommandDecorateEvent(craftPlayer, initialResult);
            } else {
                event = new AsyncChatDecorateEvent(craftPlayer, initialResult);
            }

            if (event.callEvent()) {
                return PaperAdventure.asVanilla(event.result());
            }

            return originalMessage;
        }, server.chatExecutor);
    }
}
