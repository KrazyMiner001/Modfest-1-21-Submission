package tech.krazyminer001;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.krazyminer001.block.SnuggleVaultBlockEntities;
import tech.krazyminer001.block.SnuggleVaultBlocks;
import tech.krazyminer001.item.SnuggleVaultItemGroups;
import tech.krazyminer001.item.SnuggleVaultItems;
import tech.krazyminer001.networking.SnuggleVaultC2SPacketReceiver;
import tech.krazyminer001.networking.SnuggleVaultC2SPackets;
import tech.krazyminer001.screen.SnuggleVaultScreenHandlers;

public class SnuggleVault implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("snugglevault");
	public static final String MOD_ID = "snugglevault";

	@Override
	public void onInitialize() {
		SnuggleVaultBlocks.registerBlocks();
		SnuggleVaultBlockEntities.registerBlockEntities();
		SnuggleVaultItemGroups.registerItemGroups();
		SnuggleVaultScreenHandlers.registerScreenHandlers();
		SnuggleVaultItems.registerItems();

		PayloadTypeRegistry.playC2S().register(SnuggleVaultC2SPackets.GachaMachineSpinPacket.PACKET_ID, SnuggleVaultC2SPackets.GachaMachineSpinPacket.PACKET_CODEC);
		SnuggleVaultC2SPacketReceiver.registerC2SReceivers();
	}
}