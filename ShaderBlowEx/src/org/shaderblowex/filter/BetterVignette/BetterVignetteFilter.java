package org.shaderblowex.filter.BetterVignette;

 

import java.util.Random;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
  * <pre>
 * Features:
 * 	 Allow to set the <strong>vignetting's diameter</strong>. Default is 0.9. Shader clamps this value between 0 to 
 * 	1.4. Vignetting effect is made using two circles. The inner circle represents the region untouched by vignetting.
 * 	The region between the inner and outer circle represent the area where vignetting starts to take place, which is 
 * 	a gradual fade to black from the inner to outer ring. Any part of the frame outside of the outer ring would be 
 * 	completely black.
 * </pre>
 * 
  */
public class BetterVignetteFilter extends Filter {
 
	/** Default values */
	private static final float DEFAULT_VIGNETTING_STRENGTH = 0.5f;
	private static final float DEFAULT_VIGNETTING_EXTENT = 50f;
 
	 
	private float vignetteStrength = DEFAULT_VIGNETTING_STRENGTH;
	private float vignetteExtent = DEFAULT_VIGNETTING_EXTENT;
	 
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
     
    private   void checkFloatArgument(float value, float min, float max, String name) {
if (value < min || value > max) {
throw new IllegalArgumentException(name + " was " + value + " but should be between " + min + " and " + max);
}
}
}
