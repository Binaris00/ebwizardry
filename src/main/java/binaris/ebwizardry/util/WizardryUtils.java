package binaris.ebwizardry.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;

public class WizardryUtils {
    public static boolean isFirstPerson(Entity entity) {
        return entity == MinecraftClient.getInstance().getCameraEntity() && MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON;
    }
}
