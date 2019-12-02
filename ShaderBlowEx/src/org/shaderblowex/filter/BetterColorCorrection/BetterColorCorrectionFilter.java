package org.shaderblowex.filter.BetterColorCorrection;

 


import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;


public class BetterColorCorrectionFilter extends Filter {
    
     
    private  final float DEFAULT_BRIGHTNESS=0.0f;  
    private  final float DEFAULT_CONTRAST=1.0f;  
    private  final float DEFAULT_HUE=0.0f;  
    private  final float DEFAULT_SATURATION=.0f;  
    private  final float DEFAULT_INVERT=0.0f;  
    private  final float DEFAULT_RED=1.0f;  
    private  final float DEFAULT_GREEN=1.0f;  
    private  final float DEFAULT_BLUE=1.0f;  
    private  final float DEFAULT_GAMMA=1.0f;  
    
    
    private float brightness=DEFAULT_BRIGHTNESS;
    private float contrast = DEFAULT_CONTRAST;
    private float hue=DEFAULT_HUE;
    private float saturation=DEFAULT_SATURATION;
    private float invert=DEFAULT_SATURATION;
    private float red=DEFAULT_SATURATION;
    private float green=DEFAULT_SATURATION;
    private float blue=DEFAULT_SATURATION;
    private float gamma=DEFAULT_GAMMA;

    
    /**
     * Creates a FFilter with default settings.
     */
    public BetterColorCorrectionFilter() {
        super("BetterColorCorrectionFilter");
    }
    
