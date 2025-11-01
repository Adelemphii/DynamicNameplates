package me.adelemphii.dynamicnameplates;

import me.adelemphii.dynamicnameplates.config.NametagConfig;
import me.adelemphii.dynamicnameplates.events.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;

public class DynamicNameplatesClient implements ClientModInitializer {

	public static final NametagConfig CONFIG = NametagConfig.createAndLoad();

	@Override
	public void onInitializeClient() {
		KeyInputHandler.register();
	}
}