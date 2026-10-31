package com.example.cheatclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import java.util.Random;

public class CheatManager {
    // Toggles
    public static boolean aimbot = false, silentAim = false, killAura = false;
    public static boolean spinbot = false, jitter = false, bunnyhop = false, runboost = false;
    public static boolean autoTotem = false, noClip = false, teleport = false;

    // Settings
    public static float spinSpeed = 45f;
    public static float silentAimFov = 90f;
    public static float runboostMultiplier = 1.3f;
    public static float killAuraRange = 5.0f;

    private static int attackCooldown = 0;
    private static final Random random = new Random();
    private static float spinYaw = 0f;

    public static void tick(MinecraftClient mc) {
        if (mc.player == null || mc.world == null) return;
        ClientPlayerEntity player = mc.player;

        // --- AUTO TOTEM ---
        if (autoTotem && player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            for (int i = 0; i < 9; i++) {
                if (player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(i));
                    mc.interactionManager.clickSlot(player.playerScreenHandler.syncId, 45, i, net.minecraft.screen.slot.SlotActionType.SWAP, player);
                    break;
                }
            }
        }

        // --- NO CLIP ---
        player.noClip = noClip;

        // --- BUNNYHOP ---
        if (bunnyhop && mc.options.jumpKey.isPressed() && player.isOnGround()) {
            player.jump();
        }

        // --- RUNBOOST ---
        if (runboost && !player.isOnGround()) {
            Vec3d v = player.getVelocity();
            player.setVelocity(v.x * runboostMultiplier, v.y, v.z * runboostMultiplier);
        }

        // --- SPINBOT (Sadece sunucuya döner, ekran dönmez) ---
        if (spinbot && mc.getNetworkHandler() != null) {
            spinYaw = (spinYaw + spinSpeed) % 360f;
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(spinYaw, player.getPitch(), player.isOnGround()));
        }

        // --- COMBAT (KILLAURA / SILENT AIM) ---
        if (killAura || silentAim || aimbot) {
            Entity target = findTarget(mc, killAuraRange);
            if (target != null) {
                if (silentAim || aimbot) {
                    sendSilentLook(mc, player, target);
                }
                if ((killAura || aimbot) && attackCooldown <= 0) {
                    // Vanilla bypass: Doğrudan paket gönder
                    mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.attack(target, player.isSneaking()));
                    player.swingHand(Hand.MAIN_HAND);
                    attackCooldown = 10;
                }
            }
        }

        // --- TELEPORT ---
        if (teleport && mc.options.forwardKey.isPressed() && mc.options.sprintKey.isPressed()) {
            player.setPosition(player.getX() + player.getRotationVec(1.0f).x * 5, player.getY(), player.getZ() + player.getRotationVec(1.0f).z * 5);
            teleport = false; // Tek seferlik
        }

        if (attackCooldown > 0) attackCooldown--;
    }

    private static Entity findTarget(MinecraftClient mc, double range) {
        Entity best = null; double bd = range * range;
        for (Entity p : mc.world.getEntities()) {
            if (p == mc.player || !(p instanceof LivingEntity) || !p.isAlive()) continue;
            double d = mc.player.squaredDistanceTo(p);
            if (d < bd) { bd = d; best = p; }
        }
        return best;
    }

    private static void sendSilentLook(MinecraftClient mc, ClientPlayerEntity player, Entity target) {
        Vec3d eye = player.getEyePos();
        Vec3d t = target.getPos().add(0, target.getHeight() * 0.5, 0).subtract(eye).normalize();
        float y = (float) Math.toDegrees(Math.atan2(-t.x, t.z));
        float p = (float) Math.toDegrees(Math.asin(-t.y));
        
        // FOV Kontrolü
        float yawDiff = Math.abs(player.getYaw() - y) % 360;
        if (yawDiff > 180) yawDiff = 360 - yawDiff;
        if (yawDiff > silentAimFov / 2f) return; // FOV dışındaysa dönme

        if (jitter) { 
            y += (random.nextFloat() - 0.5f) * 50f; 
            p += (random.nextFloat() - 0.5f) * 50f; 
        }
        if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(y, p, player.isOnGround()));
        }
    }
}
