package dev.martilloreparador.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.martilloreparador.HammerConfig;
import dev.martilloreparador.RepairHammerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Reemplaza el barrido horizontal BRUSH por golpes verticales de reparación.
 * Afecta solamente al Martillo Reparador en primera persona; los pinceles
 * de arqueología conservan su animación vanilla.
 * Compatible con el renderizador FirstPersonHandsAndItemsRenderer de MC 26.3.
 */
@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class VerticalRepairAnimationMixin {
    @Inject(method = "applyBrushTransform", at = @At("HEAD"), cancellable = true)
    private void martilloreparador26$verticalStrike(PoseStack poseStack,
            float partialTicks, HumanoidArm arm, float useItemRemainingTicks, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || !player.isUsingItem() ||
                !player.getUseItem().is(RepairHammerMod.HAMMER)) {
            return;
        }

        // Misma cadencia que la reparación real: el golpe toca fondo
        // al terminar cada intervalo de ticksPerHit.
        int period = Math.max(4, HammerConfig.INSTANCE.ticksPerHit);
        float used = 72000.0F - useItemRemainingTicks;
        float phase = ((used % period) + period) % period / period;

        // Elevación suave (55 % del ciclo) y golpe descendente (45 %).
        float rise = phase < 0.55F
                ? phase / 0.55F
                : 1.0F - (phase - 0.55F) / 0.45F;
        rise = Math.max(0.0F, Math.min(1.0F, rise));
        float easedRise = rise * rise * (3.0F - 2.0F * rise);

        // Solo ejes vertical (Y) e inclinación frontal (X); sin barrido lateral.
        poseStack.translate(0.0F, -0.04F + easedRise * 0.34F, 0.0F);
        poseStack.rotateDegrees(Axis.XP, -35.0F + easedRise * 65.0F);

        ci.cancel(); // No aplicar la transformación horizontal BRUSH de Minecraft.
    }
}
