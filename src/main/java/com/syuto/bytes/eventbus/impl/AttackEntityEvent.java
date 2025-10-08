package com.syuto.bytes.eventbus.impl;

import com.syuto.bytes.eventbus.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class AttackEntityEvent implements Event {
    @Getter
    private Entity target;

    @Getter
    @Setter
    private boolean cancelled;

    public AttackEntityEvent(Entity target) {
        this.target = target;
    }
}
