package tech.krazyminer001.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import tech.krazyminer001.screen.clawmachine.ClawMachineScreen;
import tech.krazyminer001.screen.gachamachine.GachaMachineScreen;
import tech.krazyminer001.screen.snugglevault.SnuggleVaultScreen;

@Environment(EnvType.CLIENT)
public class SnuggleVaultScreens {
    public static void registerScreens() {
        HandledScreens.register(SnuggleVaultScreenHandlers.SNUGGLE_VAULT_SCREEN_HANDLER, SnuggleVaultScreen::new);
        HandledScreens.register(SnuggleVaultScreenHandlers.CLAW_MACHINE_SCREEN_HANDLER, ClawMachineScreen::new);
        HandledScreens.register(SnuggleVaultScreenHandlers.GACHA_MACHINE_SCREEN_HANDLER, GachaMachineScreen::new);
    }
}
