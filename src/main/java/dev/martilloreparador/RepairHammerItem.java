package dev.martilloreparador;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class RepairHammerItem extends Item {
    private static final EquipmentSlot[] ARMOR = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public RepairHammerItem(Properties properties) { super(properties); }

    // Mantener clic derecho comienza una sesión de reparación.
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BRUSH;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack hammer, int remaining) {
        if (level.isClientSide() || !(livingEntity instanceof Player player)) return;
        HammerConfig config = HammerConfig.INSTANCE;
        int ticksUsed = getUseDuration(hammer, player) - remaining;
        if (ticksUsed <= 0 || ticksUsed % config.ticksPerHit != 0) return;

        ItemStack target = player.isShiftKeyDown() && config.repairArmor
                ? findDamagedArmor(player)
                : findOtherHandTarget(player, hammer);

        if (target == null) {
            player.sendOverlayMessage(Component.translatable("message.martilloreparador26.no_target"));
            return;
        }
        repairOnce(level, player, hammer, target);
    }

    // Clic derecho sobre otra persona: una reparación por clic a su armadura.
    @Override
    public InteractionResult interactLivingEntity(ItemStack hammer, Player player,
                                                   LivingEntity target, InteractionHand hand) {
        if (!(target instanceof Player other) || !HammerConfig.INSTANCE.repairOtherPlayers)
            return InteractionResult.PASS;
        if (player.level().isClientSide()) return InteractionResult.SUCCESS;
        ItemStack piece = findDamagedArmor(other);
        if (piece == null) {
            player.sendOverlayMessage(Component.translatable("message.martilloreparador26.no_target"));
            return InteractionResult.SUCCESS;
        }
        repairOnce(player.level(), player, hammer, piece);
        return InteractionResult.SUCCESS;
    }

    private static ItemStack findOtherHandTarget(Player player, ItemStack hammer) {
        ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack off = player.getItemInHand(InteractionHand.OFF_HAND);
        if (main == hammer) return off.isDamaged() ? off : null;
        if (off == hammer) return main.isDamaged() ? main : null;
        return null;
    }

    private static ItemStack findDamagedArmor(Player player) {
        for (EquipmentSlot slot : ARMOR) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isDamageableItem() && stack.isDamaged()) return stack;
        }
        return null;
    }

    private static void repairOnce(Level level, Player owner, ItemStack hammer, ItemStack target) {
        HammerConfig cfg = HammerConfig.INSTANCE;
        if (!target.isDamageableItem() || !target.isDamaged() || target == hammer) return;
        if (hammer.getDamageValue() >= hammer.getMaxDamage() - 1) {
            owner.sendOverlayMessage(Component.translatable("message.martilloreparador26.hammer_worn"));
            return;
        }
        if (!owner.hasInfiniteMaterials() && owner.totalExperience < cfg.xpPointsPerHit) {
            owner.sendOverlayMessage(Component.translatable("message.martilloreparador26.no_xp"));
            return;
        }

        target.setDamageValue(Math.max(0, target.getDamageValue() - cfg.durabilityRestored));
        if (!owner.hasInfiniteMaterials()) owner.giveExperiencePoints(-cfg.xpPointsPerHit);
        hammer.setDamageValue(hammer.getDamageValue() + 1);

        if (cfg.anvilSound) {
            level.playSound(null, owner.blockPosition(), SoundEvents.ANVIL_USE,
                    SoundSource.PLAYERS, 0.3F, 1.0F + level.getRandom().nextFloat() * 0.2F);
        }
        int percent = Math.round(100.0F * (target.getMaxDamage() - target.getDamageValue()) /
                Math.max(1, target.getMaxDamage()));
        owner.sendOverlayMessage(Component.translatable("message.martilloreparador26.progress", percent));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                TooltipDisplay display, Consumer<Component> text, TooltipFlag flag) {
        text.accept(Component.translatable("tooltip.martilloreparador26.hold").withStyle(ChatFormatting.GRAY));
        text.accept(Component.translatable("tooltip.martilloreparador26.sneak").withStyle(ChatFormatting.GRAY));
        text.accept(Component.translatable("tooltip.martilloreparador26.xp").withStyle(ChatFormatting.GRAY));
    }
}
