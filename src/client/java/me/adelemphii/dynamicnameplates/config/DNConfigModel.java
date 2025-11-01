package me.adelemphii.dynamicnameplates.config;

import io.wispforest.owo.config.annotation.*;
import me.adelemphii.DynamicNameplates;

@Modmenu(modId = DynamicNameplates.MOD_ID)
@Config(name = "dynamic-nameplates-config", wrapperName = "NametagConfig")
public class DNConfigModel {

    public boolean showNameplates = false;
    @ExcludeFromScreen
    public boolean holdNameplates = false;

    public RenderMode closeRenderMode = RenderMode.CROSSHAIR;
    public RenderMode farRenderMode = RenderMode.CUBE;

    @RangeConstraint(min = 0, max = 128)
    public double modeSwapRange = 4;

    @RangeConstraint(min = 1, max = 128)
    public double maxDistance = 64.0;

    @Nest
    public CubeSettings cubeSettings = new CubeSettings();

    public static class CubeSettings {
        @RangeConstraint(min = 0, max = 10)
        public double baseHorizontalDeg = 2.0;

        @RangeConstraint(min = 0, max = 20)
        public double baseVerticalDeg = 10.0;

        @RangeConstraint(min = 0.0, max = 1.0)
        public double horizLogScale = 0.6;

        @RangeConstraint(min = 0.0, max = 1.0)
        public double vertLogScale = 0.15;
    }

    public enum RenderMode {
        CROSSHAIR, // Targets what the player is looking at; raycast at distance
        CUBE,      // Checks entities in a growing cube of tolerance around crosshair
        AREA       // Shows all nametags within distance X
    }
}
