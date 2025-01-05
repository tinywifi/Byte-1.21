package com.syuto.bytes.mixin;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.syuto.bytes.Byte.mc;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerMixin extends PlayerEntity {
    public AbstractClientPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void init(CallbackInfoReturnable<SkinTextures> cir) {
        Identifier cape = Identifier.of("byte", "capes/cape.png");
        if (this.getUuid().equals(mc.player.getUuid())) {
            SkinTextures skinTextures = cir.getReturnValue();
            cir.setReturnValue(new SkinTextures(
                    skinTextures.texture(),
                    skinTextures.textureUrl(),
                    cape,
                    cape,
                    skinTextures.model(),
                    skinTextures.secure()
            ));
        }
    }
}
