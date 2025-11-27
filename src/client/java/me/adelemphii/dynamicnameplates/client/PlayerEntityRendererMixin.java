package me.adelemphii.dynamicnameplates.client;

import me.adelemphii.dynamicnameplates.DynamicNameplatesClient;
import me.adelemphii.dynamicnameplates.config.DNConfigModel;
import me.adelemphii.dynamicnameplates.config.NametagConfig;
import me.adelemphii.dynamicnameplates.render.RenderModeHandler;
import me.adelemphii.dynamicnameplates.render.modes.AreaMode;
import me.adelemphii.dynamicnameplates.render.modes.CrosshairMode;
import me.adelemphii.dynamicnameplates.render.modes.CubeMode;
import me.adelemphii.dynamicnameplates.render.modes.NoneMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Cancel nameplate rendering unless the player being rendered is the entity the client is currently
 * looking at (crosshair target).
 */
@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

	@Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			at = @At("HEAD"), cancellable = true)
	private void onRenderLabelIfPresent(PlayerEntityRenderState state, Text text, MatrixStack matrices,
										VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		NametagConfig cfg = DynamicNameplatesClient.CONFIG;
		if (cfg.showNameplates() || cfg.holdNameplates()) return;

		MinecraftClient client = MinecraftClient.getInstance();
		if (client == null || client.world == null || client.player == null) return;

		Entity renderEntity = client.world.getEntityById(state.id);
		if (renderEntity == null) return;

		Vec3d cameraPos = client.player.getCameraPosVec(1.0F);
		Vec3d targetPos = new Vec3d(state.x, state.y + 1.62, state.z);
		Vec3d toTarget = targetPos.subtract(cameraPos);
		double distance = toTarget.length();

		if (distance > cfg.maxDistance()) {
			ci.cancel();
			return;
		}

		DNConfigModel.RenderMode mode = distance < cfg.modeSwapRange()
				? cfg.closeRenderMode()
				: cfg.farRenderMode();

		RenderModeHandler handler = switch (mode) {
			case CROSSHAIR -> new CrosshairMode();
			case CUBE -> new CubeMode();
			case AREA -> new AreaMode();
			default -> new NoneMode();
		};

		if (!handler.shouldRender(client, renderEntity, toTarget, distance)) {
			ci.cancel();
		}
	}
}