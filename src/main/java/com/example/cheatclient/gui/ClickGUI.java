package com.example.cheatclient.gui;

import com.example.cheatclient.CheatManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.awt.Color;

public class ClickGUI extends Screen {
    private int selectedTab = 0;
    private final String[] tabs = {"COMBAT", "MOVEMENT", "PLAYER", "MISC"};

    // Fareyle sürüklenen slider'ı takip etmek için
    private int activeSlider = -1;

    public ClickGUI() { super(Text.literal("Gigantium Cheats")); }

    @Override
    protected void init() {
        // Ekran açıldığında hiçbir vanilla buton eklemiyoruz.
        // Her şeyi aşağıda kendi çizim fonksiyonumuzla yapacağız.
        this.clearChildren();
    }

    @Override
    public boolean shouldPause() { return false; }

    // Arka planı karartmamak ve blur'u kapatmak için renderBackground'u eziyoruz
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Sadece hafif bir karartma ekle, blur yok.
        context.fill(0, 0, this.width, this.height, new Color(0, 0, 0, 120).getRGB());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Arka planı çiz
        this.renderBackground(context, mouseX, mouseY, delta);

        int cx = this.width / 2 - 220; // Ana pencere X
        int cy = this.height / 2 - 160; // Ana pencere Y
        int w = 440;
        int h = 320;

        // 1. ANA PENCERE ARKA PLANI (Tam Opak, Koyu Tema)
        drawRoundedRect(context, cx, cy, w, h, 6, new Color(22, 22, 26, 255).getRGB());
        
        // 2. SOL MENÜ ÇUBUĞU (Sidebar)
        drawRoundedRect(context, cx, cy, 110, h, 6, new Color(16, 16, 20, 255).getRGB());
        // Sağ üst köşenin yuvarlaklığını düzeltmek için sidebar'ın sağ tarafını kapat
        context.fill(cx + 100, cy, cx + 110, cy + h, new Color(16, 16, 20, 255).getRGB());

        // Başlık
        context.drawText(this.textRenderer, "§b§lGIGANTIUM", cx + 15, cy + 15, -1, true);
        context.drawText(this.textRenderer, "§7Cheats v1.0", cx + 15, cy + 28, new Color(150, 150, 150).getRGB(), false);

        // Sekmeler
        for (int i = 0; i < tabs.length; i++) {
            int ty = cy + 50 + (i * 25);
            boolean hover = mouseX >= cx && mouseX <= cx + 110 && mouseY >= ty && mouseY <= ty + 20;
            int color = selectedTab == i ? new Color(100, 150, 255).getRGB() : (hover ? new Color(200, 200, 200).getRGB() : new Color(120, 120, 120).getRGB());
            
            if (selectedTab == i) {
                // Seçili sekme çizgisi
                context.fill(cx + 5, ty, cx + 8, ty + 20, new Color(100, 150, 255).getRGB());
                drawRoundedRect(context, cx + 10, ty, 95, 20, 4, new Color(255, 255, 255, 15).getRGB());
            }
            context.drawText(this.textRenderer, tabs[i], cx + 18, ty + 6, color, false);
        }

