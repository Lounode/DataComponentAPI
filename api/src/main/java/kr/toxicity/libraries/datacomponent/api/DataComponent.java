package kr.toxicity.libraries.datacomponent.api;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface DataComponent {
    void set(@NotNull ItemAdapter adapter);
    @NotNull
    JsonObject get();
}
