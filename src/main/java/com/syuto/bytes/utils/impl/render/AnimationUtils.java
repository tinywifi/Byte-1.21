package com.syuto.bytes.utils.impl.render;

import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationCalculator;
import net.minecraft.util.math.RotationPropertyHelper;
import org.lwjgl.opengl.GL11;

public class AnimationUtils {

    public static float height = 0.05f;

    public static void animate(MatrixStack matrices, float swingProgress) {
        float sine = (float) Math.sin(MathHelper.sqrt(swingProgress) * Math.PI);
        float f = (float) Math.sin(swingProgress * swingProgress * Math.PI);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(1 * (45.0f + f * 10.0f)));

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-sine * 30.0f));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-sine * 50.0f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-45.0f));
    }
}