        // 3. İÇERİK ALANI (Tıklanabilir Modüller)
        if (selectedTab == 0) { // COMBAT
            drawToggle(context, "Aimbot", CheatManager.aimbot, cx + 130, cy + 50, mouseX, mouseY);
            drawToggle(context, "KillAura", CheatManager.killAura, cx + 130, cy + 80, mouseX, mouseY);
            drawToggle(context, "SilentAim", CheatManager.silentAim, cx + 130, cy + 110, mouseX, mouseY);
            drawToggle(context, "Spinbot", CheatManager.spinbot, cx + 130, cy + 140, mouseX, mouseY);
            drawToggle(context, "Jitter", CheatManager.jitter, cx + 130, cy + 170, mouseX, mouseY);
            
            drawToggle(context, "Bunnyhop", CheatManager.bunnyhop, cx + 290, cy + 50, mouseX, mouseY);
            drawToggle(context, "Runboost", CheatManager.runboost, cx + 290, cy + 80, mouseX, mouseY);
            drawToggle(context, "Auto Totem", CheatManager.autoTotem, cx + 290, cy + 110, mouseX, mouseY);
            drawToggle(context, "No Clip", CheatManager.noClip, cx + 290, cy + 140, mouseX, mouseY);
            drawToggle(context, "Teleport", CheatManager.teleport, cx + 290, cy + 170, mouseX, mouseY);

            // Slider'lar (Kendi çizimimiz)
            drawSlider(context, "Spin Speed", CheatManager.spinSpeed, 1, 180, cx + 130, cy + 210, 300, mouseX, mouseY, 0);
            drawSlider(context, "SilentAim FOV", CheatManager.silentAimFov, 30, 360, cx + 130, cy + 240, 300, mouseX, mouseY, 1);
            drawSlider(context, "KillAura Range", CheatManager.killAuraRange, 3, 6, cx + 130, cy + 270, 300, mouseX, mouseY, 2);
        } else if (selectedTab == 1) { // MOVEMENT
            drawToggle(context, "Bunnyhop", CheatManager.bunnyhop, cx + 130, cy + 50, mouseX, mouseY);
            drawToggle(context, "Runboost", CheatManager.runboost, cx + 130, cy + 80, mouseX, mouseY);
            drawToggle(context, "No Clip", CheatManager.noClip, cx + 130, cy + 110, mouseX, mouseY);
            
            drawSlider(context, "Runboost Mult", CheatManager.runboostMultiplier, 1.0f, 3.0f, cx + 130, cy + 150, 300, mouseX, mouseY, 3);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    // --- ÇİZİM YARDIMCILARI ---

    private void drawToggle(DrawContext context, String name, boolean state, int x, int y, int mx, int my) {
        int w = 140, h = 22;
        drawRoundedRect(context, x, y, w, h, 4, new Color(35, 35, 40, 255).getRGB());
        
        context.drawText(this.textRenderer, name, x + 10, y + 7, new Color(220, 220, 220).getRGB(), false);

        int swX = x + w - 30;
        int swY = y + 5;
        // Switch arka planı
        drawRoundedRect(context, swX, swY, 22, 12, 6, state ? new Color(100, 150, 255).getRGB() : new Color(60, 60, 65).getRGB());
        // Switch topu
        drawRoundedRect(context, swX + (state ? 11 : 1), swY - 2, 14, 14, 7, new Color(240, 240, 240).getRGB());
    }

    private void drawSlider(DrawContext context, String name, float val, float min, float max, int x, int y, int w, int mx, int my, int id) {
        int h = 20;
        drawRoundedRect(context, x, y, w, h, 4, new Color(35, 35, 40, 255).getRGB());
        
        context.drawText(this.textRenderer, name, x + 8, y + 6, new Color(200, 200, 200).getRGB(), false);
        context.drawText(this.textRenderer, String.format("%.1f", val), x + w - 30, y + 6, new Color(100, 150, 255).getRGB(), false);

        // Slider çubuğu
        int barX = x + 90;
        int barY = y + 9;
        int barW = w - 130;
        
        drawRoundedRect(context, barX, barY, barW, 2, 1, new Color(50, 50, 60).getRGB());
        
        float pct = (val - min) / (max - min);
        int fillW = (int)(barW * pct);
        drawRoundedRect(context, barX, barY, fillW, 2, 1, new Color(100, 150, 255).getRGB());
        
        // Slider topuzu
        drawRoundedRect(context, barX + fillW - 3, barY - 3, 6, 8, 3, new Color(255, 255, 255).getRGB());
    }

    private void drawRoundedRect(DrawContext context, int x, int y, int w, int h, int r, int color) {
        context.fill(x + r, y, x + w - r, y + h, color);
        context.fill(x, y + r, x + r, y + h - r, color);
        context.fill(x + w - r, y + r, x + w, y + h - r, color);
        // Köşe dolguları (daha yuvarlak görünmesi için)
        context.fill(x + 1, y + 1, x + r, y + r, color);
        context.fill(x + w - r, y + 1, x + w - 1, y + r, color);
        context.fill(x + 1, y + h - r, x + r, y + h - 1, color);
        context.fill(x + w - r, y + h - r, x + w - 1, y + h - 1, color);
    }

    // --- ETKİLEŞİM (FARE) ---

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int cx = this.width / 2 - 220;
        int cy = this.height / 2 - 160;

        // Sekme tıklamaları
        for (int i = 0; i < tabs.length; i++) {
            int ty = cy + 50 + (i * 25);
            if (mx >= cx && mx <= cx + 110 && my >= ty && my <= ty + 20) {
                selectedTab = i;
                return true;
            }
        }

        // Toggle tıklamaları
        if (selectedTab == 0) {
            if (clickToggle(mx, my, cx + 130, cy + 50)) CheatManager.aimbot = !CheatManager.aimbot;
            if (clickToggle(mx, my, cx + 130, cy + 80)) CheatManager.killAura = !CheatManager.killAura;
            if (clickToggle(mx, my, cx + 130, cy + 110)) CheatManager.silentAim = !CheatManager.silentAim;
            if (clickToggle(mx, my, cx + 130, cy + 140)) CheatManager.spinbot = !CheatManager.spinbot;
            if (clickToggle(mx, my, cx + 130, cy + 170)) CheatManager.jitter = !CheatManager.jitter;

            if (clickToggle(mx, my, cx + 290, cy + 50)) CheatManager.bunnyhop = !CheatManager.bunnyhop;
            if (clickToggle(mx, my, cx + 290, cy + 80)) CheatManager.runboost = !CheatManager.runboost;
            if (clickToggle(mx, my, cx + 290, cy + 110)) CheatManager.autoTotem = !CheatManager.autoTotem;
            if (clickToggle(mx, my, cx + 290, cy + 140)) CheatManager.noClip = !CheatManager.noClip;
            if (clickToggle(mx, my, cx + 290, cy + 170)) CheatManager.teleport = !CheatManager.teleport;

            // Slider tıklama algılama
            if (clickSlider(mx, my, cx + 130, cy + 210, 300)) activeSlider = 0;
            if (clickSlider(mx, my, cx + 130, cy + 240, 300)) activeSlider = 1;
            if (clickSlider(mx, my, cx + 130, cy + 270, 300)) activeSlider = 2;
        } else if (selectedTab == 1) {
            if (clickToggle(mx, my, cx + 130, cy + 50)) CheatManager.bunnyhop = !CheatManager.bunnyhop;
            if (clickToggle(mx, my, cx + 130, cy + 80)) CheatManager.runboost = !CheatManager.runboost;
            if (clickToggle(mx, my, cx + 130, cy + 110)) CheatManager.noClip = !CheatManager.noClip;

            if (clickSlider(mx, my, cx + 130, cy + 150, 300)) activeSlider = 3;
        }

        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        activeSlider = -1;
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        int cx = this.width / 2 - 220;
        if (activeSlider != -1) {
            // Slider çubuğunun başlangıç noktası X: cx + 130 + 90 = cx + 220
            double relX = mx - (cx + 220);
            double pct = Math.max(0, Math.min(1, relX / 170.0)); // 170 = bar genişliği

            if (activeSlider == 0) CheatManager.spinSpeed = (float)(1 + pct * 179);
            if (activeSlider == 1) CheatManager.silentAimFov = (float)(30 + pct * 330);
            if (activeSlider == 2) CheatManager.killAuraRange = (float)(3 + pct * 3);
            if (activeSlider == 3) CheatManager.runboostMultiplier = (float)(1.0 + pct * 2.0);
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    private boolean clickToggle(double mx, double my, int x, int y) {
        return mx >= x && mx <= x + 140 && my >= y && my <= y + 22;
    }

    private boolean clickSlider(double mx, double my, int x, int y, int w) {
        return mx >= x && mx <= x + w && my >= y && my <= y + 20;
    }
}
