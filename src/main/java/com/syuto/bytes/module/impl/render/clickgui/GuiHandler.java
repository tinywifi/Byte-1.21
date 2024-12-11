package com.syuto.bytes.module.impl.render.clickgui;

import com.syuto.bytes.module.Module;
import com.syuto.bytes.module.ModuleManager;
import com.syuto.bytes.module.api.Category;
import com.syuto.bytes.module.impl.render.ClickGui;
import com.syuto.bytes.setting.Setting;
import com.syuto.bytes.setting.impl.BooleanSetting;
import com.syuto.bytes.setting.impl.ColorSetting;
import com.syuto.bytes.setting.impl.ModeSetting;
import com.syuto.bytes.setting.impl.NumberSetting;
import imgui.ImGui;
import imgui.ImVec4;
import imgui.flag.ImGuiDataType;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImDouble;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.syuto.bytes.Byte.mc;

public class GuiHandler extends Screen {

    private Category selectedCategory = Category.COMBAT;

    public GuiHandler() {
        super(Text.of("Byte"));
    }

    @Override
    public void close() {
        super.close();
        ModuleManager.getModule(ClickGui.class).toggle();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //super.render(context, mouseX, mouseY, delta);

        ImGuiImpl.draw(io -> {
            if (mc.isWindowFocused()) {
                renderMain();
            }
        });
    }

    public void renderMain() {
        ImGui.getStyle().setWindowRounding(6.5f);
        ImGui.styleColorsClassic();
        ImGui.begin("Byte", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoNav | ImGuiWindowFlags.NoDocking);
        renderCategories();

        ImGui.end();
    }

    public void renderCategories() {
        if (ImGui.beginChild("CategoryList", 200, 0, true)) {
            for (Category category : Category.values()) {
                boolean isSelected = category.equals(selectedCategory);
                if (ImGui.selectable(category.properName, isSelected)) {
                    selectedCategory = category;
                }
            }
            ImGui.endChild();
        }

        renderModules();
    }

    public void renderModules() {
        List<Module> modulesInCategory = ModuleManager.modules.stream()
                .filter(module -> module.getCategory() == selectedCategory).toList();

        ImGui.sameLine();

        if (ImGui.beginChild("Modules", 0, 0, true)) {
            if (selectedCategory != null) {
                for (Module module : modulesInCategory) {
                    if (ImGui.collapsingHeader(module.getName())) {
                        if (ImGui.isItemHovered()) {
                            ImGui.beginTooltip();
                            ImGui.text(module.getDescription());
                            ImGui.endTooltip();
                        }

                        if (ImGui.button(module.isEnabled() ? "Disable" : "Enable")) {
                            module.toggle();
                        }

                        for (Setting<?> setting : module.settings) {
                            renderSetting(module, setting);
                        }
                    }
                }
            }
            ImGui.endChild();
        }
    }

    private void renderSetting(Module module, Setting<?> setting) {
        String label = setting.getName() + "##" + module.getName();

        if (setting instanceof ModeSetting modeSetting) {
            var modes = modeSetting.getModes();
            String[] modesArray = modes.toArray(new String[0]);
            int currentModeIndex = modes.indexOf(modeSetting.getValue());
            ImInt currentIndex = new ImInt(currentModeIndex);

            if (ImGui.combo(label, currentIndex, modesArray)) {
                modeSetting.setValue(modes.get(currentIndex.get()));
            }
        }
        else if (setting instanceof NumberSetting numberSetting) {
            renderNumberSetting(numberSetting, label);
        }
        else if (setting instanceof BooleanSetting booleanSetting) {
            boolean active = booleanSetting.getValue();
            if (ImGui.checkbox(label, active)) {
                booleanSetting.setValue(!active);
            }
        }
        else if (setting instanceof ColorSetting colorSetting) {
            Color color = colorSetting.getValue();
            float[] colorArray = {
                    color.getRed() / 255f,
                    color.getGreen() / 255f,
                    color.getBlue() / 255f,
                    color.getAlpha() / 255f
            };

            if (ImGui.colorEdit4(label, colorArray)) {
                Color newColor = new Color(
                        (int) (colorArray[0] * 255),
                        (int) (colorArray[1] * 255),
                        (int) (colorArray[2] * 255),
                        (int) (colorArray[3] * 255)
                );
                colorSetting.setValue(newColor);
            }
        }
    }

    public void renderNumberSetting(Setting<?> setting, String label) {
        if (setting instanceof NumberSetting numberSetting) {
            Number value = numberSetting.getValue();
            Number minValue = numberSetting.getMinValue();
            Number maxValue = numberSetting.getMaxValue();

            if (value instanceof Integer) {
                ImInt valueArray = new ImInt(value.intValue());
                if (ImGui.sliderScalar(label, ImGuiDataType.S32, valueArray, minValue.intValue(), maxValue.intValue())) {
                    numberSetting.setValue(valueArray.get());
                }
            } else if (value instanceof Float) {
                ImFloat valueArray = new ImFloat(value.floatValue());
                if (ImGui.sliderScalar(label, ImGuiDataType.Float, valueArray, minValue.floatValue(), maxValue.floatValue(), "%.2f")) {
                    numberSetting.setValue(valueArray.get());
                }
            } else if (value instanceof Double) {
                ImDouble valueArray = new ImDouble(value.doubleValue());
                if (ImGui.sliderScalar(label, ImGuiDataType.Double, valueArray, minValue.doubleValue(), maxValue.doubleValue(), "%.2f")) {
                    numberSetting.setValue(valueArray.get());
                }
            }
        }
    }
}
