 
package org.shaderblowex.filter.Posterization;

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

 
public class PosterizationFilter extends Filter {
 
    private static final float DEFAULT_STEP=10.f;  
            
     private float step=DEFAULT_STEP;
     
    /**
     * Creates a white highlighting filter with the specified strength. 
       * @param step Step. Default 10. Max 1.0. Min 50.0
     */
    public PosterizationFilter(  float step) {
      super("Posterization");
     // 
      checkFloatArgument(step, 1f, 50f, "Step");
     //
     this.step=step;
        
    }
     

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/Posterization/Posterization.j3md");
        material.setFloat("Step", step);
   
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    
    
    /**
     * Set the step of the effect.
     * 
     * @param The step of the effect. 
     */
    public void setStep(float step) {
        
        checkFloatArgument(step, 1, 50f, "Step");
        
        if (material != null) {
            material.setFloat("Step", step);
        }
        this.step = step;
    }
    
    /**
     * Get step.
     * 
     * @return The step of the effect. 
     */
    public float getStep() {
        return step;
    }
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(step, "step", DEFAULT_STEP);
        
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        step = (Float) ic.readFloat("step", DEFAULT_STEP );
        
        
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