package kr.toxicity.libraries.datacomponent.nms.v1_21_R1.adventure;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.JavaOps;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.Codec;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.craftbukkit.CraftRegistry;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class PaperAdventure {
    public static final Codec<Tag, String, CommandSyntaxException, RuntimeException> NBT_CODEC = new Codec<>() {
        @Override
        public @NotNull Tag decode(final @NotNull String encoded) throws CommandSyntaxException {
            return new TagParser(new StringReader(encoded)).readValue();
        }

        @Override
        public @NotNull String encode(final @NotNull Tag decoded) {
            return decoded.toString();
        }
    };
    public static final ComponentSerializer<Component, Component, net.minecraft.network.chat.Component> WRAPPER_AWARE_SERIALIZER = new WrapperAwareSerializer(() -> CraftRegistry.getMinecraftRegistry().createSerializationContext(JavaOps.INSTANCE));

    private PaperAdventure() {
    }

    // Key

    public static ResourceLocation asVanilla(final Key key) {
        return ResourceLocation.fromNamespaceAndPath(key.namespace(), key.value());
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

    // NBT

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<Key, ? extends DataComponentValue> asAdventure(
        final DataComponentPatch patch
    ) {
        if (patch.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<Key, DataComponentValue> map = new HashMap<>();
        for (final Map.Entry<DataComponentType<?>, Optional<?>> entry : patch.entrySet()) {
            if (entry.getKey().isTransient()) continue;
            @Subst("key:value") final String typeKey = requireNonNull(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(entry.getKey())).toString();
            if (entry.getValue().isEmpty()) {
                   map.put(Key.key(typeKey), DataComponentValue.removed());
            } else {
                map.put(Key.key(typeKey), new DataComponentValueImpl(entry.getKey().codec(), entry.getValue().get()));
            }
        }
        return map;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DataComponentPatch asVanilla(final Map<? extends Key, ? extends DataComponentValue> map) {
        if (map.isEmpty()) {
            return DataComponentPatch.EMPTY;
        }
        final DataComponentPatch.Builder builder = DataComponentPatch.builder();
        map.forEach((key, dataComponentValue) -> {
            final DataComponentType<?> type = requireNonNull(BuiltInRegistries.DATA_COMPONENT_TYPE.get(asVanilla(key)));
            if (dataComponentValue instanceof DataComponentValue.Removed) {
                builder.remove(type);
                return;
            }
            final DataComponentValueImpl<?> converted = DataComponentValueConverterRegistry.convert(DataComponentValueImpl.class, key, dataComponentValue);
            builder.set((DataComponentType) type, (Object) converted.value());
        });
        return builder.build();
    }

    public record DataComponentValueImpl<T>(com.mojang.serialization.Codec<T> codec, T value) implements DataComponentValue.TagSerializable {

        @Override
        public @NotNull BinaryTagHolder asBinaryTag() {
            return BinaryTagHolder.encode(this.codec.encodeStart(CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), this.value).getOrThrow(IllegalArgumentException::new), NBT_CODEC);
        }
    }


}
