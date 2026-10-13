package dev.martilloreparador;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Configuración del servidor (en el directorio config/ de Minecraft). */
public final class HammerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir()
            .resolve("martilloreparador26.json");

    public int ticksPerHit = 8;
    public int durabilityRestored = 12;
    public int xpPointsPerHit = 1;
    public boolean repairArmor = true;
    public boolean repairOtherPlayers = true;
    public boolean anvilSound = true;

    public static HammerConfig INSTANCE = new HammerConfig();
    private HammerConfig() { }

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                try (Reader reader = Files.newBufferedReader(PATH, StandardCharsets.UTF_8)) {
                    HammerConfig incoming = GSON.fromJson(reader, HammerConfig.class);
                    if (incoming != null) INSTANCE = incoming;
                }
            }
            INSTANCE.ticksPerHit = Math.max(1, Math.min(100, INSTANCE.ticksPerHit));
            INSTANCE.durabilityRestored = Math.max(1, Math.min(1000, INSTANCE.durabilityRestored));
            INSTANCE.xpPointsPerHit = Math.max(0, Math.min(100, INSTANCE.xpPointsPerHit));
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (Exception ex) {
            System.err.println("[Martillo Reparador] No se pudo leer la configuración: " + ex);
        }
    }
}
