package tech.krazyminer001.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import tech.krazyminer001.SnuggleVault;
import tech.krazyminer001.block.clawmachine.ClawMachineBlock;
import tech.krazyminer001.block.gachamachine.GachaMachineBlock;
import tech.krazyminer001.block.snugglevault.CreativeSnuggleVaultBlock;
import tech.krazyminer001.block.snugglevault.SnuggleVaultBlock;

import static tech.krazyminer001.utility.Utility.of;

public class SnuggleVaultBlocks {
    public static final Block SNUGGLE_VAULT = registerWithItem("snuggle_vault", new SnuggleVaultBlock(AbstractBlock.Settings.create().nonOpaque()));
    public static final Block CREATIVE_SNUGGLE_VAULT = registerWithItem("creative_snuggle_vault", new CreativeSnuggleVaultBlock(AbstractBlock.Settings.create().nonOpaque()));
    public static final Block CLAW_MACHINE = registerWithItem("claw_machine", new ClawMachineBlock(AbstractBlock.Settings.create().nonOpaque()));
    public static final Block GACHA_MACHINE = registerWithItem("gacha_machine", new GachaMachineBlock(AbstractBlock.Settings.create().nonOpaque()));

    private static Block register(String id, Block block) {
        return Registry.register(Registries.BLOCK, of(id), block);
    }

    private static Block registerWithItem(String id, Block block) {
        Block registeredBlock = register(id, block);
        Registry.register(Registries.ITEM, of(id), new BlockItem(registeredBlock, new Item.Settings()));
        return registeredBlock;
    }

    public static void registerBlocks() {
        SnuggleVault.LOGGER.info("Registering Blocks for " + SnuggleVault.MOD_ID);
    }
}