    /**
     *@param contrast default is 1,ranges from 0 to 10.
     * @param  brightness default is 0. ranges from 0 to 1.
     * @param  hue default 0, ranges from -1 to 1.
     * @param saturation  default 0. ranges from -1 to 1.
     * @param invert  default 0. ranges from 0 to 1.
     * @param red  default 1. ranges from 0 to 10.
     * @param green  default 1. ranges from 0 to 10.
     * @param blue  default 1. ranges from 0 to 10.
     * @param gamma  default 1. ranges from 0 to 10. 
     */
     public BetterColorCorrectionFilter(float contrast, float brightness, float hue, float saturation, float invert, float red, float green, float blue, float gamma) 
       {
        super("BetterColorCorrectionFilter");
       
        // 
        checkFloatArgument(contrast, 0f, 100f, "Contrast");
        checkFloatArgument(brightness, 0f, 1f, "Brightness");
        checkFloatArgument(hue, -1f, 1f, "Hue");
        checkFloatArgument(saturation, -1f, 1f, "Saturation");
        checkFloatArgument(invert, 0f, 1f, "Invert");
        checkFloatArgument(red, 0f, 10f, "Red");
        checkFloatArgument(green, 0f, 10f, "Green");
        checkFloatArgument(blue, 0f, 10f, "Blue");
        checkFloatArgument(gamma, 0f, 10f, "Gamma");
        //
        this.brightness = brightness;
        this.contrast = contrast;
        this.hue = hue;
        this.saturation = saturation;
        this.invert = invert;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.gamma = gamma;
        }
    
     
    
    
    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/BetterColorCorrection/BetterColorCorrection.j3md");
        if(brightness!=DEFAULT_BRIGHTNESS)
           material.setFloat("Brightness", brightness);
        if(contrast!=DEFAULT_CONTRAST)
            material.setFloat("Contrast", contrast);
        if(hue!=DEFAULT_HUE)
            material.setFloat("Hue", hue);
        if(saturation!=DEFAULT_SATURATION)
           material.setFloat("Saturation", saturation);
        if(invert!=DEFAULT_INVERT)
            material.setFloat("Invert", invert);
        if(red!=DEFAULT_RED)
            material.setFloat("Red", red);
        if(green!=DEFAULT_GREEN)
            material.setFloat("Green", green);
        if(blue!=DEFAULT_BLUE)
            material.setFloat("Blue", blue);
        if(gamma!=DEFAULT_GAMMA)
            material.setFloat("Gamma", gamma);
       
    }

    @Override
    protected Material getMaterial() {
         return material;
    }
    
    
    /**Keep 0 to do nothing
     *@param brightness the brightness.
     */
     public void setBrightness(float brightness) {
       checkFloatArgument(brightness, 0f, 1f, "Brightness");
        if (material != null) 
           material.setFloat("Brightness", brightness);
       
       this.brightness = brightness;
    }

    /**default = 1
     *@param saturation the saturation, needs to be between -1 and 1.
     */
    public void setSaturation(float saturation) {
        checkFloatArgument(saturation, -1f, 1f, "Saturation"); 
         this.saturation = saturation;
         if (material != null)  
             material.setFloat("Saturation", saturation);
    }
     
   /**Default = 1
    *@param contrast the contrast value.
    */
    public void setContrast(float contrast) {
         material.setFloat("Contrast", contrast);
         if (material != null)  
             material.setFloat("Contrast", contrast);
         
        this.contrast = contrast;
    }
    /**Default = 1. From 0;
    *@param invert the invert value by which colors get inverted.
    */
    public void setInvert(float invert) {
        
        checkFloatArgument(invert, 0f, 1f, "Invert");
        if (material != null)  
             material.setFloat("Invert", invert);
        
        this.invert = invert;
    }
   /**Default = 1. From 0 to 10;
    *@param red - red color to enhance.
    */
    public void setRed(float red) {
          checkFloatArgument(red, 0f, 10f, "Red");
        if (material != null)  
             material.setFloat("Red", red);
        
        this.red = red;
    }
      /**Default = 1. From 0 to 10;
    *@param green - green color to enhance.
    */
    public void setGreen(float green) {
          checkFloatArgument(green, 0f, 10f, "Red");
        if (material != null)  
             material.setFloat("Green", green);
        
        this.green = green;
    }
    /**Default = 1. From 0 to 10;
    *@param blue - blue color to enhance.
    */
    public void setBlue(float blue) {
          checkFloatArgument(green, 0f, 10f, "Red");
        if (material != null)  
             material.setFloat("Blue", blue);
        
        this.blue = blue;
    }
    /**@return the extra saturation*/
    public float getSaturation() {
        return saturation;
    }
  

     /**@return the extra brightness*/
     public float getBrightness() {
        return brightness;
    }
     public float getHue() {
        return hue;
    }

    /**Sets the hue value, range from -1 to 1, any other value doesn't work.*/
   public void setHue(float hue) {
       if (hue >-1 && hue <1){
           this.hue = hue;
           if (material != null) {
                material.setFloat("Hue", hue);
           }
       }
    }
   
    /**@return the extra contrast*/
    public float getContrast() {
        return contrast;
    }

   /**
     * Set the gamma of the scen.
     * 
     * @param The gamma of the scene. 
     */
    public void setGamma(float gamma) {
        
        checkFloatArgument(gamma, 0, 10f, "Gamma");
        
        if (material != null) {
            material.setFloat("Gamma", gamma);
        }
        this.gamma = gamma;
    }
    
    /**
     * Get gamma.
     * 
     * @return The gamma of the scene. 
     */
    public float getGamma() {
        return gamma;
    }
  
    /**
     * Get red.
     * 
     * @return The red of the scene. 
     */
    public float getRed() {
        return red;
    }
 /**
     * Get green.
     * 
     * @return The green of the scene. 
     */
    public float getGreen() {
        return green;
    }
     /**
     * Get blue.
     * 
     * @return The blue of the scene. 
     */
    public float getBlue() {
        return blue;
    }
      /**@return the invert value*/
    public float getInvert() {
        return invert;
    }
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(contrast, "contrast", 1.0);
        oc.write(brightness, "brightness", 1.0f);
        oc.write(hue, "hue", 1.0);
        oc.write(saturation, "saturation", 1.0);
        oc.write(invert, "invert", 1.0);
        oc.write(red, "red", 1.0);
        oc.write(blue, "blue", 1.0);
        oc.write(green, "green", 1.0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        contrast = ic.readFloat("contrast", 1.0f );
        brightness = ic.readFloat("brightness", 1.0f );
        hue = ic.readFloat("hue", 1.0f );
        saturation = ic.readFloat("saturation", 1.0f );
        invert = ic.readFloat("invert", 1.0f );
        red = ic.readFloat("red", 1.0f );
        blue = ic.readFloat("blue", 1.0f );
        green = ic.readFloat("green", 1.0f );
    }

private   void checkFloatArgument(float value, float min, float max, String name) {
if (value < min || value > max) {
throw new IllegalArgumentException(name + " was " + value + " but should be between " + min + " and " + max);
}
}
private   void checkIntArgument(int value, int min, int max, String name) {
if (value < min || value > max) {
throw new IllegalArgumentException(name + " was " + value + " but should be between " + min + " and " + max);
}
}

}
