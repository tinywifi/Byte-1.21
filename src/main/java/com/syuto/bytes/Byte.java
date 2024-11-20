package com.syuto.bytes;

import com.syuto.bytes.commands.CommandManager;
import com.syuto.bytes.eventbus.EventBus;
import com.syuto.bytes.eventbus.EventListener;
import com.syuto.bytes.modules.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Byte implements ModInitializer {
	public static final String MOD_ID = "byte";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Byte INSTANCE = new Byte();
	public EventBus eventBus;
	public static MinecraftClient mc;
	public CommandManager commandManager;

	public Byte() {
		eventBus = new EventBus();
		commandManager = new CommandManager();
	}


	@Override
	public void onInitialize() {
		eventBus.register(new EventListener());
		ModuleManager.registerModules();
		mc = MinecraftClient.getInstance();
		LOGGER.info("Byte load");
	}
}