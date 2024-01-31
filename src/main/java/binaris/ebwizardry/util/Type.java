package binaris.ebwizardry.util;

import binaris.ebwizardry.Wizardry;
import net.minecraft.util.Identifier;

public class Type {
    /**
     * 3D-rendered light-beam particle.<p></p><b>Defaults:</b><br>Lifetime: 1 tick<br> Colour: white
     */
    public static final Identifier BEAM = new Identifier(Wizardry.MODID, "beam");
    /**
     * Square block face highlight particle.<p></p><b>Defaults:</b><br>Lifetime: 160 ticks<br>Colour: white
     */
    public static final Identifier BLOCK_HIGHLIGHT = new Identifier(Wizardry.MODID, "block_highlight");
    /**
     * Helical animated 'buffing' particle.<p></p><b>Defaults:</b><br>Lifetime: 15 ticks
     * <br>Velocity: (0, 0.27, 0)<br>Colour: white
     */
    public static final Identifier BUFF = new Identifier(Wizardry.MODID, "buff");
    /**
     * Large, thick cloud.<p></p><b>Defaults:</b><br>Lifetime: 48-60 ticks<br> Colour: dark grey
     */
    public static final Identifier CLOUD = new Identifier(Wizardry.MODID, "cloud");
    /**
     * Spiral particle, like potions.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks<br>Colour: white
     */
    public static final Identifier DARK_MAGIC = new Identifier(Wizardry.MODID, "dark_magic");
    /**
     * Single pixel particle.<p></p><b>Defaults:</b><br>Lifetime: 16-80 ticks<br>Colour: white
     */
    public static final Identifier DUST = new Identifier(Wizardry.MODID, "dust");
    /**
     * Rapid flash, like fireworks.<p></p><b>Defaults:</b><br>Lifetime: 6 ticks<br>Colour: white
     */
    public static final Identifier FLASH = new Identifier(Wizardry.MODID, "flash");
    /**
     * Particle that looks like the guardian's beam attack.<p></p><b>Defaults:</b><br>Lifetime: 1 tick
     */
    public static final Identifier GUARDIAN_BEAM = new Identifier(Wizardry.MODID, "guardian_beam");
    /**
     * Small shard of ice.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks<br>Gravity: true
     */
    public static final Identifier ICE = new Identifier(Wizardry.MODID, "ice");
    /**
     * Single leaf.<p></p><b>Defaults:</b><br>Lifetime: 10-15 ticks<br>Velocity: (0, -0.03, 0)
     * <br>Colour: green/brown
     */
    public static final Identifier LEAF = new Identifier(Wizardry.MODID, "leaf");
    /**
     * 3D-rendered lightning particle.<p></p><b>Defaults:</b><br>Lifetime: 3 ticks<br> Colour: blue
     */
    public static final Identifier LIGHTNING = new Identifier(Wizardry.MODID, "lightning");
    /**
     * 2D lightning effect, normally on the ground.<p></p><b>Defaults:</b><br>Lifetime: 7 ticks
     * <br>Facing: up
     */
    public static final Identifier LIGHTNING_PULSE = new Identifier(Wizardry.MODID, "lightning_pulse");
    /**
     * Bubble that doesn't burst in air.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks
     */
    public static final Identifier MAGIC_BUBBLE = new Identifier(Wizardry.MODID, "magic_bubble");
    /**
     * Animated flame.<p></p><b>Defaults:</b><br>Lifetime: 12-16 ticks<br>
     */
    public static final Identifier MAGIC_FIRE = new Identifier(Wizardry.MODID, "magic_fire");
    /**
     * Soft-edged round particle.<p></p><b>Defaults:</b><br>Lifetime: 8-40 ticks<br>Colour: white
     */
    public static final Identifier PATH = new Identifier(Wizardry.MODID, "path");
    /**
     * Scorch mark.<p></p><b>Defaults:</b><br>Lifetime: 100-140 ticks<br>Colour: black<br>Fade: black
     */
    public static final Identifier SCORCH = new Identifier(Wizardry.MODID, "scorch");
    /**
     * Snowflake particle.<p></p><b>Defaults:</b><br>Lifetime: 40-50 ticks<br>Velocity: (0, -0.02, 0)
     */
    public static final Identifier SNOW = new Identifier(Wizardry.MODID, "snow");
    /**
     * Animated lightning particle.<p></p><b>Defaults:</b><br>Lifetime: 3 ticks
     */
    public static final Identifier SPARK = new Identifier(Wizardry.MODID, "spark");
    /**
     * Animated sparkle particle.<p></p><b>Defaults:</b><br>Lifetime: 48-60 ticks<br>Colour: white
     */
    public static final Identifier SPARKLE = new Identifier(Wizardry.MODID, "sparkle");
    /**
     * 3D-rendered expanding sphere.<p></p><b>Defaults:</b><br>Lifetime: 6 ticks<br>Colour: white
     */
    public static final Identifier SPHERE = new Identifier(Wizardry.MODID, "sphere");
    /**
     * 3D-rendered vine particle.<p></p><b>Defaults:</b><br>Lifetime: 1 tick<br> Colour: green
     */
    public static final Identifier VINE = new Identifier(Wizardry.MODID, "vine");
}
