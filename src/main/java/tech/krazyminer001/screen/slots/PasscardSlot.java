package tech.krazyminer001.screen.slots;

import gay.lemmaeof.terrifictickets.TerrificTickets;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class PasscardSlot extends Slot {
    public PasscardSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() == TerrificTickets.PASSCARD;
    }
}
