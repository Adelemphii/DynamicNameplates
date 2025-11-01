package me.adelemphii.dynamicnameplates.render.modes;

import me.adelemphii.dynamicnameplates.DynamicNameplatesClient;
import me.adelemphii.dynamicnameplates.render.RenderModeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class CrosshairMode implements RenderModeHandler {

    @Override
    public boolean shouldRender(MinecraftClient client, Entity renderEntity, Vec3d toTarget, double distance) {
        HitResult hit = client.crosshairTarget;
        if (hit instanceof EntityHitResult ehr && ehr.getEntity() == renderEntity) {
            return true;
        }

        double reach = Math.min(distance, DynamicNameplatesClient.CONFIG.maxDistance());
        Vec3d start = client.player.getCameraPosVec(1.0F);
        Vec3d look = client.player.getRotationVec(1.0F);

        EntityHitResult entityHit = ProjectileUtil.raycast(
                client.player,
                start,
                start.add(look.multiply(reach)),
                client.player.getBoundingBox().stretch(look.multiply(reach)).expand(1.0),
                e -> !e.isSpectator() && e.canHit(),
                reach * reach
        );

        return entityHit != null && entityHit.getEntity() == renderEntity;
    }
}
