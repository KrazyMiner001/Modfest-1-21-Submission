package tech.krazyminer001.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class SnuggleVaultC2SPacketSender {
    public static void sendGachaMachineSpinPacket() {
        ClientPlayNetworking.send(new SnuggleVaultC2SPackets.GachaMachineSpinPacket());
    }

    public static void sendClawMachineStartPacket() {
        ClientPlayNetworking.send(new SnuggleVaultC2SPackets.ClawMachineStartPacket());
    }

    public static void sendClawMachineEndPacket() {
        ClientPlayNetworking.send(new SnuggleVaultC2SPackets.ClawMachineEndPacket());
    }
}
