package tech.krazyminer001.block.snugglevault;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.item.SnuggleVaultItems;

public class SnuggleVaultBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final MapCodec<SnuggleVaultBlock> CODEC = createCodec(SnuggleVaultBlock::new);
    public static final VoxelShape SHAPE = VoxelShapes.union(
            VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0/16, 1.0),
            VoxelShapes.cuboid(1.0/16, 1.0/16, 1.0/16, 15.0/16, 15.0/16, 15.0/16),
            VoxelShapes.cuboid(0.0, 15.0/16, 0.0, 1.0, 1.0, 1.0)
    );

    public SnuggleVaultBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        SnuggleVaultBlockEntity blockEntity = (SnuggleVaultBlockEntity) world.getBlockEntity(pos);
        if (blockEntity == null) return ActionResult.FAIL;
        if (player.getMainHandStack().getItem() == SnuggleVaultItems.UNLOCKINATOR) {
            blockEntity.setOwner(null);
            return ActionResult.SUCCESS;
        }
        if (!player.isSneaking()) {
            if (blockEntity.getOwner() == player.getUuid() || blockEntity.getOwner() == null) {
                //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity cast to
                //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
                NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

                if (screenHandlerFactory != null) {
                    //With this call the server will request the client to open the appropriate Screenhandler
                    player.openHandledScreen(screenHandlerFactory);
                    return ActionResult.SUCCESS;
                }
            } else {
                player.sendMessage(Text.translatable("block.snugglevault.snugglevault.not_owned"), true);
                return ActionResult.FAIL;
            }
        }
        if (blockEntity.getOwner() == null) {
            blockEntity.setOwner(player.getUuid());
            return ActionResult.SUCCESS;
        }
        if (blockEntity.getOwner() == player.getUuid()) {
            blockEntity.setOwner(null);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SnuggleVaultBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
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
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
