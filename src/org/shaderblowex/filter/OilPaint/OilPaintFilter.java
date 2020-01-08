 
package org.shaderblowex.filter.OilPaint;
 
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

 
public class OilPaintFilter extends Filter {
 
    private static final int DEFAULT_STRENGTH=5;  
            
     private int strength=DEFAULT_STRENGTH;
    /**
     * Creates a OilpaintFilter . 
      
     */
    public OilPaintFilter(  ) {
      super("OilPaintFilter");
   
    }
      /**
     * Creates a oil paint filter with the specified strength. 
       * @param strength Strength. Default 5. Max 25 . Min 1.0
     */
    public OilPaintFilter(  int strength) {
     this();
     // 
       checkIntArgument(strength, 1, 25, "Strength");
     //
     this.strength=strength;
        
    }
     

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/OilPaint/OilPaint.j3md");
          material.setInt("Strength", strength);
   
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
    public void setStrength(int strength) {
        
        checkIntArgument(strength, 1, 25, "Strength");
        
        if (material != null) {
            material.setInt("Strength", strength);
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
        strength = (Integer) ic.readInt("strength", DEFAULT_STRENGTH );
        
        
    }
private   void checkIntArgument(int value, int min, int max, String name) {
if (value < min || value > max) {
throw new IllegalArgumentException(name + " was " + value + " but should be between " + min + " and " + max);
}
}
 
 
}