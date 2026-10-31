package com.example.cheatclient.gui;

import com.example.cheatclient.CheatManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickGUI extends Screen {
    public ClickGUI() { super(Text.literal("Cheat GUI")); }

    @Override
    protected void init() {
        int x = 20, y = 20, w = 120, h = 20, s = 24;
        addToggle("KillAura", CheatManager.killAura, v -> CheatManager.killAura = v, x, y); y += s;
        addToggle("SilentAim", CheatManager.silentAim, v -> CheatManager.silentAim = v, x, y); y += s;
        addToggle("Reach", CheatManager.reach, v -> CheatManager.reach = v, x, y); y += s;
        addToggle("Spinbot", CheatManager.spinbot, v -> CheatManager.spinbot = v, x, y); y += s;
        addToggle("Jitter", CheatManager.jitter, v -> CheatManager.jitter = v, x, y); y += s;
        addToggle("Bunnyhop", CheatManager.bunnyhop, v -> CheatManager.bunnyhop = v, x, y); y += s;
        addToggle("Runboost", CheatManager.runboost, v -> CheatManager.runboost = v, x, y);
    }

    private void addToggle(String name, boolean state, java.util.function.Consumer<Boolean> setter, int x, int y) {
        this.addDrawableChild(ButtonWidget.builder(Text.literal(name + ": " + (state ? "ON" : "OFF")), b -> {
            boolean n = !b.getMessage().getString().endsWith("ON");
            setter.accept(n);
            b.setMessage(Text.literal(name + ": " + (n ? "ON" : "OFF")));
        }).dimensions(x, y, 120, 20).build());
    }

    @Override public boolean shouldPause() { return false; }
    @Override public void render(DrawContext context, int mx, int my, float d) {
        renderBackground(context, mx, my, d);
        super.render(context, mx, my, d);
    }
}
