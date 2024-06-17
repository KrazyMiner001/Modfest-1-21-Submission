package tech.krazyminer001.block.snugglevault;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CreativeSnuggleVaultBlock extends SnuggleVaultBlock {
    public CreativeSnuggleVaultBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CreativeSnuggleVaultBlockEntity(pos, state);
    }
}
