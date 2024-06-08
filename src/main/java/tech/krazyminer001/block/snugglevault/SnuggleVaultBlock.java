package tech.krazyminer001.block.snugglevault;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.item.SnuggleVaultItems;

public class SnuggleVaultBlock extends BlockWithEntity {
    public static final DirectionProperty DIRECTION = Properties.HORIZONTAL_FACING;
    public static final MapCodec<SnuggleVaultBlock> CODEC = createCodec(SnuggleVaultBlock::new);

    public SnuggleVaultBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (placer == null || world.isClient) return;
        if (!placer.isSneaking()) {
            world.setBlockState(pos, state.with(DIRECTION, placer.getHorizontalFacing().getOpposite()));
            return;
        }
        world.setBlockState(pos, state.with(DIRECTION, placer.getHorizontalFacing()));
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
        builder.add(DIRECTION);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(DIRECTION, rotation.rotate(state.get(DIRECTION)));
    }
}
