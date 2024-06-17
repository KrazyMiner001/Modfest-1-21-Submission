package tech.krazyminer001.block.snugglevault;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import tech.krazyminer001.block.SnuggleVaultBlockEntities;

public class CreativeSnuggleVaultBlockEntity extends SnuggleVaultBlockEntity {
    public CreativeSnuggleVaultBlockEntity(BlockPos pos, BlockState state) {
        super(SnuggleVaultBlockEntities.CREATIVE_SNUGGLE_VAULT, pos, state);
    }

    @Override
    public boolean dispenseItem(int index) {
        if (index > 27 || world == null || world.isClient()) {
            return false;
        }
        double xOffset = switch (world.getBlockState(pos).get(SnuggleVaultBlock.FACING)) {
            case NORTH -> -3.0 / 16;
            case SOUTH -> 3.0 / 16;
            case EAST -> 0.5;
            case WEST -> -0.5;
            default -> 0.0;
        };
        double zOffset = switch (world.getBlockState(pos).get(SnuggleVaultBlock.FACING)) {
            case NORTH -> -0.5;
            case SOUTH -> 0.5;
            case EAST -> -3.0 / 16;
            case WEST -> 3.0 / 16;
            default -> 0.0;
        };
        world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5 + xOffset, pos.getY() + 11.0 / 16, pos.getZ() + 0.5 + zOffset, items.get(index + 1)));
        return true;
    }
}
