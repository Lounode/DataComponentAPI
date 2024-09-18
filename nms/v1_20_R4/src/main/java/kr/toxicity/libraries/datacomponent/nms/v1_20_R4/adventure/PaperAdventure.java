package kr.toxicity.libraries.datacomponent.nms.v1_20_R4.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PaperAdventure {
    public static final ComponentSerializer<Component, Component, net.minecraft.network.chat.Component> WRAPPER_AWARE_SERIALIZER = new WrapperAwareSerializer();

    private PaperAdventure() {
    }

    // Component

    public static @NotNull Component asAdventure(@Nullable final net.minecraft.network.chat.Component component) {
        return component == null ? Component.empty() : WRAPPER_AWARE_SERIALIZER.deserialize(component);
    }
    @Contract("null -> null; !null -> !null")
    public static net.minecraft.network.chat.Component asVanilla(final @Nullable Component component) {
        if (component == null) return null;
        if (true) return new AdventureComponent(component);
        return WRAPPER_AWARE_SERIALIZER.serialize(component);
    }


}
