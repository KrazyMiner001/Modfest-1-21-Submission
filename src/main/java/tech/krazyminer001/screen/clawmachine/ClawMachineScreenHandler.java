package tech.krazyminer001.screen.clawmachine;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import tech.krazyminer001.screen.SnuggleVaultScreenHandlers;
import tech.krazyminer001.screen.slots.PasscardSlot;

public class ClawMachineScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    // This constructor gets called on the client when the server wants it to open the Claw Machine Screen handler.
    // The client will call the other constructor with an empty Inventory, 
    // and the screenHandler will automatically sync this empty inventory with the inventory on the server.
    public ClawMachineScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1));
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first.
    // The server knows the inventory of the container and can therefore directly provide it as an argument.
    // Said container's inventory will be synced to the client.
    public ClawMachineScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(SnuggleVaultScreenHandlers.CLAW_MACHINE_SCREEN_HANDLER, syncId);
        checkSize(inventory, 1);
        this.inventory = inventory;
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        // Place the slots in the correct locations for a 3x3 Grid.
        // The slots exist on both server and client!
        // However, this will not render the background of the slots.
        // This is the Screens job
        
        // Claw Machine's inventory
        // TODO: Actually draw inventory
        this.addSlot(new PasscardSlot(inventory, 0, 80, 35));

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
}
