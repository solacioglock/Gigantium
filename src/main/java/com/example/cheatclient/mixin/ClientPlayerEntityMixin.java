package com.example.cheatclient.mixin;

import com.example.cheatclient.CheatManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "getBlockInteractionRange", at = @At("RETURN"), cancellable = true)
    private void onReach(CallbackInfoReturnable<Float> cir) {
        if (CheatManager.reach) cir.setReturnValue(5.0f);
    }
}
