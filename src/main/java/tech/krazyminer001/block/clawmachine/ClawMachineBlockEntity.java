package tech.krazyminer001.block.clawmachine;

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
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.api.ImplementedInventory;
import tech.krazyminer001.block.SnuggleVaultBlockEntities;
import tech.krazyminer001.block.snugglevault.SnuggleVaultBlockEntity;
import tech.krazyminer001.screen.clawmachine.ClawMachineScreenHandler;

public class ClawMachineBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int progress;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return progress;
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
            }
        }

        @Override
        public int size() {
            return 1;
        }
    };
    private boolean active = false;

    public ClawMachineBlockEntity(BlockPos pos, BlockState state) {
        super(SnuggleVaultBlockEntities.CLAW_MACHINE, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, items, registryLookup);
        nbt.getBoolean("active");
        nbt.putInt("progress", progress);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.writeNbt(nbt, items, registryLookup);
        nbt.putBoolean("active", active);
        nbt.putInt("progress", progress);
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
        return new ClawMachineScreenHandler(syncId, playerInventory, this, snuggleVaultBlockEntity, propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    public static void clientTick(World world, BlockPos blockPos, BlockState state, ClawMachineBlockEntity clawMachineBlockEntity) {

    }

    public static void serverTick(World world, BlockPos blockPos, BlockState state, ClawMachineBlockEntity clawMachineBlockEntity) {
        if (clawMachineBlockEntity.isActive()) {
            clawMachineBlockEntity.incrementProgress();
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void toggleActive() {
        active = !active;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public int incrementProgress() {
        if (progress < 162) {
            progress++;
        } else {
            progress = 0;
        }
        return progress;
    }
}
