package me.adelemphii.dynamicnameplates.render.modes;


import me.adelemphii.dynamicnameplates.DynamicNameplatesClient;
import me.adelemphii.dynamicnameplates.config.NametagConfig;
import me.adelemphii.dynamicnameplates.render.RenderModeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CubeMode implements RenderModeHandler {

    private double getHorizontalAngleRad(Vec3d look, Vec3d dir) {
        Vec3d lookHoriz = new Vec3d(look.x, 0.0, look.z);
        Vec3d targetHoriz = new Vec3d(dir.x, 0.0, dir.z);
        double lh = lookHoriz.length();
        double th = targetHoriz.length();

        if (lh < 1e-6 || th < 1e-6) return 0.0;

        double hDot = lookHoriz.dotProduct(targetHoriz) / (lh * th);
        hDot = MathHelper.clamp(hDot, -1.0, 1.0);
        return Math.acos(hDot);
    }

    @Override
    public boolean shouldRender(MinecraftClient client, Entity renderEntity, Vec3d toTarget, double distance) {
        Vec3d dir = toTarget.normalize();
        Vec3d look = client.player.getRotationVec(1.0F).normalize();

        double horizontalAngleRad = getHorizontalAngleRad(look, dir);

        double lookPitch = Math.asin(MathHelper.clamp(look.y, -1.0, 1.0));
        double targetPitch = Math.asin(MathHelper.clamp(dir.y, -1.0, 1.0));
        double verticalAngleRad = Math.abs(lookPitch - targetPitch);

        NametagConfig cfg = DynamicNameplatesClient.CONFIG;
        double horizScale = Math.log1p(distance) * cfg.cubeSettings.horizLogScale();
        double vertScale  = Math.log1p(distance) * cfg.cubeSettings.vertLogScale();

        double maxHorizRad = Math.toRadians(cfg.cubeSettings.baseHorizontalDeg() + horizScale);
        double maxVertRad  = Math.toRadians(cfg.cubeSettings.baseVerticalDeg() + vertScale);

        return horizontalAngleRad <= maxHorizRad && verticalAngleRad <= maxVertRad;
    }
}
