package tech.krazyminer001.block.gachamachine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import tech.krazyminer001.block.snugglevault.SnuggleVaultBlockEntity;
import tech.krazyminer001.screen.gachamachine.GachaMachineScreenHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class GachaMachineBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private DefaultedList<ItemStack> gachaItems() {
        DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);
        if (world == null) return items;
        
        BlockEntity worldBlockEntity = world.getBlockEntity(pos.down());
        if (!(worldBlockEntity instanceof SnuggleVaultBlockEntity snuggleVaultBlockEntity)) return items;
        
        snuggleVaultBlockEntity.markDirty();
        DefaultedList<ItemStack> tempItems = DefaultedList.of();
        tempItems.addAll(snuggleVaultBlockEntity.getItems());
        tempItems.removeFirst();
        AtomicInteger i = new AtomicInteger();
        tempItems.reversed().forEach(itemStack -> {
            if (itemStack.isEmpty()) return;
            items.set(i.getAndIncrement(), itemStack);
        });
        return items;
    }

    public GachaMachineBlockEntity(BlockPos pos, BlockState state) {
        super(SnuggleVaultBlockEntities.GACHA_MACHINE, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, items, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, items, registryLookup);
        super.writeNbt(nbt, registryLookup);
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
        assert world != null;
        if (!(world.getBlockEntity(pos.down()) instanceof SnuggleVaultBlockEntity snuggleVaultBlockEntity)) return null;
        return new GachaMachineScreenHandler(syncId, playerInventory, this, snuggleVaultBlockEntity);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
}
