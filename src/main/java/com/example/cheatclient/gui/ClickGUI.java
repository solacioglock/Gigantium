package com.example.cheatclient.gui;

import com.example.cheatclient.CheatManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.MinecraftClient;

import java.awt.Color;

public class ClickGUI extends Screen {
    private int selectedTab = 0;
    private final String[] tabs = {"COMBAT", "MOVEMENT", "PLAYER", "MISC"};

    public ClickGUI() { super(Text.literal("Gigantium Cheats")); }

    @Override
    protected void init() {
        // Ekran yeniden boyutlandığında Slider'ları tekrar ekle
        this.clearChildren();
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        if (selectedTab == 0) {
            addSlider("Spin Speed", centerX + 10, centerY - 10, 100, 20, CheatManager.spinSpeed, 1, 180, v -> CheatManager.spinSpeed = v);
            addSlider("Silent Aim FOV", centerX + 10, centerY + 15, 100, 20, CheatManager.silentAimFov, 30, 360, v -> CheatManager.silentAimFov = v);
            addSlider("KillAura Range", centerX + 10, centerY + 40, 100, 20, CheatManager.killAuraRange, 3, 6, v -> CheatManager.killAuraRange = v);
        } else if (selectedTab == 1) {
            addSlider("Runboost Mult", centerX + 10, centerY - 10, 100, 20, CheatManager.runboostMultiplier, 1.0f, 3.0f, v -> CheatManager.runboostMultiplier = v);
        }
    }

