package tech.krazyminer001.networking;

import gay.lemmaeof.terrifictickets.api.TerrificTicketsApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import tech.krazyminer001.block.SnuggleVaultBlockEntities;
import tech.krazyminer001.block.SnuggleVaultBlocks;
import tech.krazyminer001.block.gachamachine.GachaMachineBlockEntity;
import tech.krazyminer001.block.snugglevault.SnuggleVaultBlockEntity;
import tech.krazyminer001.screen.gachamachine.GachaMachineScreenHandler;

import java.util.OptionalInt;

public class SnuggleVaultC2SPacketReceiver {
    public static void registerC2SReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(SnuggleVaultC2SPackets.GachaMachineSpinPacket.PACKET_ID, ((payload, context) -> {
            var player = context.player();
            var screen = player.currentScreenHandler;
            if (!(screen instanceof GachaMachineScreenHandler gachaMachineScreenHandler)) {
                return;
            }
            int index = gachaMachineScreenHandler.getPossibleIndices().stream().toList().get((int) (Math.random() * gachaMachineScreenHandler.getPossibleIndices().size()));
            SnuggleVaultBlockEntity snuggleVaultBlockEntity = gachaMachineScreenHandler.getVault();
            World world = snuggleVaultBlockEntity.getWorld();
            BlockPos pos = snuggleVaultBlockEntity.getPos().up();
            if (world != null) {
                NamedScreenHandlerFactory screenHandlerFactory = world.getBlockState(pos).createScreenHandlerFactory(world, pos);
                if (screenHandlerFactory != null) {
                    //With this call the server will request the client to open the appropriate Screenhandler
                    player.openHandledScreen(screenHandlerFactory);
                }
                if (world.getBlockEntity(pos) instanceof GachaMachineBlockEntity gachaMachineBlockEntity) {
                    ItemStack gachaMachinePasscard = gachaMachineBlockEntity.getStack(0);
                    int removedTokens = TerrificTicketsApi.removeTokens(gachaMachinePasscard, 1);
                    gachaMachineBlockEntity.setStack(0, gachaMachinePasscard);
                    if (removedTokens != 0) {
                        snuggleVaultBlockEntity.dispenseItem(index);
                        ItemStack snuggleVaultPasscard = snuggleVaultBlockEntity.getStack(0);
                        TerrificTicketsApi.removeTokens(snuggleVaultPasscard ,-removedTokens);
                        snuggleVaultBlockEntity.setStack(0, snuggleVaultPasscard);
                    }
                }
            }
        }));
    }
}
