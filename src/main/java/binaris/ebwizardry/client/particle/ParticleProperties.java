package binaris.ebwizardry.client.particle;

// this is a good way to store the properties? idk but it works for me
/**
 * This class is used to store the properties of a particle
 * To set the properties into the particle factory, basically
 * this is just getters and setters.
 * */
public class ParticleProperties {
    double velocityX;
    double velocityY;
    double velocityZ;
    float red;
    float green;
    float blue;
    float scale;
    int maxAge;
    double x;
    double y;
    double z;
    boolean shaded;
    // ------------------------- Getters -------------------------------- //
    public double getVelocityX() {
        return velocityX;
    }
    public double getVelocityY() {
        return velocityY;
    }
    public double getVelocityZ() {
        return velocityZ;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getScale() {
        return scale;
    }

    public int getMaxAge() {
        return maxAge;
    }
    public boolean getShaded() {
        return shaded;
    }

    // ------------------------- Setters -------------------------------- //
    public void setVelocity(double velocityX, double velocityY, double velocityZ) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }
    public void setColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public void setScale(float scale) {
        this.scale = scale;
    }
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void setShaded(boolean shaded) {
        this.shaded = shaded;
    }
}
