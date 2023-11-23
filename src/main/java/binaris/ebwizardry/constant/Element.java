package binaris.ebwizardry.constant;

import binaris.ebwizardry.Wizardry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

public enum Element implements StringIdentifiable {

    MAGIC(Formatting.GRAY, "magic"),
    FIRE(Formatting.DARK_RED, "fire"),
    ICE(Formatting.AQUA, "ice"),
    LIGHTNING(Formatting.DARK_AQUA, "lightning"),
    NECROMANCY(Formatting.DARK_PURPLE, "necromancy"),
    EARTH(Formatting.DARK_GREEN, "earth"),
    SORCERY(Formatting.GREEN, "sorcery"),
    HEALING(Formatting.YELLOW, "healing");

    Element(Formatting colour, String name){
        this(colour, name, Wizardry.MODID);
    }

    Element(Formatting colour, String name, String modid){
        this.colour = colour;
        this.unlocalisedName = name;
        this.icon = new Identifier(modid, "textures/gui/container/element_icon_" + unlocalisedName + ".png");
    }

    /** Display colour for this element */
    private final Formatting colour;
    /** Unlocalised name for this element */
    private final String unlocalisedName;
    /** The {@link net.minecraft.util.Identifier} for this element's 8x8 icon (displayed in the arcane workbench GUI) */
    private final Identifier icon;
    ;

    /** Returns the element with the given name, or throws an {@link java.lang.IllegalArgumentException} if no such
     * element exists. */
    public static Element fromName(String name){

        for(Element element : values()){
            if(element.unlocalisedName.equals(name)) return element;
        }

        throw new IllegalArgumentException("No such element with unlocalised name: " + name);
    }

    /** Same as {@link Element#fromName(String)}, but returns the given fallback instead of throwing an exception if no
     * element matches the given string. */
    @Nullable
    public static Element fromName(String name, @Nullable Element fallback){

        for(Element element : values()){
            if(element.unlocalisedName.equals(name)) return element;
        }

        return fallback;
    }

    /** Returns the translated display name of this element, without formatting. */
    //public String getDisplayName(){
    //    return Wizardry.proxy.translate("element." + getName());
    //}

    /** Returns the {@link Formatting} object representing the colour of this element. */
    public Formatting getColour(){
        return colour;
    }

    /** Returns the translated display name for wizards of this element, shown in the trading GUI. */
    public Text getWizardName(){
        return Text.translatable("element." + asString() + ".wizard");
    }

    /** Returns this element's unlocalised name. Also used as the serialised string in block properties. */
    @Override
    public String asString(){
        return unlocalisedName;
    }

    /** Returns the {@link Identifier} for this element's 8x8 icon (displayed in the arcane workbench GUI). */
    public Identifier getIcon(){
        return icon;
    }
}
