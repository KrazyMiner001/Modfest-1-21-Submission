package tech.krazyminer001;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.krazyminer001.block.SnuggleVaultBlockEntities;
import tech.krazyminer001.block.SnuggleVaultBlocks;
import tech.krazyminer001.item.SnuggleVaultItemGroups;

public class SnuggleVault implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("snugglevault");
	public static final String MOD_ID = "snugglevault";

	@Override
	public void onInitialize() {
		SnuggleVaultBlocks.registerBlocks();
		SnuggleVaultBlockEntities.registerBlockEntities();
		SnuggleVaultItemGroups.registerItemGroups();
	}
}