package tech.krazyminer001.screen.gachamachine;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.joml.Vector2i;
import tech.krazyminer001.api.ImplementedInventory;
import tech.krazyminer001.block.snugglevault.SnuggleVaultBlockEntity;
import tech.krazyminer001.screen.SnuggleVaultScreenHandlers;
import tech.krazyminer001.screen.slots.DisplaySlot;
import tech.krazyminer001.screen.slots.PasscardSlot;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GachaMachineScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final SnuggleVaultBlockEntity vault;
    private final Set<Integer> possibleIndices;

    // This constructor gets called on the client when the server wants it to open the Gacha Screen handler.
    // The client will call the other constructor with an empty Inventory, 
    // and the screenHandler will automatically sync this empty inventory with the inventory on the server.
    public GachaMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1), null);
    }

    public GachaMachineScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        this(syncId, playerInventory, inventory, null);
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first.
    // The server knows the inventory of the container and can therefore directly provide it as an argument.
    // Said container's inventory will be synced to the client.
    public GachaMachineScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, SnuggleVaultBlockEntity vault) {
        super(SnuggleVaultScreenHandlers.GACHA_MACHINE_SCREEN_HANDLER, syncId);
        DefaultedList<ItemStack> vaultInventoryItems;
        if (vault == null) {
            vaultInventoryItems = DefaultedList.ofSize(27, ItemStack.EMPTY);
        } else {
            DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);
            vault.markDirty();
            DefaultedList<ItemStack> tempItems = DefaultedList.of();
            tempItems.addAll(vault.getItems());
            tempItems.removeFirst();
            AtomicInteger i = new AtomicInteger();
            tempItems.reversed().forEach(itemStack -> {
                if (itemStack.isEmpty()) return;
                items.set(i.getAndIncrement(), itemStack);
            });
            vaultInventoryItems = items;
        }
        checkSize(inventory, 1);
        this.inventory = inventory;
        this.vault = vault;
        ImplementedInventory vaultInventory = ImplementedInventory.of(vaultInventoryItems);
        checkSize(vaultInventory, 27);
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        // Place the slots in the correct locations for a 3x3 Grid.
        // The slots exist on both server and client!
        // However, this will not render the background of the slots.
        // This is the Screens job

        // Gacha machine inventory
        this.addSlot(new PasscardSlot(inventory, 0, 180, 53));

        Random random = new Random(vaultInventoryItems.hashCode() + playerInventory.hashCode() + this.hashCode() + (vault == null ? 0 : Objects.requireNonNull(Objects.requireNonNull(vault).getWorld()).getTime()));
        Set<Integer> indices = new HashSet<>();
        int ballNum = 6;
        while (indices.size() < ballNum) {
            indices.add(random.nextInt(27));
        }
        Set<Vector2i> positionSet = new HashSet<>();
        while (positionSet.size() < ballNum) {
            positionSet.add(new Vector2i(13 + 22 * (random.nextInt(7)), 22 + 22 * (random.nextInt(2))));
        }
        List<Vector2i> positions = new ArrayList<>(positionSet);

        indices.forEach(i -> {
            Vector2i position = positions.removeFirst();
            this.addSlot(new DisplaySlot(vaultInventory, i, position.x, position.y));
        });
        this.possibleIndices = indices;

        // The player's inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // ... And the player's Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    public SnuggleVaultBlockEntity getVault() {
        return vault;
    }

    public Set<Integer> getPossibleIndices() {
        return possibleIndices;
    }
}


