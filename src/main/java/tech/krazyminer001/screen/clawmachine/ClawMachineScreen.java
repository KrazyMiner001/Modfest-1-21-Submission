package tech.krazyminer001.screen.clawmachine;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import tech.krazyminer001.networking.SnuggleVaultC2SPacketSender;

import java.util.List;

import static tech.krazyminer001.utility.Utility.of;

public class ClawMachineScreen extends HandledScreen<ClawMachineScreenHandler> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/dispenser.png");
    private final List<ButtonWidget> buttons = Lists.newArrayList();
    private boolean active = false;


    public ClawMachineScreen(ClawMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private <T extends ButtonWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add(button);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        this.buttons.clear();
        this.addButton(new TexturedButtonWidget(
                179 + x,
                y,
                18,
                45,
                new ButtonTextures(
                        of("container/gachamachine/lever_unselected"),
                        of("container/gachamachine/lever_selected")
                ),
                button -> startGame()
        ));

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_SPACE) {
            if (this.active) {
                SnuggleVaultC2SPacketSender.sendClawMachineEndPacket();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void startGame() {
        this.buttons.clear();
        this.active = true;
        SnuggleVaultC2SPacketSender.sendClawMachineStartPacket();
    }
}
