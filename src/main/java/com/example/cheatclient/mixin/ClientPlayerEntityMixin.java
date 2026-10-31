package com.example.cheatclient.mixin;

import com.example.cheatclient.CheatManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "getEntityInteractionRange", at = @At("RETURN"), cancellable = true)
    private void onEntityReach(CallbackInfoReturnable<Float> cir) {
        if (CheatManager.aimbot || CheatManager.killAura) cir.setReturnValue(CheatManager.killAuraRange);
    }

    @Inject(method = "getBlockInteractionRange", at = @At("RETURN"), cancellable = true)
    private void onBlockReach(CallbackInfoReturnable<Float> cir) {
        if (CheatManager.aimbot || CheatManager.killAura) cir.setReturnValue(CheatManager.killAuraRange);
    }
}
