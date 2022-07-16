package io.github.KacyBiscuit.insomania;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsoMania implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("InsoMania");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Loading {}!", mod.metadata().name());
	}
}
