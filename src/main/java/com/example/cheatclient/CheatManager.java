package com.example.cheatclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.Hand;
import java.util.Random;

public class CheatManager {
    public static boolean killAura = false, silentAim = false, reach = false;
    public static boolean spinbot = false, jitter = false, bunnyhop = false, runboost = false;
    private static int attackCooldown = 0;
    private static final Random random = new Random();
    private static float spinYaw = 0f;

    public static void tick(MinecraftClient mc) {
        if (mc.player == null || mc.world == null) return;
        ClientPlayerEntity player = mc.player;

        if (bunnyhop && mc.options.jumpKey.isPressed() && player.isOnGround()) player.jump();
        if (runboost && !player.isOnGround()) {
            Vec3d v = player.getVelocity();
            player.setVelocity(v.x * 1.3, v.y, v.z * 1.3);
        }
        if (spinbot) {
            spinYaw = (spinYaw + 45f) % 360f;
            player.setYaw(spinYaw);
            player.prevYaw = spinYaw;
        }
        if (killAura || silentAim) {
            PlayerEntity target = findTarget(mc, reach ? 5.0 : 3.0);
            if (target != null) {
                if (silentAim) sendSilentLook(mc, player, target);
                if (killAura && attackCooldown <= 0) {
                    mc.interactionManager.attackEntity(player, target);
                    player.swingHand(Hand.MAIN_HAND);
                    attackCooldown = 10;
                }
            }
        }
        if (attackCooldown > 0) attackCooldown--;
    }

    private static PlayerEntity findTarget(MinecraftClient mc, double range) {
        PlayerEntity best = null; double bd = range * range;
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player || !p.isAlive()) continue;
            double d = mc.player.squaredDistanceTo(p);
            if (d < bd) { bd = d; best = p; }
        }
        return best;
    }

    private static void sendSilentLook(MinecraftClient mc, ClientPlayerEntity player, PlayerEntity target) {
        Vec3d eye = player.getEyePos();
        Vec3d t = target.getPos().add(0, target.getHeight() * 0.5, 0).subtract(eye).normalize();
        float y = (float) Math.toDegrees(Math.atan2(-t.x, t.z));
        float p = (float) Math.toDegrees(Math.asin(-t.y));
        if (jitter) { y += (random.nextFloat() - 0.5f) * 50f; p += (random.nextFloat() - 0.5f) * 50f; }
        if (mc.getNetworkHandler() != null) mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(y, p, player.isOnGround()));
    }
}
