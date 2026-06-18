package net.autoclicky;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Hand;
import java.util.Random;

public class AutoClickyMod implements ClientModInitializer {
    private static KeyBinding toggleRightClick;
    private final Random random = new Random();
    private long lastRightClickTime = 0;

    @Override
    public void onInitializeClient() {
        toggleRightClick = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Строительство (Зажим ПКМ)", InputUtil.Type.KEYSYM, -1, "BedWars"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.interactionManager == null) return;

            if (toggleRightClick.isPressed()) {
                long currentTime = System.currentTimeMillis();
                long nextClickDelay = 64 + random.nextInt(14); 
                if (currentTime - lastRightClickTime >= nextClickDelay) {
                    client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, client.crosshairTarget);
                    client.player.swingHand(Hand.MAIN_HAND);
                    lastRightClickTime = currentTime;
                }
            }
        });
    }
}
