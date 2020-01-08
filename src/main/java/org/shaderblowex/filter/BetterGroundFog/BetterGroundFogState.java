package org.shaderblowex.filter.BetterGroundFog;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 


import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author Alexander Kasigkeit <alexander.kasigkeit@web.de>
 */
public class BetterGroundFogState extends BaseAppState {

    protected final ColorRGBA FOG_DAY = new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f);
    protected final ColorRGBA FOG_NIGHT = new ColorRGBA(0.25f, 0.3f, 0.35f, 1.0f);
    protected final ColorRGBA FOG_INDOORS = new ColorRGBA(0.10f, 0.10f, 0.14f, 1.0f);

    protected final ColorRGBA SUN_DAY = new ColorRGBA(1.0f, 0.9f, 0.7f, 1.0f);
    protected final ColorRGBA SUN_NIGHT = new ColorRGBA(0.35f, 0.45f, 0.5f, 1.0f);
    protected final ColorRGBA SUN_INDOORS = FOG_INDOORS;

    protected final ColorRGBA FOG_COLOR = new ColorRGBA(FOG_DAY);
    protected final ColorRGBA SUN_COLOR = new ColorRGBA(SUN_DAY);
    protected final ColorRGBA TARGET_FOG_COLOR = new ColorRGBA();
    protected final ColorRGBA TARGET_SUN_COLOR = new ColorRGBA();

    protected BetterGroundFogFilter filter = new BetterGroundFogFilter();

    protected DirectionalLight sun = null;
    protected float fadeTime = 2f;
    protected boolean isIndoors = false, isNight = false;

    @Override
    protected void initialize(Application app) {
        if (sun != null) {
            filter.setSunDirection(sun.getDirection());
        }
        filter.updateCameraPosition(app.getCamera().getLocation());
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    @Override
    public void update(float tpf) {
        filter.updateCameraPosition(getApplication().getCamera().getLocation());
        if (sun != null) {
            filter.setSunDirection(sun.getDirection());
        }

        if (isIndoors) {
            TARGET_SUN_COLOR.set(SUN_INDOORS);
            TARGET_FOG_COLOR.set(FOG_INDOORS);
        } else {
            if (isNight) {
                TARGET_SUN_COLOR.set(SUN_NIGHT);
                TARGET_FOG_COLOR.set(FOG_NIGHT);
            } else {
                TARGET_SUN_COLOR.set(SUN_DAY);
                TARGET_FOG_COLOR.set(FOG_DAY);
            }
        }
        float perc = tpf / fadeTime;
        FOG_COLOR.interpolateLocal(TARGET_FOG_COLOR, perc);
        SUN_COLOR.interpolateLocal(TARGET_SUN_COLOR, perc);
        filter.setSunColor(SUN_COLOR);
        filter.setFogColor(FOG_COLOR);
    }

    /**
     * saves a reference to the directional light used as sunlight, to send uptodate direction to the shader per frame
     *
     * @param sun DirectionalLight used as sun
     */
    public void setSun(DirectionalLight sun) {
        this.sun = sun;
    }

    /**
     * sets the duration in seconds it takes to fade from indoors to outdoors / night to day
     *
     * @param fadeTime time in seconds, default 2.0f
     */
    public void setFadeTime(float fadeTime) {
        this.fadeTime = fadeTime;
    }

    /**
     * used to set indoors state, in indoors state fog color and sunlight color are the same to prevent from light
     * scattering inside
     *
     * @param isIndoors true for setting to indoor, default is false
     */
    public void setIsIndoors(boolean isIndoors) {
        this.isIndoors = isIndoors;
    }

    /**
     * at night you usually want darker fog and no yellowish lightscattering so you can define night colors and then
     * fade to them by activating night mode
     *
     * @param isNight true for settings to night, default is false
     */
    public void setIsNight(boolean isNight) {
        this.isNight = isNight;
    }

    /**
     * sets the fog color used for fog at daytime that is not influenced by sunlight
     *
     * @param color fogs color, default is (0.5f, 0.6f, 0.7f, 1.0f) slightly blueish gray
     */
    public void setFogColorDay(ColorRGBA color) {
        FOG_DAY.set(color);
    }

    /**
     * sets the color used for 'lightscattering' at day, that is the color the fog fades into when looking into the
     * direction of the sun
     *
     * @param color sun color, default is (1.0f, 0.9f, 0.7f, 1.0f) slightly yellowish grey
     */
    public void setSunColorDay(ColorRGBA color) {
        SUN_DAY.set(color);
    }

    /**
     * sets the fog color used for fog at nighttime that is not influenced by sunlight
     *
     * @param color fogs color, default is (0.25f, 0.3f, 0.35f, 1.0f) slightly blueish darker gray
     */
    public void setFogColorNight(ColorRGBA color) {
        FOG_NIGHT.set(color);
    }

    /**
     * sets the color used for 'lightscattering' at night, that is the color the fog fades into when looking into the
     * direction of the moon
     *
     * @param color moon color, default is (0.35f, 0.45f, 0.5f, 1.0f) slightly blueish grey
     */
    public void setSunColorNight(ColorRGBA color) {
        SUN_NIGHT.set(color);
    }

    /**
     * sets the fog color used for fog indoors that is never influenced by sunlight
     *
     * @param color fogs color, default is (0.10f, 0.10f, 0.14f, 1.0f) slightly blueish darker gray, useful for caves to
     * prevent them from seeming brighter due to grey fog
     */
    public void setFogColorIndoors(ColorRGBA color) {
        FOG_INDOORS.set(color);
    }

    /**
     * sets the color used for 'lightscattering' indoors, that is the color the fog fades into when looking into the
     * direction of the light, default is the same as indoors fog color so you get no lightscattering effect indoors
     *
     * @param color light color, default is (0.10f, 0.10f, 0.14f, 1.0f), same as indoors fog color
     */
    public void setSunColorIndoors(ColorRGBA color) {
        SUN_INDOORS.set(color);
    }

    /**
     * returns a reference to the directional light used as sun
     *
     * @return sun, or null if not set
     */
    public DirectionalLight getSun() {
        return sun;
    }

    /**
     * get the time used to fade between different fog colors
     *
     * @return fadeTime in seconds
     */
    public float getFadeTime() {
        return fadeTime;
    }

    /**
     * returns current indoors state
     *
     * @return true if fog is in indoors state
     */
    public boolean isIndoors() {
        return isIndoors;
    }

    /**
     * returns current night state
     *
     * @return true if fog is in night state
     */
    public boolean isNight() {
        return isNight;
    }

    /**
     * returns a reference to the BetterFogFilter, you need that to get a reference to add to your FilterPostProcessor
     * at the position you prefer (should be after shadows and postprocessing water at least)
     *
     * @return the filter this state updates
     */
    public BetterGroundFogFilter getFilter() {
        return filter;
    }

}