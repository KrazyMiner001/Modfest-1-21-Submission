package tech.krazyminer001.screen.gachamachine;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import tech.krazyminer001.networking.SnuggleVaultC2SPacketSender;
import tech.krazyminer001.screen.slots.DisplaySlot;
import static tech.krazyminer001.utility.Utility.of;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GachaMachineScreen extends HandledScreen<GachaMachineScreenHandler> {
    private static final Identifier TEXTURE = of("textures/gui/container/gacha_machine.png");

    public GachaMachineScreen(GachaMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth + 26, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        List<DisplaySlot> slots = this.handler.slots.stream().filter(slot -> slot instanceof DisplaySlot).map(slot -> (DisplaySlot) slot).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        for (DisplaySlot slot : slots) {
            context.drawGuiTexture(of("container/gachamachine/ball_green"), slot.x + x - 3, slot.y + y - 3, 22, 22);
        }
    }

    private <T extends ButtonWidget> void addButton(T button) {
        this.addDrawableChild(button);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;

        this.addButton(new TexturedButtonWidget(
                179 + x,
                y,
                18,
                45,
                new ButtonTextures(
                        of("container/gachamachine/lever_unselected"),
                        of("container/gachamachine/lever_selected")
                ),
                button -> rollGacha()
        ));


    }

    private void rollGacha() {
        SnuggleVaultC2SPacketSender.sendGachaMachineSpinPacket();
    }
}
