package tech.krazyminer001.block.clawmachine;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.block.SnuggleVaultBlockEntities;

public class ClawMachineBlock extends BlockWithEntity {
    public static final MapCodec<ClawMachineBlock> CODEC = createCodec(ClawMachineBlock::new);

    public ClawMachineBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ClawMachineBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            // This will call the createScreenHandlerFactory method from BlockWithEntity,
            // which will return our blockEntity cast to a namedScreenHandlerFactory
            // (since we extend BlockWithEntity)
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                // The server requests the client to open the Claw Machine screen handler
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return validateTicker(type, SnuggleVaultBlockEntities.CLAW_MACHINE, ClawMachineBlockEntity::clientTick);
        } else {
            return validateTicker(type, SnuggleVaultBlockEntities.CLAW_MACHINE, ClawMachineBlockEntity::serverTick);
        }
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> validateTicker(World world, BlockEntityType<T> givenType, BlockEntityType<? extends AbstractFurnaceBlockEntity> expectedType) {
        return world.isClient ? null : validateTicker(givenType, expectedType, AbstractFurnaceBlockEntity::tick);
    }
}
