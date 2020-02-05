 
package main.java.org.shaderblowex.filter.WhiteBloom;
 
 
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

 
public class WhiteBloomFilter extends Filter {
 
    private static final float DEFAULT_STRENGTH=0.1f;  
    private static final float DEFAULT_SCALE=256f;  
            
      private float strength=DEFAULT_STRENGTH;
       private float scale=DEFAULT_SCALE;
     
       
   public WhiteBloomFilter(  ) {
      super("WhiteBloomFilter");
   }
    /**
     * Creates white bloom  filter with the specified strength. 
     * @param strength Strength. Default 0.1. Max 5.0. Min 0.0
     */
    public WhiteBloomFilter(  float strength) {
      this();
     // 
      checkFloatArgument(strength, 0f, 10f, "Strength");
     //
     this.strength=strength;
        
    }
     

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/WhiteBloom/WhiteBloom.j3md");
        material.setFloat("Strength", strength);
       material.setFloat("Scale", scale);
   
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
        
        checkFloatArgument(strength, 0, 10f, "Strength");
        
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
    
    
     
    /**
     * Set the stregth of the effect.
     * 
     * @param The strength of the effect. 
     */
    public void setScale(float scale) {
        
        checkFloatArgument(strength, 0, 2048f, "Scale");
        
        if (material != null) {
            material.setFloat("Scale", scale);
        }
        this.scale = scale;
    }
    
    /**
     * Get scale.
     * 
     * @return The scale of blur. 
     */
    public float getScale() {
        return scale;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(strength, "strength", DEFAULT_STRENGTH);
        oc.write(strength, "scale", DEFAULT_SCALE);
        
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        strength = (Float) ic.readFloat("strength", DEFAULT_STRENGTH );
        scale = (Float) ic.readFloat("scale", DEFAULT_SCALE );
        
        
    }
private   void checkFloatArgument(float value, float min, float max, String name) {
if (value < min || value > max) {
throw new IllegalArgumentException(name + " was " + value + " but should be between " + min + " and " + max);
}
}
 
}