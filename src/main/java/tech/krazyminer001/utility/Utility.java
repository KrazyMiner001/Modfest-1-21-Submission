package tech.krazyminer001.utility;

import net.minecraft.util.Identifier;
import tech.krazyminer001.SnuggleVault;

public class Utility {
    public static Identifier of(String path) {
        return Identifier.of(SnuggleVault.MOD_ID, path);
    }
}
