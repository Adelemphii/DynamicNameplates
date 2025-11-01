package me.adelemphii.dynamicnameplates.events;

import me.adelemphii.DynamicNameplates;
import me.adelemphii.dynamicnameplates.DynamicNameplatesClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    public static final String KEY_CATEGORY = "key.category.%s".formatted(DynamicNameplates.MOD_ID);
    public static final String KEY_TOGGLE_NAMEPLATES = "key.%s.toggle_nameplates".formatted(DynamicNameplates.MOD_ID);
    public static final String KEY_SHOW_NAMEPLATES = "key.%s.show_nameplates".formatted(DynamicNameplates.MOD_ID);

    public static KeyBinding toggleNameplatesKey;
    public static KeyBinding showNameplatesKey;

    private static boolean wasHoldingShowKey = false;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.player == null) return;
            if(toggleNameplatesKey.wasPressed()) {
                DynamicNameplatesClient.CONFIG.showNameplates(!DynamicNameplatesClient.CONFIG.showNameplates());
                client.player.sendMessage(Text.translatable("text.%s.toggled_nameplates".formatted(DynamicNameplates.MOD_ID))
                                .formatted(Formatting.AQUA)
                        , false);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            boolean configEnabled = DynamicNameplatesClient.CONFIG.showNameplates();
            boolean isPressed = showNameplatesKey.isPressed();

            // If nameplates are already enabled via config, ignore the key entirely
            if (configEnabled) {
                DynamicNameplatesClient.CONFIG.holdNameplates(false);
                wasHoldingShowKey = false;
                return;
            }

            if (isPressed && !wasHoldingShowKey) {
                DynamicNameplatesClient.CONFIG.holdNameplates(true);
                wasHoldingShowKey = true;
                return;
            }

            if (!isPressed && wasHoldingShowKey) {
                DynamicNameplatesClient.CONFIG.holdNameplates(false);
                wasHoldingShowKey = false;
            }
        });
    }

    public static void register() {
        toggleNameplatesKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_TOGGLE_NAMEPLATES,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KEY_CATEGORY
        ));
        showNameplatesKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SHOW_NAMEPLATES,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                KEY_CATEGORY
        ));

        registerKeyInputs();
    }
}
