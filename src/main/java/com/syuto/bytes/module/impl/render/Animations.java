package com.syuto.bytes.module.impl.render;

import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.setting.impl.ModeSetting;

import java.util.Arrays;
import java.util.List;

public class Animations extends Module {

    public ModeSetting mode = new ModeSetting("Animation", this, "Exhibition", "Vanilla", "Spin");


    public Animations() {
        super("Animations", "animations", Category.RENDER);
    }



}
