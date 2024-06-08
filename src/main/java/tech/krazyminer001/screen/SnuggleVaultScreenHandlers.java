package tech.krazyminer001.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import tech.krazyminer001.SnuggleVault;
import tech.krazyminer001.screen.snugglevault.SnuggleVaultScreenHandler;

import static tech.krazyminer001.utility.Utility.of;

public class SnuggleVaultScreenHandlers {
    public static final ScreenHandlerType<SnuggleVaultScreenHandler> SNUGGLE_VAULT_SCREEN_HANDLER = registerScreenHandlerType("snuggle_vault",
            new ScreenHandlerType<>(SnuggleVaultScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

    private static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandlerType(String name, ScreenHandlerType<T> screenHandlerType) {
        return Registry.register(Registries.SCREEN_HANDLER, of(name), screenHandlerType);
    }

    public static void registerScreenHandlers() {
        SnuggleVault.LOGGER.info("Registering screen handlers for " + SnuggleVault.MOD_ID);
    }
}
