package binaris.ebwizardry.util;

import net.minecraft.util.math.MathHelper;

public final class DrawingUtils {

    /**
     * Mixes the two given opaque colours in the proportion specified.
     * @param colour1 The first colour to mix, as a 6-digit hexadecimal.
     * @param colour2 The second colour to mix, as a 6-digit hexadecimal.
     * @param proportion The proportion of the second colour; will be clamped to between 0 and 1.
     * @return The resulting colour, as a 6-digit hexadecimal.
     */
    public static int mix(int colour1, int colour2, float proportion){

        proportion = MathHelper.clamp(proportion, 0, 1);

        int r1 = colour1 >> 16 & 255;
        int g1 = colour1 >> 8 & 255;
        int b1 = colour1 & 255;
        int r2 = colour2 >> 16 & 255;
        int g2 = colour2 >> 8 & 255;
        int b2 = colour2 & 255;

        int r = (int)(r1 + (r2-r1) * proportion);
        int g = (int)(g1 + (g2-g1) * proportion);
        int b = (int)(b1 + (b2-b1) * proportion);

        return (r << 16) + (g << 8) + b;
    }
}
