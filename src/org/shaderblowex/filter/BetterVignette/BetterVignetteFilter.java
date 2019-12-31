package org.shaderblowex.filter.BetterVignette;


import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

 
public class BetterVignetteFilter extends Filter {
 
	/** Default values */
	private static final float DEFAULT_VIGNETTING_STRENGTH = 0.5f;
	private static final float DEFAULT_VIGNETTING_EXTENT = 50f;
         private static final float DEFAULT_BLUR_SIZE = 0.1f;
	private static final float DEFAULT_BLUR_QUALITY = 20f;
         private static final float DEFAULT_GRAY_POWER = 2.0f;
	private static final float DEFAULT_GRAY_MARGIN = 4f;
 
	 
	private float vignetteStrength = DEFAULT_VIGNETTING_STRENGTH;
	private float vignetteExtent = DEFAULT_VIGNETTING_EXTENT;
	private float blurQuality=DEFAULT_BLUR_QUALITY;
        private float blurSize =DEFAULT_BLUR_SIZE;
        private float grayPower=DEFAULT_GRAY_POWER;
        private float grayMargin =DEFAULT_GRAY_MARGIN;
         
        
           
        
	/**
	 * Default Constructor.
	 */
	public BetterVignetteFilter() {
		 
	}
          
	@Override
	protected Material getMaterial() {
		return this.material;
	}
 
	@Override
	protected void initFilter(final AssetManager manager, final RenderManager renderManager, final ViewPort vp,
			final int w, final int h) {
		this.material = new Material(manager, "ShaderBlowEx/MatDefs/BetterVignette/BetterVignette.j3md");
	        material.setFloat("VignetteStrength", vignetteStrength);
                material.setFloat("VignetteExtent", vignetteExtent);
		material.setFloat("BlurQuality", blurQuality);
                material.setFloat("BlurSize", blurSize);
                material.setFloat("GrayPower", grayPower);
                material.setFloat("GrayMargin", grayMargin);
	}

	 
 public void setVignetteStrength(float vignetteStrength)
         {
        checkFloatArgument(vignetteStrength, 0, 1.0f, "VignetteStrength");
          if (this.material != null) {
                  this.material.setFloat("VignetteStrength", vignetteStrength );
                  this.vignetteStrength = vignetteStrength;
          }
	}

	public float getVignetteStrength() {
		return vignetteStrength;
	}
  	 
 public void setVignetteExtent(float vignetteExtent) 
        {
        checkFloatArgument(vignetteExtent, 0, 100.0f, "VignetteExtent");
        if (this.material != null) {
                 this.material.setFloat("VignetteExtent", vignetteExtent );
                this.vignetteExtent = vignetteExtent;
        }
	}

 public float getVignetteExtent() {
		return vignetteExtent;
	}      
      	 
 public void setBlurSize(float blurSize) 
        {
        checkFloatArgument(blurSize, 0, 1.0f, "BlurSize");
        if (this.material != null) {
                 this.material.setFloat("BlurSize", blurSize );
                this.blurSize = blurSize;
        }
	}

 public float getBlurSize() {
		return blurSize;
	}      
  public void setBlurQuality(float blurQuality) 
        {
        checkFloatArgument(blurQuality, 0, 50.0f, "BlurQuality");
        if (this.material != null) {
                 this.material.setFloat("BlurQuality", blurQuality );
                this.blurQuality = blurQuality;
        }
	}

 public float getBlurQuality() {
		return blurQuality;
	} 
 
 public void setGrayPower(float grayPower) 
        {
        checkFloatArgument(grayPower, 0, 5.0f, "GrayPower");
        if (this.material != null) {
                 this.material.setFloat("GrayPower", grayPower );
                this.grayPower = grayPower;
        }
	}

 public float getGrayPower() {
		return grayPower;
	} 
 
  public void setGrayMargin(float grayMargin) 
        {
        checkFloatArgument(grayMargin, 0, 10.0f, "GrayMargin");
        if (this.material != null) {
                 this.material.setFloat("GrayMargin", grayMargin );
                this.grayMargin = grayMargin;
        }
	}

 public float getGrayMargin() {
		return grayMargin;
	} 
 
    private   void checkFloatArgument(float value, float min, float max, String name) {
if (value < min || value > max) {
throw new IllegalArgumentException(name + " was " + value + " but should be between " + min + " and " + max);
}
}
}
