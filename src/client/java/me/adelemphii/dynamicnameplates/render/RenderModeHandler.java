package me.adelemphii.dynamicnameplates.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public interface RenderModeHandler {
    boolean shouldRender(MinecraftClient client, Entity renderEntity, Vec3d toTarget, double distance);
}
