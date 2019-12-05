package org.shaderblowex.filter.BokehDoF;

  
  
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
 
public class BokehDoFFilter extends Filter {

    
    private final float DEFAULT_FOCUS_POINT=8f;  
    private final float DEFAULT_RADIUS_SCALE=0.5f;  
    private final float DEFAULT_BLUR_SIZE=3;  
    private final float DEFAULT_FOCUS_SCALE=12;  
    
    
    private float focusPoint = DEFAULT_FOCUS_POINT;
    private float radiusScale = DEFAULT_RADIUS_SCALE;
     private float blurSize = DEFAULT_BLUR_SIZE;
     private float focusScale = DEFAULT_FOCUS_SCALE;
      
     private float xScale;
    private float yScale;
    
   

    /**
     * Creates a BokehDoFFilter filter
     */
    public BokehDoFFilter() {
        super("FocusFilter");
    }

    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    protected Material getMaterial() {

        return material;
    }

    @Override
    protected void initFilter(AssetManager assets, RenderManager renderManager,
            ViewPort vp, int w, int h) {
        material = new Material(assets, "ShaderBlowEx/MatDefs/BokehDoF/BokehDoF.j3md");
        material.setFloat("FocusPoint", focusPoint);
        material.setFloat("RadiusScale", radiusScale);
        material.setFloat("BlurSize", blurSize);
        material.setFloat("FocusScale", focusScale);
     

        xScale = 1.0f / w;
        yScale = 1.0f / h;

        material.setFloat("XScale",   xScale);
        material.setFloat("YScale",  yScale);
    }

    /**
     *  Sets the point at which objects are purely in focus.
     */
    public void setFocusPoint(float f) {
       checkFloatArgument(focusPoint, 0.0f,   50.0f, "FocusPoint"); 
        this.focusPoint = f;
        if (material != null) {
            material.setFloat("FocusPoint", focusPoint);
        }

    }

    /**
     * returns the focus point
     * @return the distance
     */
    public float getFocusPoint() {
        return focusPoint;
    }

    /**
     *  Sets the range to either side of focusPoint where the
     *  objects go gradually out of focus.  
     */
    public void setRadiusScale(float f) {
         checkFloatArgument(radiusScale, 0.0f,   1.0f, "RadiusScale"); 
        this.radiusScale = f;
        if (material != null) {
            material.setFloat("RadiusScale", radiusScale);
        }

    }

    /**
     * returns the radiusScale
     * @return radiusScale
     */
    public float getRadiusScale() {
        return radiusScale;
    }

      /**
     *  Sets Focus scale which is responsible for blur level
      */
    public void setFocusScale(float f) {
         checkFloatArgument(focusScale, 0.0f,   20.0f, "FocusScale"); 
        this.focusScale = f;
        if (material != null) {
            material.setFloat("FocusScale", focusScale);
        }

    }

    /**
     * returns the focusScale
     * @return focusScale
     */
    public float getFocusScale() {
        return focusScale;
    }


    /**
     *  Sets the minimum blur factor before the convolution filter is
     *  calculated.  The default is 15  
     */
    public void setBlurSize( float f ) {
       checkFloatArgument(blurSize, 0.0f,   20.0f, "BlurSize"); 
        this.blurSize = f;
        if (material != null) {
            material.setFloat("BlurSize", blurSize);
        }
    }

    /**
     * returns the blur factor.
     * @return the blurSize
     */
    public float getBlurSize() {
        return blurSize;
    }
 
  
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
         oc.write(blurSize, "blurSize", 0.2f);
        oc.write(focusPoint, "focusPoint", 50f);
        oc.write(radiusScale, "radiusScale", 0.5f);
          oc.write(focusScale, "focusScale", 0.5f);
      }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        blurSize = ic.readFloat("blurSize", 1f);
        focusPoint = ic.readFloat("focusPoint", 50f);
        radiusScale = ic.readFloat("radiusScale", 0.5f);
        focusScale = ic.readFloat("focusScale", 0.5f);
        
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