    private void addSlider(String name, int x, int y, int w, int h, float current, float min, float max, java.util.function.Consumer<Float> setter) {
        this.addDrawableChild(new SliderWidget(x, y, w, h, Text.literal(name), (current - min) / (max - min)) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal(name + ": " + String.format("%.1f", min + (max - min) * this.value)));
            }
            @Override
            protected void applyValue() {
                setter.accept((float)(min + (max - min) * this.value));
            }
        });
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Arka plan karartma
        context.fill(0, 0, this.width, this.height, new Color(0, 0, 0, 150).getRGB());

        int cx = this.width / 2 - 200; // Ana pencere X
        int cy = this.height / 2 - 150; // Ana pencere Y
        int w = 400;
        int h = 300;

        // Ana pencere arka planı
        drawRoundedRect(context, cx, cy, w, h, 8, new Color(20, 20, 25, 255).getRGB());
        // Sol Menü Çubuğu (Sidebar)
        drawRoundedRect(context, cx, cy, 100, h, 8, new Color(15, 15, 20, 255).getRGB());
        
        // Gigantium Cheats Başlığı
        context.drawText(this.textRenderer, "§b§lGigantium §fCheats", cx + 10, cy + 15, -1, true);

        // Sidebar Sekmeleri
        for (int i = 0; i < tabs.length; i++) {
            int ty = cy + 40 + (i * 25);
            boolean hover = mouseX >= cx && mouseX <= cx + 100 && mouseY >= ty && mouseY <= ty + 20;
            int color = selectedTab == i ? new Color(60, 60, 80).getRGB() : (hover ? new Color(40, 40, 50).getRGB() : -1);
            
            if (selectedTab == i) context.fill(cx + 5, ty, cx + 95, ty + 20, new Color(255, 255, 255, 30).getRGB());
            context.drawText(this.textRenderer, tabs[i], cx + 15, ty + 6, color, false);
        }

        // Toggle Butonlarını Çiz
        drawToggle(context, "Aimbot", CheatManager.aimbot, cx + 120, cy + 40, mouseX, mouseY);
        drawToggle(context, "KillAura", CheatManager.killAura, cx + 120, cy + 70, mouseX, mouseY);
        drawToggle(context, "SilentAim", CheatManager.silentAim, cx + 120, cy + 100, mouseX, mouseY);
        drawToggle(context, "Spinbot", CheatManager.spinbot, cx + 120, cy + 130, mouseX, mouseY);
        drawToggle(context, "Jitter", CheatManager.jitter, cx + 120, cy + 160, mouseX, mouseY);
        
        drawToggle(context, "Bunnyhop", CheatManager.bunnyhop, cx + 280, cy + 40, mouseX, mouseY);
        drawToggle(context, "Runboost", CheatManager.runboost, cx + 280, cy + 70, mouseX, mouseY);
        drawToggle(context, "Auto Totem", CheatManager.autoTotem, cx + 280, cy + 100, mouseX, mouseY);
        drawToggle(context, "No Clip", CheatManager.noClip, cx + 280, cy + 130, mouseX, mouseY);
        drawToggle(context, "Teleport", CheatManager.teleport, cx + 280, cy + 160, mouseX, mouseY);

        // Slider'ları ve diğer widget'ları çiz
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawToggle(DrawContext context, String name, boolean state, int x, int y, int mx, int my) {
        int width = 110;
        int height = 20;
        
        // Kutu arka planı
        drawRoundedRect(context, x, y, width, height, 4, new Color(30, 30, 38, 200).getRGB());
        
        // İsim
        context.drawText(this.textRenderer, name, x + 8, y + 6, new Color(200, 200, 200).getRGB(), false);

        // Toggle Switch (Yuvarlak animasyonlu)
        int swX = x + width - 30;
        int swY = y + 4;
        drawRoundedRect(context, swX, swY, 24, 12, 6, state ? new Color(80, 120, 255).getRGB() : new Color(60, 60, 70).getRGB());
        drawRoundedRect(context, swX + (state ? 12 : 0), swY - 2, 16, 16, 8, new Color(240, 240, 240).getRGB());
    }

    private void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        // DrawContext vanilla yuvarlak çizim desteklemediği için köşeleri ve ortayı dolduruyoruz
        context.fill(x + radius, y, x + width - radius, y + height, color);
        context.fill(x, y + radius, x + width, y + height - radius, color);
        // Basit köşe dolgusu (tam yuvarlak değil ama şık durur)
        context.fill(x + 2, y + 2, x + radius, y + radius, color);
        context.fill(x + width - radius, y + 2, x + width - 2, y + radius, color);
        context.fill(x + 2, y + height - radius, x + radius, y + height - 2, color);
        context.fill(x + width - radius, y + height - radius, x + width - 2, y + height - 2, color);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int cx = this.width / 2 - 200;
        int cy = this.height / 2 - 150;

        // Sidebar tıklamaları
        for (int i = 0; i < tabs.length; i++) {
            int ty = cy + 40 + (i * 25);
            if (mouseX >= cx && mouseX <= cx + 100 && mouseY >= ty && mouseY <= ty + 20) {
                selectedTab = i;
                this.init(); // Slider'ları güncelle
                return true;
            }
        }

        // Toggle tıklamaları (Sadece Combat sekmesinde çalışsın diye kaba bir kontrol)
        if (selectedTab == 0) {
            if (clickToggle(mouseX, mouseY, cx + 120, cy + 40, 110, 20)) CheatManager.aimbot = !CheatManager.aimbot;
            if (clickToggle(mouseX, mouseY, cx + 120, cy + 70, 110, 20)) CheatManager.killAura = !CheatManager.killAura;
            if (clickToggle(mouseX, mouseY, cx + 120, cy + 100, 110, 20)) CheatManager.silentAim = !CheatManager.silentAim;
            if (clickToggle(mouseX, mouseY, cx + 120, cy + 130, 110, 20)) CheatManager.spinbot = !CheatManager.spinbot;
            if (clickToggle(mouseX, mouseY, cx + 120, cy + 160, 110, 20)) CheatManager.jitter = !CheatManager.jitter;

            if (clickToggle(mouseX, mouseY, cx + 280, cy + 40, 110, 20)) CheatManager.bunnyhop = !CheatManager.bunnyhop;
            if (clickToggle(mouseX, mouseY, cx + 280, cy + 70, 110, 20)) CheatManager.runboost = !CheatManager.runboost;
            if (clickToggle(mouseX, mouseY, cx + 280, cy + 100, 110, 20)) CheatManager.autoTotem = !CheatManager.autoTotem;
            if (clickToggle(mouseX, mouseY, cx + 280, cy + 130, 110, 20)) CheatManager.noClip = !CheatManager.noClip;
            if (clickToggle(mouseX, mouseY, cx + 280, cy + 160, 110, 20)) CheatManager.teleport = !CheatManager.teleport;
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean clickToggle(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
