package tech.krazyminer001.block.gachamachine;

import gay.lemmaeof.terrifictickets.api.TerrificTicketsApi;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tech.krazyminer001.block.snugglevault.SnuggleVaultBlockEntity;
import tech.krazyminer001.networking.SnuggleVaultC2SPackets;
import tech.krazyminer001.screen.gachamachine.GachaMachineScreenHandler;

import java.util.concurrent.ThreadLocalRandom;

public class GachaNetworkHandler implements ServerPlayNetworking.PlayPayloadHandler<SnuggleVaultC2SPackets.GachaMachineSpinPacket> {
    @Override
    public void receive(SnuggleVaultC2SPackets.GachaMachineSpinPacket payload, ServerPlayNetworking.Context context) {
        var player = context.player();
        var screen = player.currentScreenHandler;

        if (!(screen instanceof GachaMachineScreenHandler gachaMachineScreenHandler)) {
            return;
        }

        var possibleIndices = gachaMachineScreenHandler.getPossibleIndices();
        int index = possibleIndices.stream()
                .skip(ThreadLocalRandom.current().nextInt(possibleIndices.size()))
                .findAny()
                .orElseThrow();

        SnuggleVaultBlockEntity snuggleVaultBlockEntity = gachaMachineScreenHandler.getVault();
        World world = snuggleVaultBlockEntity.getWorld();
        BlockPos pos = snuggleVaultBlockEntity.getPos().up();
        if (world != null) {
            NamedScreenHandlerFactory screenHandlerFactory = world.getBlockState(pos).createScreenHandlerFactory(world, pos);
            if (screenHandlerFactory != null) {
                // The server will request the client to open the Gacha Machine's screen.
                player.openHandledScreen(screenHandlerFactory);
            }
            if (world.getBlockEntity(pos) instanceof GachaMachineBlockEntity gachaMachineBlockEntity) {
                ItemStack gachaMachinePasscard = gachaMachineBlockEntity.getStack(0);
                int removedTokens = TerrificTicketsApi.removeTokens(gachaMachinePasscard, 1);
                gachaMachineBlockEntity.setStack(0, gachaMachinePasscard);

                if (removedTokens != 0) {
                    snuggleVaultBlockEntity.dispenseItem(index);
                    ItemStack snuggleVaultPasscard = snuggleVaultBlockEntity.getStack(0);
                    TerrificTicketsApi.removeTokens(snuggleVaultPasscard, -removedTokens);
                    snuggleVaultBlockEntity.setStack(0, snuggleVaultPasscard);
                }
            }
        }
    }
}
