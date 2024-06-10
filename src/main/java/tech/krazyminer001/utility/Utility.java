package tech.krazyminer001.utility;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import tech.krazyminer001.SnuggleVault;

public class Utility {
    public static Identifier of(String path) {
        return Identifier.of(SnuggleVault.MOD_ID, path);
    }

    public static VoxelShape rotateShape(VoxelShape shape, Direction direction) {
        Box box = shape.getBoundingBox();
        Vec3d center = new Vec3d(0.5, 0.5, 0.5);
        Vec3d min = box.getMinPos().subtract(center);
        Vec3d max = box.getMaxPos().subtract(center);

        switch (direction) {
            case EAST: // Rotate 90 degrees
                min = new Vec3d(-min.z, min.y, min.x);
                max = new Vec3d(-max.z, max.y, max.x);
                break;
            case SOUTH: // Rotate 180 degrees
                min = new Vec3d(-min.x, min.y, -min.z);
                max = new Vec3d(-max.x, max.y, -max.z);
                break;
            case WEST: // Rotate 270 degrees
                min = new Vec3d(min.z, min.y, -min.x);
                max = new Vec3d(max.z, max.y, -max.x);
                break;
            default: // No rotation
                break;
        }

        // Ensure min is less than max
        Vec3d tempMin = new Vec3d(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z));
        Vec3d tempMax = new Vec3d(Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z));

        min = tempMin.add(center);
        max = tempMax.add(center);

        return VoxelShapes.cuboid(min.x, min.y, min.z, max.x, max.y, max.z);
    }
}
