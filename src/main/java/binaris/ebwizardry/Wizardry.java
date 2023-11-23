package binaris.ebwizardry;

import binaris.ebwizardry.registry.*;
import binaris.ebwizardry.spell.Spell;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.block.Block;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Wizardry implements ModInitializer {
	/** Wizardry's mod ID. */
	public static final String MODID = "ebwizardry";
	/** Wizardry's mod name, in readable form. */
	public static final String NAME = "Electroblob's Wizardry";
	public static SimpleRegistry<Spell> REGISTRIES_SPELL = FabricRegistryBuilder.createSimple(Spell.class, new Identifier(Wizardry.MODID, "spell"))
			.attribute(RegistryAttribute.SYNCED)
			.buildAndRegister();

	public static final TagKey<Block> EXAMPLE_ORES = TagKey.of(RegistryKeys.BLOCK, new Identifier("tutorial", "example_ores"));


    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final Random random = new Random();

	@Override
	public void onInitialize() {
		LOGGER.info("Electroblob's Wizardry!");


		WizardryItems.register();
		WizardryBlocks.register();
		Spells.registry();
		WizardryTabs.use();
		WizardrySounds.register();


	}

}