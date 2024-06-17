package tech.krazyminer001.block.gachamachine;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.utility.Utility;

public class GachaMachineBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final MapCodec<GachaMachineBlock> CODEC = createCodec(GachaMachineBlock::new);
    public static final VoxelShape BASE = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0 / 16, 1.0);
    public static final VoxelShape CONTAINER = VoxelShapes.cuboid(4.0 / 16, 1.0 / 16, 1.0 / 16, 15.0 / 16, 11.0 / 16, 15.0 / 16);
    public static final VoxelShape COIN_SLOT = VoxelShapes.cuboid(1.0 / 16, 1.0 / 16, 1.5 / 16, 4.0 / 16, 5.0 / 16, 3.5 / 16);


    public GachaMachineBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GachaMachineBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            // This will call the createScreenHandlerFactory method from BlockWithEntity,
            // which will return our blockEntity cast to a namedScreenHandlerFactory
            // (as we extend BlockWithEntity).
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                // The server requests the client to open the Gacha screen handler
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
   
    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        PlayerEntity placer = ctx.getPlayer();
        if (placer == null) {
            return super.getPlacementState(ctx);
        }
        return this.getDefaultState().with(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(
                Utility.rotateShape(BASE, state.get(FACING)),
                Utility.rotateShape(CONTAINER, state.get(FACING)),
                Utility.rotateShape(COIN_SLOT, state.get(FACING))
        );
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(
                Utility.rotateShape(BASE, state.get(FACING)),
                Utility.rotateShape(CONTAINER, state.get(FACING)),
                Utility.rotateShape(COIN_SLOT, state.get(FACING))
        );
    }
}
