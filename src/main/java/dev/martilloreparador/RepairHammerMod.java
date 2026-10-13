package dev.martilloreparador;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

/**
 * Mod independiente, sin reutilizar clases ni texturas del Repair Hammer original.
 */
public class RepairHammerMod implements ModInitializer {
    public static final String MOD_ID = "martilloreparador26";

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static final ResourceKey<Item> HAMMER_ID =
            ResourceKey.create(Registries.ITEM, id("repair_hammer"));

    public static final TagKey<Item> HAMMER_REPAIR_MATERIALS =
            TagKey.create(Registries.ITEM, id("hammer_repair_materials"));

    public static final Item HAMMER = Registry.register(
            BuiltInRegistries.ITEM,
            HAMMER_ID,
            new RepairHammerItem(new Item.Properties()
                    .setId(HAMMER_ID)
                    .stacksTo(1)
                    .durability(5000)
                    .repairable(HAMMER_REPAIR_MATERIALS))
    );

    @Override
    public void onInitialize() {
        HammerConfig.load();
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(HAMMER));
    }
}
