package tech.krazyminer001.datagen.lang;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import tech.krazyminer001.block.SnuggleVaultBlocks;
import tech.krazyminer001.item.SnuggleVaultItems;

import java.util.concurrent.CompletableFuture;

public class SnuggleVaultEnglishUSProvider extends FabricLanguageProvider {
    public SnuggleVaultEnglishUSProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(SnuggleVaultBlocks.SNUGGLE_VAULT, "Snuggle Vault");
        translationBuilder.add(SnuggleVaultBlocks.CREATIVE_SNUGGLE_VAULT, "Creative Snuggle Vault");
        translationBuilder.add(SnuggleVaultBlocks.CLAW_MACHINE, "Claw Machine");
        translationBuilder.add(SnuggleVaultBlocks.GACHA_MACHINE, "Gacha Machine");

        translationBuilder.add(SnuggleVaultItems.UNLOCKINATOR, "Unlockinator");

        translationBuilder.add("block.snugglevault.snugglevault.not_owned", "You do not own this Snuggle Vault.");
        translationBuilder.add("item_group.snugglevault.snuggle_vault", "Snuggle Vault");
    }
}
