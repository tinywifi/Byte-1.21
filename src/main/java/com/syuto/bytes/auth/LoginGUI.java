package com.syuto.bytes.auth;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;

import static com.syuto.bytes.Byte.mc;

public class LoginGUI extends Screen {
    public static String rsp;

    private Text loginStatus = Text.literal("");
    private TextFieldWidget usernameField;


    public LoginGUI() {
        super(Text.of("Login"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        usernameField = new TextFieldWidget(
                textRenderer,
                centerX - 100,
                centerY - 30,
                200,
                20,
                Text.literal("Username")
        );
        usernameField.setFocused(true);
        this.addSelectableChild(usernameField);


        loginStatus = Text.literal(Formatting.GRAY + "N/A");

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Login"), button -> {
            loginStatus = Text.literal(Formatting.YELLOW + "Logging in....");

            try {
                validateLogin(usernameField.getText().toLowerCase());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }).dimensions(centerX - 100, centerY + 40, 200, 20).build());
    }


    private void validateLogin(String username) throws Exception {
        rsp = Authentication.authenticate(username);
        switch(rsp) {
            case "INVALID_REQUEST" -> loginStatus = Text.literal(Formatting.RED + "Error!");
            case "INVALID_HWID", "NO_ACTIVE_CHALLENGE" -> loginStatus = Text.literal(Formatting.RED + "Invalid HWID!");
            case "USER_NOT_FOUND" -> loginStatus = Text.literal(Formatting.RED + "Invalid User!");
            case "AUTHENTICATION_FAILED" -> loginStatus = Text.literal(Formatting.RED + "Authentication Failed!");
            case "AUTHENTICATION_SUCCESS" -> {
                loginStatus = Text.literal(Formatting.GREEN + "Successful login!");
                mc.setScreen(null);
                System.out.println("No");
            }
        }
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        int boxWidth = 250;
        int boxHeight = 140;
        int boxX = this.width / 2 - boxWidth / 2;
        int boxY = this.height / 2 - 70;

        context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0x88000000);

        int lineY = boxY - 1;
        float animationOffset = (System.currentTimeMillis() % 2000) / 2000.0f;
        for (int x = 0; x < boxWidth; x++) {
            float position = ((float) x / boxWidth + animationOffset) % 1.0f;
            float hue = position;
            int color = Color.HSBtoRGB(hue, 1.0f, 1.0f);
            context.fill(boxX + x, lineY, boxX + x + 1, lineY + 1, color);
        }

        String boxTitle = "Welcome to Byte!";
        context.drawTextWithShadow(
                this.textRenderer,
                boxTitle,
                this.width / 2 - this.textRenderer.getWidth(boxTitle) / 2,
                boxY + 10,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                loginStatus,
                this.width / 2 - this.textRenderer.getWidth(loginStatus) / 2,
                boxY + 80,
                0xFFFFFF
        );

        usernameField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (usernameField.isFocused() && usernameField.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (usernameField.charTyped(chr, modifiers)) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (usernameField.mouseClicked(mouseX, mouseY, button)) {
            usernameField.setFocused(true);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
