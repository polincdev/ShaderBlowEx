 
package org.shaderblowex.filter.BetterToneMap;

/**
 *
 * @author xxx
 */
 

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

 
public class BetterToneMapFilter extends Filter {

     static public int TYPE_LINEAR=0;    
     static public int TYPE_SIMPLE_REINHARD=1;    
     static public int TYPE_LUMA_BASED_REINHARD=2;    
     static public int TYPE_WHITE_PRESERVING_REINHARD=3;    
     static public int TYPE_RONBIN_DAHOUSE=4;    
     static public int TYPE_ACES_FILM=5;    
     static public int TYPE_ACES_ABSOLUTE_FILM=6;  
     static public int TYPE_FILMIC=7;    
     static public int TYPE_UNCHARTED2=8;    
     static public int TYPE_DX11DSK=9;    
     static public int TYPE_TIMOTHY=10;    
     static public int TYPE_EXPONENTIAL=11;    
     static public int TYPE_UNREAL=12;    
     static public int TYPE_AMD_LOTTES=13;    
     static public int TYPE_REINHARD2=14;    
     static public int TYPE_UCHIMURA=15;    
     static public int TYPE_FERWERDA=16;    
     static public int TYPE_HAARNPIETERDUIKER=17;    
     static public int TYPE_WARD=18;    
     static public int TYPE_TUMBLIN_RESHMEIER=19;    
      static public int TYPE_SCHICK=20;    
      static public int TYPE_REINHARD_EXTENDED=21;    
      static public int TYPE_REINHARD_DEVLIN=22;    
      static public int TYPE_MEAN_VALUE=23;    
      static public int TYPE_MAX_DIVISION=24;    
      static public int TYPE_LOGARITMIC=25;    
      static public int TYPE_INSOMNIAC=26;    
      static public int TYPE_GRAHAM_ALDRIDGE_FILMIC=27;    
      static public int TYPE_EXPONENTIATION=28;    
      static public int TYPE_EXPONENTIAL2=29;    
      static public int TYPE_DROGO=30;  
      static public int TYPE_CLAMPING=31;    
      static public int TYPE_JODIE_ROBO=32;    
      static public int TYPE_JODIE_REINHARD=33;    
      static public int TYPE_BARTEROPE=34;    
     static public int TYPE_GIFFORD=35;    
    
       
    private static final int DEFAULT_TONEMAP_TYPE=0;  
    private static final float DEFAULT_GAMMA=1.0f;  
    private static final float DEFAULT_EXPOSURE=1.0f;  
           
    private int tonemapType=DEFAULT_TONEMAP_TYPE;
    private float gamma=DEFAULT_GAMMA;
    private float exposure=DEFAULT_EXPOSURE;
    /**
     * Creates a tone-mapping filter with the specified type, exposure and gamma.
     * 
     * @param type  Tonemap type. Use internal variables
     * @param exposure Exposure. Default 1.0
     * @param gamma Gamma. Default 1.0
     */
    public BetterToneMapFilter(int tonemapType, float exposure, float gamma) {
                 
     this.tonemapType=tonemapType;
     this.gamma=gamma;
     this.exposure=exposure;
        
    }
     

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/BetterToneMap/BetterToneMap.j3md");
        material.setInt("ToneMapType", tonemapType);
        material.setFloat("Gamma", gamma);
        material.setFloat("Exposure", exposure);
   
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    /**
     * Set the type of the tonemapper. Use predefined types
     * 
     * @param The type of the tonemapper. 
     */
    public void setType(int tonemapType) {
        
        checkIntArgument(tonemapType, 0, 35, "TonemapType"); 
        
        if (material != null) {
            material.setInt("ToneMapType", tonemapType);
        }
        this.tonemapType = tonemapType;
    }
    
    /**
     * Get type.
     * 
     * @return The type of the tonemapping. 
     */
    public float getType() {
        return tonemapType;
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
     * Set the gamma of the scen.
     * 
     * @param The gamma of the scene. 
     */
    public void setExposure(float exposure) {
        
        checkFloatArgument(exposure, 0, 10f, "Gamma");
        
        if (material != null) {
            material.setFloat("Exposure", exposure);
        }
        this.exposure = exposure;
    }
    
    /**
     * Get exposure.
     * 
     * @return The exposure of the scene. 
     */
    public float getExposure() {
        return exposure;
    }
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(gamma, "Gamma", DEFAULT_GAMMA);
          oc.write(exposure, "Exposure", DEFAULT_EXPOSURE);
          oc.write(tonemapType, "ToneMapType",DEFAULT_TONEMAP_TYPE);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        gamma = (Float) ic.readFloat("Gamma", DEFAULT_GAMMA );
         exposure = (Float) ic.readFloat("Exposure", DEFAULT_EXPOSURE );
        tonemapType = (Integer) ic.readInt("ToneMapType", DEFAULT_TONEMAP_TYPE );
        
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