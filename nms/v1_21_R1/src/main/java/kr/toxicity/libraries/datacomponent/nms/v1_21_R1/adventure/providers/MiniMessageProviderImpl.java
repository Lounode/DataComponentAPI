package kr.toxicity.libraries.datacomponent.nms.v1_21_R1.adventure.providers;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage") // permitted provider
public class MiniMessageProviderImpl implements MiniMessage.Provider {

    @Override
    public @NotNull MiniMessage miniMessage() {
        return MiniMessage.builder().build();
    }

    @Override
    public @NotNull Consumer<MiniMessage.Builder> builder() {
        return builder -> {};
    }
}
