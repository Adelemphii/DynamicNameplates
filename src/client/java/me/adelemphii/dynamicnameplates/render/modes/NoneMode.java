package me.adelemphii.dynamicnameplates.render.modes;

import me.adelemphii.dynamicnameplates.render.RenderModeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class NoneMode implements RenderModeHandler {
    @Override
    public boolean shouldRender(MinecraftClient client, Entity renderEntity, Vec3d toTarget, double distance) {
        return false;
    }
}
