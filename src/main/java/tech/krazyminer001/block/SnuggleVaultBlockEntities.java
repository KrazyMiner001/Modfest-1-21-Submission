package tech.krazyminer001.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import tech.krazyminer001.SnuggleVault;
import tech.krazyminer001.block.clawmachine.ClawMachineBlockEntity;
import tech.krazyminer001.block.gachamachine.GachaMachineBlockEntity;
import tech.krazyminer001.block.snugglevault.CreativeSnuggleVaultBlockEntity;
import tech.krazyminer001.block.snugglevault.SnuggleVaultBlockEntity;

import static tech.krazyminer001.utility.Utility.of;

public class SnuggleVaultBlockEntities {
    public static final BlockEntityType<SnuggleVaultBlockEntity> SNUGGLE_VAULT = register("snuggle_vault",
        BlockEntityType.Builder.create(SnuggleVaultBlockEntity::new,
                SnuggleVaultBlocks.SNUGGLE_VAULT).build());

    public static final BlockEntityType<CreativeSnuggleVaultBlockEntity> CREATIVE_SNUGGLE_VAULT = register("creative_snuggle_vault",
        BlockEntityType.Builder.create(CreativeSnuggleVaultBlockEntity::new,
                SnuggleVaultBlocks.CREATIVE_SNUGGLE_VAULT).build());

    public static final BlockEntityType<ClawMachineBlockEntity> CLAW_MACHINE = register("claw_machine",
        BlockEntityType.Builder.create(ClawMachineBlockEntity::new,
                SnuggleVaultBlocks.CLAW_MACHINE).build());

    public static final BlockEntityType<GachaMachineBlockEntity> GACHA_MACHINE = register("gacha_machine",
        BlockEntityType.Builder.create(GachaMachineBlockEntity::new,
                SnuggleVaultBlocks.GACHA_MACHINE).build());

    private static <T extends BlockEntity>BlockEntityType<T> register(String id, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, of(id), type);
    }

    public static void registerBlockEntities() {
        SnuggleVault.LOGGER.info("Registering Block Entities for " + SnuggleVault.MOD_ID);
    }
}
