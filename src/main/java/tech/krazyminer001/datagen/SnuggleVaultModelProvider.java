package tech.krazyminer001.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import tech.krazyminer001.block.SnuggleVaultBlocks;
import tech.krazyminer001.item.SnuggleVaultItems;

public class SnuggleVaultModelProvider extends FabricModelProvider {
    public SnuggleVaultModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(SnuggleVaultBlocks.SNUGGLE_VAULT)
                .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
                        .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(SnuggleVaultBlocks.SNUGGLE_VAULT, "_east")))
                        .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(SnuggleVaultBlocks.SNUGGLE_VAULT, "_west")))
                        .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(SnuggleVaultBlocks.SNUGGLE_VAULT, "_south")))
                        .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(SnuggleVaultBlocks.SNUGGLE_VAULT, "_north")))
        ));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(SnuggleVaultItems.UNLOCKINATOR, Models.GENERATED);
    }
}
