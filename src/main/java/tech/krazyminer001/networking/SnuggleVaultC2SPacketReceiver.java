package tech.krazyminer001.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import tech.krazyminer001.block.gachamachine.GachaNetworkHandler;

public class SnuggleVaultC2SPacketReceiver {
    public static void registerC2SReceivers() {
        var gachaHandler = new GachaNetworkHandler();
        
        ServerPlayNetworking.registerGlobalReceiver(SnuggleVaultC2SPackets.GachaMachineSpinPacket.PACKET_ID, gachaHandler);
    }
}
