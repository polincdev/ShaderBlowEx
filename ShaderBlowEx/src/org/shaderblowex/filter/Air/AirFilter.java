package org.shaderblowex.filter.Air;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

/**
 * A filter to render a atmosphere effect
 * @author Rémy Bouquet aka Nehon
 */
public class AirFilter extends Filter {

    private ColorRGBA airColor = new ColorRGBA(0.8f,0.8f,1.0f,1f);
    private float airDensity = 0.4f;
    private float airDistance = 10;
  private float airDesaturation = 1f;

    /**
     * Creates a AirFilter
     */
    public AirFilter() {
        super("AirFilter");
    }

    /**
     * Create a air filter 
     * @param airColor the color of the air (default is white)
     * @param airDensity the density of the air (default is 0.7)
     * @param airDistance the distance of the air (default is 1000)
     */
    public AirFilter(ColorRGBA airColor, float airDensity, float airDistance) {
        this();
        this.airColor = airColor;
        this.airDensity = airDensity;
        this.airDistance = airDistance;
    }

    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/Air/Air.j3md");
        material.setColor("AirColor", airColor);
        material.setFloat("AirDensity", airDensity);
        material.setFloat("AirDistance", airDistance);
        material.setFloat("AirDesaturation", airDesaturation);
    }

    @Override
    protected Material getMaterial() {

        return material;
    }


    /**
     * returns the air color
     * @return the pre-existing instance
     */
    public ColorRGBA getAirColor() {
        return airColor;
    }

    /**
     * Sets the color of the air. In general should be blueish
     * @param airColor
     */
    public void setAirColor(ColorRGBA airColor) {
        if (material != null) {
            material.setColor("AirColor", airColor);
        }
        this.airColor = airColor;
    }

    /**
     * returns the air density
     * @return the density value
     */
    public float getAirDensity() {
        return airDensity;
    }

    /**
     * Sets the density of the air, a high value gives a thick air
     * @param airDensity
     */
    public void setAirDensity(float airDensity) {
         checkFloatArgument(airDensity, 0, 1f, "AirDensity");
        if (material != null) {
            material.setFloat("AirDensity", airDensity);
        }
        this.airDensity = airDensity;
    }

    /**
     * returns the air distance
     * @return the distance
     */
    public float getAirDistance() {
        return airDistance;
    }

    /**
     * the distance of the air. the higer the value the distant the air looks
     * @param airDistance
     */
    public void setAirDistance(float airDistance) {
        checkFloatArgument(airDistance, 0, 200f, "AirDistance");
        if (material != null) {
            material.setFloat("AirDistance", airDistance);
        }
        this.airDistance = airDistance;
    }
 /**
     * returns the air desaturation
     * @return the airDesaturation
     */
    public float getAirDesaturation() {
        return airDesaturation;
    }

    /**
     * the desaturation factor based on distance. The higer the value the faster models get desaturated  1-2 seems resonable.
     * @param airDesaturation
     */
    public void setAirDesaturation(float airDesaturation) {
          checkFloatArgument(airDesaturation, 0, 5f, "AirDesaturation");
        if (material != null) {
            material.setFloat("AirDesaturation", airDesaturation);
        }
        this.airDesaturation = airDesaturation;
    }
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(airColor, "airColor", ColorRGBA.White.clone());
        oc.write(airDensity, "airDensity", 0.7f);
        oc.write(airDistance, "airDistance", 1000);
        oc.write(airDesaturation, "airDesaturation", 1f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        airColor = (ColorRGBA) ic.readSavable("airColor", ColorRGBA.White.clone());
        airDensity = ic.readFloat("airDensity", 0.7f);
        airDistance = ic.readFloat("airDistance", 1000);
        airDesaturation = ic.readFloat("airDesaturation", 1f);
    }

private   void checkFloatArgument(float value, float min, float max, String name) {
if (value < min || value > max) {
throw new IllegalArgumentException(name + " was " + value + " but should be between " + min + " and " + max);
}
}
}
