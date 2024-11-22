package com.syuto.bytes;

import com.syuto.bytes.commands.CommandManager;
import com.syuto.bytes.eventbus.EventBus;
import com.syuto.bytes.eventbus.Handlers;
import com.syuto.bytes.module.ModuleManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Byte implements ModInitializer {

	public static final String MOD_ID = "byte";
	public static final String NAME = "Byte", VERSION = "1.0.0"; // https://semver.org
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

	public static Byte INSTANCE = new Byte();
	public EventBus eventBus;
	public static MinecraftClient mc = MinecraftClient.getInstance();
	public CommandManager commandManager;
	public Handlers handlers;

	public Byte() {
		eventBus = new EventBus();
		commandManager = new CommandManager();
		handlers = new Handlers();
	}

	@Override
	public void onInitialize() {
		final long time = System.currentTimeMillis();
		ModuleManager.registerModules();
		eventBus.register(handlers);
        LOGGER.info("Initialized {} in {}ms.", NAME, System.currentTimeMillis() - time);
	}
}