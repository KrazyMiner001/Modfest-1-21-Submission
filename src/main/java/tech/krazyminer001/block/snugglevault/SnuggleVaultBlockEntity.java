package tech.krazyminer001.block.snugglevault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.api.ImplementedInventory;
import tech.krazyminer001.block.SnuggleVaultBlockEntities;
import tech.krazyminer001.screen.snugglevault.SnuggleVaultScreenHandler;

import java.util.UUID;

public class SnuggleVaultBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
    protected final DefaultedList<ItemStack> items = DefaultedList.ofSize(28, ItemStack.EMPTY);
    private UUID owner = null;

    public SnuggleVaultBlockEntity(BlockPos pos, BlockState state) {
        this(SnuggleVaultBlockEntities.SNUGGLE_VAULT, pos, state);
    }

    public SnuggleVaultBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        markDirty();
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, items, registryLookup);
        if (nbt.contains("owner")) {
            this.owner = nbt.getUuid("owner");
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, items, registryLookup);
        if (this.owner != null) {
            nbt.putUuid("owner", this.owner);
        }
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void markDirty() {
        assert world != null;
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        super.markDirty();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SnuggleVaultScreenHandler(syncId, playerInventory, this);
    }

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
        this.setStack(index + 1, ItemStack.EMPTY);
        return true;
    }
}
