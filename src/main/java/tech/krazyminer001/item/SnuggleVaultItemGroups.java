package tech.krazyminer001.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import tech.krazyminer001.SnuggleVault;
import tech.krazyminer001.block.SnuggleVaultBlocks;

import static tech.krazyminer001.utility.Utility.of;

public class SnuggleVaultItemGroups {
    public static final ItemGroup SNUGGLE_VAULT = register("snuggle_vault",
            FabricItemGroup.builder()
                    .icon(() -> SnuggleVaultBlocks.SNUGGLE_VAULT.asItem().getDefaultStack())
                    .displayName(Text.translatable("item_group.snugglevault.snuggle_vault"))
                    .entries((displayContext, entries) -> {
                        entries.add(SnuggleVaultBlocks.SNUGGLE_VAULT.asItem().getDefaultStack());
                        entries.add(SnuggleVaultItems.UNLOCKINATOR.getDefaultStack());
                    })
                    .build());

    private static ItemGroup register(String id, ItemGroup group) {
        return Registry.register(Registries.ITEM_GROUP, of(id), group);
    }

    public static void registerItemGroups() {
        SnuggleVault.LOGGER.info("Registering Item Groups for " + SnuggleVault.MOD_ID);
    }
}
