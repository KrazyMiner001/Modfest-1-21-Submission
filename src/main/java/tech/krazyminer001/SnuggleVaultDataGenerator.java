package tech.krazyminer001;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import tech.krazyminer001.datagen.SnuggleVaultModelProvider;
import tech.krazyminer001.datagen.lang.SnuggleVaultEnglishUSProvider;

public class SnuggleVaultDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(SnuggleVaultModelProvider::new);
        pack.addProvider(SnuggleVaultEnglishUSProvider::new);
    }
}
