package tech.krazyminer001.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import tech.krazyminer001.SnuggleVault;

import static tech.krazyminer001.utility.Utility.of;

public class SnuggleVaultItems {
    public static final Item UNLOCKINATOR = register("unlockinator", new Item(new Item.Settings().maxCount(1)));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, of(name), item);
    }

    public static void registerItems() {
        SnuggleVault.LOGGER.info("Registering Items for " + SnuggleVault.MOD_ID);
    }
}
