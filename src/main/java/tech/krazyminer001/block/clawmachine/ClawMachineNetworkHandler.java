package tech.krazyminer001.block.clawmachine;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tech.krazyminer001.block.SnuggleVaultBlockEntities;
import tech.krazyminer001.block.snugglevault.SnuggleVaultBlockEntity;
import tech.krazyminer001.networking.SnuggleVaultC2SPackets;
import tech.krazyminer001.screen.clawmachine.ClawMachineScreenHandler;
import tech.krazyminer001.screen.slots.DisplaySlot;

import java.util.*;

public class ClawMachineNetworkHandler {
    public static class ClawMachineStartHandler implements ServerPlayNetworking.PlayPayloadHandler<SnuggleVaultC2SPackets.ClawMachineStartPacket> {
        @Override
        public void receive(SnuggleVaultC2SPackets.ClawMachineStartPacket payload, ServerPlayNetworking.Context context) {
            SnuggleVaultBlockEntity snuggleVaultBlockEntity = ((ClawMachineScreenHandler) context.player().currentScreenHandler).getVault();
            World world = snuggleVaultBlockEntity.getWorld();
            BlockPos pos = snuggleVaultBlockEntity.getPos().up();
            assert world != null;
            ClawMachineBlockEntity clawMachineBlockEntity = world.getBlockEntity(pos, SnuggleVaultBlockEntities.CLAW_MACHINE).orElse(null);

            if (clawMachineBlockEntity != null) {
                clawMachineBlockEntity.setActive(true);
            }

        }
    }

    public static class ClawMachineEndPacket implements ServerPlayNetworking.PlayPayloadHandler<SnuggleVaultC2SPackets.ClawMachineEndPacket> {
        @Override
        public void receive(SnuggleVaultC2SPackets.ClawMachineEndPacket payload, ServerPlayNetworking.Context context) {
            ClawMachineScreenHandler screenHandler = (ClawMachineScreenHandler) context.player().currentScreenHandler;

            SnuggleVaultBlockEntity snuggleVaultBlockEntity = screenHandler.getVault();
            World world = snuggleVaultBlockEntity.getWorld();
            BlockPos pos = snuggleVaultBlockEntity.getPos().up();
            assert world != null;
            ClawMachineBlockEntity clawMachineBlockEntity = world.getBlockEntity(pos, SnuggleVaultBlockEntities.CLAW_MACHINE).orElse(null);

            if (clawMachineBlockEntity != null) {
                ArrayList<Slot> slots = screenHandler.slots.stream().filter(slot -> slot instanceof DisplaySlot).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                HashMap<Slot, Integer> slotMap = new HashMap<>();
                slots.forEach(slot -> {
                    int x = slot.x + 11;

                    slotMap.put(slot, screenHandler.getProgress() - x);
                });

                Optional<Slot> closestSlot = slotMap.keySet()
                        .stream()
                        .filter(slot -> Objects.equals(slotMap.get(slot), Collections.min(slotMap.values())))
                        .findFirst();

                closestSlot.ifPresent(slot -> {
                    if (Math.abs(Collections.min(slotMap.values())) < 5) {
                        snuggleVaultBlockEntity.dispenseItem(slot.id);
                    }
                });
            }
        }
    }
}
