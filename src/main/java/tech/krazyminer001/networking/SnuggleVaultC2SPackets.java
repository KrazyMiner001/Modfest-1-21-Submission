package tech.krazyminer001.networking;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;

import java.util.UUID;

import static tech.krazyminer001.utility.Utility.of;

public class SnuggleVaultC2SPackets {
    public record GachaMachineSpinPacket() implements CustomPayload {
        public static final CustomPayload.Id<GachaMachineSpinPacket> PACKET_ID = new CustomPayload.Id<>(of("gacha_machine_spin"));
        public static final PacketCodec<RegistryByteBuf, GachaMachineSpinPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {}, buf -> new GachaMachineSpinPacket());

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }



    }
}
