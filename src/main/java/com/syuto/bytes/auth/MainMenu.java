package com.syuto.bytes.auth;

import com.mojang.blaze3d.systems.RenderSystem;
import com.syuto.bytes.Byte;
import com.syuto.bytes.utils.impl.render.Snow;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ServerLinksScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.PositionedSoundInstance;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.syuto.bytes.Byte.mc;

public class MainMenu extends Screen {

    public final PositionedSoundInstance loopingSound = PositionedSoundInstance.master(
            Byte.FEM_SOUND_EVENT,
            1.0F,
            3.0f
    );



    Identifier imageIdentifier = Identifier.of("byte", "background/byte_4.png");
    Identifier imageIdentifier2 = Identifier.of("byte", "background/byte_3.png");
    Identifier bk = Identifier.of("byte", "background/back.png");

    public MainMenu() {
        super(Text.of("Custom Main Menu"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = (this.height / 2);

        this.addDrawableChild(new TransparentButton(
                centerX - 100, centerY - 25, 200, 20,
                Text.literal("Singleplayer"),
                button -> onSingleplayer()));

        this.addDrawableChild(new TransparentButton(
                centerX - 100, centerY, 200, 20,
                Text.literal("Multiplayer"),
                button -> onMultiplayer()));

        this.addDrawableChild(new TransparentButton(
                centerX - 100, centerY + 25, 98, 20,
                Text.literal("Options"),
                button -> onOpenSettings()));

        this.addDrawableChild(new TransparentButton(
                centerX + 2, centerY + 25, 98, 20,
                Text.literal("Quit Game"),
                button -> onExit()));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int centerX = this.width / 2;
        int centerY = (this.height / 2);

        super.render(context, mouseX, mouseY, delta);

        int logoWidth = 125;
        int logoHeight = 125;

        int logoX = centerX - (logoWidth / 2);
        int logoY = centerY - logoHeight;

        context.drawTexture(
                RenderLayer::getGuiTextured,
                bk,
                0, 0,
                0, 0,
                width, height,
                width, height
        );

        //Snow.renderSnowflakes(context);

        this.children().forEach(child -> {
            if (child instanceof ButtonWidget) {
                ((ButtonWidget) child).render(context, mouseX, mouseY, delta);
            }
        });

        //playCustomSound();
    }

    private void playCustomSound() {
        if (loopingSound.canPlay() && !mc.getSoundManager().isPlaying(loopingSound)) {
            mc.getSoundManager().play(loopingSound);
        }
    }

    private void onSingleplayer() {
        this.client.setScreen(new SelectWorldScreen(this));
    }

    private void onMultiplayer() {
        this.client.setScreen(new MultiplayerScreen(this));
    }

    private void onOpenSettings() {
        this.client.setScreen(new OptionsScreen(this, this.client.options));
    }

    private void onExit() {
        this.client.scheduleStop();
    }

    @Override
    public void close() {
        super.close();
        mc.getSoundManager().stop(loopingSound);
    }
}
