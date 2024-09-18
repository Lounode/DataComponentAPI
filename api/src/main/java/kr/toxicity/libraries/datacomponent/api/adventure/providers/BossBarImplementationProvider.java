package kr.toxicity.libraries.datacomponent.api.adventure.providers;

import kr.toxicity.libraries.datacomponent.api.adventure.BossBarImplementationImpl;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBarImplementation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage") // permitted provider
public class BossBarImplementationProvider implements BossBarImplementation.Provider {
    @Override
    public @NotNull BossBarImplementation create(final @NotNull BossBar bar) {
        return new BossBarImplementationImpl(bar);
    }
}
