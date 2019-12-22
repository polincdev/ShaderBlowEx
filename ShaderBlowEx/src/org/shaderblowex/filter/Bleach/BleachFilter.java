 
package org.shaderblowex.filter.Bleach;

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
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

 
public class BleachFilter extends Filter {
 
    private static final float DEFAULT_STRENGTH=0.1f;  
            
     private float strength=DEFAULT_STRENGTH;
     
    /**
     * Creates a bleach filter with the specified strength. 
     * Bleach - washed out, desaturated look from Saving Private Ryan or Minority Report
      * @param strength Strength. Default 0.1. Max 5.0. Min 0.0
     */
    public BleachFilter(  float strength) {
      super("BleachMapFilter");
     // 
      checkFloatArgument(strength, 0f, 5f, "Strength");
     //
     this.strength=strength;
        
    }
     

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/Bleach/Bleach.j3md");
        material.setFloat("Strength", strength);
   
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    
    
    /**
     * Set the stregth of the effect.
     * 
     * @param The strength of the effect. 
     */
    public void setStrength(float strength) {
        
        checkFloatArgument(strength, 0, 5f, "Strength");
        
        if (material != null) {
            material.setFloat("Strength", strength);
        }
        this.strength = strength;  
    }
    
    /**
     * Get strength.
     * 
     * @return The strength of the effect. 
     */
    public float getStrength() {
        return strength;
    }
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(strength, "strength", DEFAULT_STRENGTH);
        
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        strength = (Float) ic.readFloat("strength", DEFAULT_STRENGTH );
        
        
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