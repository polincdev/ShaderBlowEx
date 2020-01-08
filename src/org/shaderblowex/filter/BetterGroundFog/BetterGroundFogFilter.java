package org.shaderblowex.filter.BetterGroundFog;
 
    
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;

/**
 *
 * @author Alexander Kasigkeit <alexander.kasigkeit@web.de>
 */
public class BetterGroundFogFilter extends Filter {

    private Vector3f sunDirection = new Vector3f(-1, -1, -1).normalizeLocal();
    private ColorRGBA sunColor = new ColorRGBA(1.0f, 0.9f, 0.7f, 1.0f);
    private float sunShininess = 8.0f;

    private ColorRGBA fogColor = new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f);
    private float fogDensity = 0.025f;
    private float groundLevel = 0f;
    private Vector4f fogBoundary = new Vector4f(0, 1, 0, -50);

    private float k = 0f;
    private float cdotf = 0f;

    public BetterGroundFogFilter() {
        super("BetterGroundFogFilter");
    }
  public BetterGroundFogFilter(Vector3f sunDirection,ColorRGBA sunColor,float sunShininess,ColorRGBA fogColor,float fogDensity,float groundLevel ,Vector4f fogBoundary  ) {
        this();
        this.sunDirection=sunDirection;
        this. sunColor=sunColor;
        this. sunShininess=sunShininess;
        this. fogColor=fogColor;
        this. fogDensity=fogDensity;
        this. groundLevel=groundLevel;
        this. fogBoundary=fogBoundary; 
    }
    /**
     * sets direction of the sunlight
     *
     * @param direction normalized direction pointing from the sun, not towards the sun
     */
    public void setSunDirection(Vector3f direction) {
        sunDirection = direction;
    }

    /**
     * set the fogs color when looking right into the sun through the fog, default is 1.0, 0.9, 0.7 (slightly yellowish)
     *
     * @param color fogs color
     */
    public void setSunColor(ColorRGBA color) {
        sunColor = color;
    }

    /**
     * used to adjust the amount of fog thats influenced by the sun, higher values give smaller areas ( pow(sunFactor,
     * sunShininess)), default is 8.0
     *
     * @param shininess the shininess of the sun
     */
    public void setSunShininess(float shininess) {
        sunShininess = shininess;
    }

    /**
     * set the fogs color, default is 0.5, 0.6, 0.7 (slightly blueish grey)
     *
     * @param color fogs color
     */
    public void setFogColor(ColorRGBA color) {
        fogColor = color;
    }

    /**
     * sets the fogs density, higher values give thicker fog, default is 0.025
     *
     * @param density fogs density
     */
    public void setFogDensity(float density) {
        fogDensity = density;
    }

    /**
     * sets the height of the groundLevel (can be used to prevent from underwaterfog). each fragment that is calculated
     * to be below this position is treated as if it was at this height, default is 0. set to Float.MIN_VALUE to turn
     * off
     *
     * @param groundLevel lowest level to apply fog to
     */
    public void setGroundLevel(float groundLevel) {
        this.groundLevel = groundLevel;
    }

    /**
     * sets the fogs boundary, x,y and z components should be its normal pointing away from the fog, w is the distance,
     * default is 0,1,0,-50 (that means fog lays horizontally and starts at y=50 and). After the fogs plane was updated,
     * updateCameraPosition(pos) should be called
     *
     * @param boundary the plane that seperates fogged from not fogged area
     */
    public void setFogBoundary(Vector4f boundary) {
        fogBoundary = boundary;
    }

    /**
     * should be called whenever the camera position changes, updates c-dot-f and k so it does not have to be calculated
     * for each fragment
     *
     * @param pos current position of the camera
     */
    public void updateCameraPosition(Vector3f pos) {
        if (fogBoundary == null) {
            throw new NullPointerException("cannot update camera position: fog boundary is null");
        }
        cdotf = fogBoundary.x * pos.x + fogBoundary.y * pos.y + fogBoundary.z * pos.z + fogBoundary.w;
        if (cdotf > 0f) {
            k = 0f;
        } else {
            k = 1f;
        }
    }

    /**
     * returns sunDirection
     *
     * @return sunDirection
     */
    public Vector3f getSunDirection() {
        return sunDirection;
    }

    /**
     * returns sunColor
     *
     * @return sunColor
     */
    public ColorRGBA getSunColor() {
        return sunColor;
    }

    /**
     * returns sunShininess
     *
     * @return sunShininess
     */
    public float getSunShininess() {
        return sunShininess;
    }

    /**
     * returns fogColor
     *
     * @return fogColor
     */
    public ColorRGBA getFogColor() {
        return fogColor;
    }

    /**
     * returns fogDensity
     *
     * @return fogDensity
     */
    public float getFogDensity() {
        return fogDensity;
    }

    /**
     * returns groundLevel
     *
     * @return groundLevel
     */
    public float getGroundLevel() {
        return groundLevel;
    }

    /**
     * returns fogBoundary
     *
     * @return fogBoundary
     */
    public Vector4f getFogBoundary() {
        return fogBoundary;
    }

    @Override
    public boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    public void initFilter(AssetManager assets, RenderManager renderManager,
            ViewPort vp, int w, int h) {
        material = new Material(assets, "ShaderBlowEx/MatDefs/BetterGroundFog/BetterGroundFog.j3md");
    }

    @Override
    public Material getMaterial() {
        if (sunDirection != null) {
            material.setVector3("SunDirection", sunDirection);
            material.setColor("SunColor", sunColor);
            material.setFloat("SunShininess", sunShininess);
        } else {
            clearIfExists(material, "SunDirection");
            clearIfExists(material, "SunColor");
            clearIfExists(material, "SunShininess");
        }
        if (groundLevel > -Float.MAX_VALUE) {
            material.setFloat("GroundLevel", groundLevel);
        } else {
            clearIfExists(material, "GroundLevel");
        }
        material.setColor("FogColor", fogColor);
        material.setFloat("FogDensity", fogDensity);
        material.setVector4("FogBoundary", fogBoundary);
        material.setFloat("K", k);
        material.setFloat("CF", cdotf);
        return material;
    }

    protected void clearIfExists(Material mat, String param) {
        if (material.getParam(param) != null) {
            material.clearParam(param);
        }
    }

    @Override
    public void cleanUpFilter(Renderer r) {
    }

}
