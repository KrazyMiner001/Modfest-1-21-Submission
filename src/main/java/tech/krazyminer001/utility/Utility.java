package tech.krazyminer001.utility;

import net.minecraft.util.Identifier;
import tech.krazyminer001.Modfest121;

public class Utility {
    public static Identifier of(String path) {
        return Identifier.of(Modfest121.MOD_ID, path);
    }
}
