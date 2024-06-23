package tech.krazyminer001.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import tech.krazyminer001.block.clawmachine.ClawMachineNetworkHandler;
import tech.krazyminer001.block.gachamachine.GachaNetworkHandler;

public class SnuggleVaultC2SPacketReceiver {
    public static void registerC2SReceivers() {
        var gachaHandler = new GachaNetworkHandler();
        var clawMachineStartHandler = new ClawMachineNetworkHandler.ClawMachineStartHandler();
        var clawMachineEndHandler = new ClawMachineNetworkHandler.ClawMachineEndPacket();

        ServerPlayNetworking.registerGlobalReceiver(SnuggleVaultC2SPackets.GachaMachineSpinPacket.PACKET_ID, gachaHandler);

        ServerPlayNetworking.registerGlobalReceiver(SnuggleVaultC2SPackets.ClawMachineStartPacket.PACKET_ID, clawMachineStartHandler);
        ServerPlayNetworking.registerGlobalReceiver(SnuggleVaultC2SPackets.ClawMachineEndPacket.PACKET_ID, clawMachineEndHandler);
    }
}
