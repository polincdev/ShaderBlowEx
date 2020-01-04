package org.shaderblowex.filter.MipMapBloom;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 


import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import java.io.IOException;
import java.util.ArrayList;



/**
 * This bloom filter uses mipmaps of the main scene to generate a large
 * bloom effect.
 * The final result is a scene texture with blur of the bright parts, which is
 * calculated by summing over the mipmap levels using the intensity equation:
 * <code>final_color=bloomFactor*sum(mipmap_color*bloomPower^level)</code>
 * <p>
 * Adjustment of the bloom inensity and the downsampling coefficient shall be
 * chosen very carefully.
 */
// *****************************************************************************
   public class MipmapBloomFilter extends Filter
// *****************************************************************************
{
      
   private Quality quality=Quality.High;
   private GlowMode glowMode=GlowMode.Scene;
   private float exposurePower=5.0f;
   private float exposureCutOff=0.0f;
   private float bloomFactor=1.5f;
   private float bloomPower=0.5f;
   private float downSamplingCoef=2.0f;
   private Pass preGlowPass;
   private Pass extractPass;
   private Material extractMat;
   private int screenWidth;
   private int screenHeight;    
   private RenderManager renderManager;
   private ViewPort viewPort;
   private AssetManager assetManager;
   private int initialWidth;
   private int initialHeight;
   private final int numPasses=8;
   private Format texFormat=Format.RGB111110F;

/**
 * GlowMode specifies if the glow will be applied to the whole scene, or to
 * objects that have aglow color or a glow map.
 */
// =============================================================================
   public enum GlowMode
// =============================================================================
{  /**
    * Apply bloom filter to bright areas in the scene. (Default)
    */
   Scene,
   /**
    * Apply bloom only to objects that have a glow map or a glow color.
    */
   Objects,
   /**
    * Apply bloom to both bright parts of the scene and objects with glow map.
    */
   SceneAndObjects;
} // GlowMode ==================================================================
   
   
   
/**
 * The Quality can be adjusted to achieve better results at lower
 * performance.
 */
// =============================================================================
   public enum Quality
// =============================================================================
{
   /**
    * Uses an additional gaussian blur to smooth each mipmap level. (Default)
    */
   High,

   /**
    * Lower quality but better performance mode.
    */
   Low;
} // ===========================================================================

    
/**
 * Instantiates a new bloom filter.
 */
// =============================================================================
   public MipmapBloomFilter()
// =============================================================================
{
   super("MipmapBloomFilter");
} // ===========================================================================



/**
 * Instantiates a new bloom filter with the specified GlowMode.
 * @param glowMode
 */
// =============================================================================
    public MipmapBloomFilter(GlowMode glowMode)
// =============================================================================
{  this();
   this.glowMode=glowMode;
} // ===========================================================================
    
    

/**
 * Instantiates a new bloom filter with the specified quality.
 * @param quality
 */
// =============================================================================
    public MipmapBloomFilter(Quality quality)
// =============================================================================
{  this();
   this.quality=quality;
} // ===========================================================================



/**
 * Instantiates a new bloom filter with the specified quality and GlowMode.
 * @param quality
 * @param glowMode
 */
// =============================================================================
    public MipmapBloomFilter(Quality quality, GlowMode glowMode)
// =============================================================================
{  this();
   this.quality=quality;
   this.glowMode=glowMode;
} // ===========================================================================



// =============================================================================
   @Override
   protected void initFilter(final AssetManager manager, 
    RenderManager renderManager, ViewPort vp, int w, int h)
// =============================================================================
{
   this.renderManager=renderManager;
   this.viewPort=vp;

   this.assetManager=manager;
   this.initialWidth=w;    //640;
   this.initialHeight=h;   //(int)(640.0f*(float)h/(float)w);

   postRenderPasses=new ArrayList<Pass>();
   
// Configure extract pass.
// -----------------------------------------------------------------------------
   postRenderPasses.add(makeExtractPass(manager, renderManager, w, h)); 

// Configure mipmap blur passes.
// -----------------------------------------------------------------------------
// The mipmaps will be generated with according width and height, that can be
// specified implicitly with the downSamplingFactor.
   final Pass[] mmPasses=new Pass[numPasses];
   final Texture2D[] mipmaps=new Texture2D[numPasses];

   for (int ii=0; ii<numPasses; ii++)
   {
      final int passWidth=Math.max(1, 
       (int)(initialWidth/FastMath.pow(downSamplingCoef, (ii+1))));
      final int passHeight=//initialHeight;
       Math.max(1, (int)(initialHeight/FastMath.pow(downSamplingCoef, (ii+1))));

      final Material passMat=new Material(manager,
       "ShaderBlowEx/MatDefs/MipmapBloom/MipmapSampler.j3md");
      final int jj=ii;
      mmPasses[jj]=new Pass()
      {  
         @Override
         public void beforeRender()
         {  
            if (jj==0)
               passMat.setTexture("Texture", extractPass.getRenderedTexture());
            else
               passMat.setTexture("Texture", mmPasses[jj-1]
                .getRenderedTexture());
            
            if (quality==Quality.High)
            {
               passMat.setFloat("Dx", 0.5f/(float)passWidth);
               passMat.setFloat("Dy", 0.5f/(float)passHeight);
            }
         }
      };

      mmPasses[jj].init(renderManager.getRenderer(), passWidth, 
       passHeight, texFormat, Format.Depth, 1, passMat);
      mmPasses[jj].getRenderedTexture().setMagFilter(
       Texture.MagFilter.Bilinear);
      mmPasses[jj].getRenderedTexture().setMinFilter(
       Texture.MinFilter.Trilinear);
      postRenderPasses.add(mmPasses[jj]);

//    In high quality mode each mipmap will be blurred with a gaussian blur,
//    which makes the result much smoother.
      if (quality==Quality.High && jj>=3)
         mipmaps[jj]=gaussianBlur(manager, mmPasses[jj].getRenderedTexture());
      else
         mipmaps[jj]=mmPasses[jj].getRenderedTexture();

   }
      
// Accumulate mipmaps to the final image.
// -----------------------------------------------------------------------------
   material=new Material(manager, "ShaderBlowEx/MatDefs/MipmapBloom/Accumulation.j3md");
   for (int ii=0; ii<numPasses; ii++)
      material.setTexture("Texture"+(ii+1), mipmaps[ii]);
   setBloomIntensity(bloomFactor, bloomPower);
} // initFilter ================================================================


   
/**
 * Informs the FilterPostProcessor that this Filter needs the original scene as 
 * texture.
 * 
 * @return  <code>true</code>
 */
// =============================================================================
   @Override
   protected boolean isRequiresSceneTexture() {return true;}
// =============================================================================

   
   
/**
 * Makes the glow pass and/or extract pass.
 * 
 * @param manager
 * @param renderManager
 * @param w
 * @param h
 * @return 
 */
// =============================================================================
   protected Pass makeExtractPass(AssetManager manager, 
    RenderManager renderManager, int w, int h)
// =============================================================================
{
// Configure the preGlowPass, for use with GlowMode.SceneAndObjects and
// GlowMode.Objects.
// -----------------------------------------------------------------------------
   screenWidth=(int)Math.max(1.0, (w/downSamplingCoef));
   screenHeight=(int)Math.max(1.0, (h/downSamplingCoef));
   if (glowMode!=GlowMode.Scene)
   {  preGlowPass=new Pass();
      preGlowPass.init(renderManager.getRenderer(), screenWidth, screenHeight,
       texFormat, Format.Depth);
   }

// Configure extractPass, extracting bright pixels from the scene.
// -----------------------------------------------------------------------------   
   extractMat=new Material(manager, "Common/MatDefs/Post/BloomExtract.j3md");
   extractPass=new Pass()
   {
      @Override
      public boolean requiresSceneAsTexture() {return true;}

      @Override
      public void beforeRender() {
         extractMat.setFloat("ExposurePow", exposurePower);
         extractMat.setFloat("ExposureCutoff", exposureCutOff);
         if (glowMode!=GlowMode.Scene)
            extractMat.setTexture("GlowMap", preGlowPass.getRenderedTexture());
         extractMat.setBoolean("Extract", glowMode!=GlowMode.Objects);
      }
   };

   extractPass.init(renderManager.getRenderer(), w, h, 
    texFormat, Format.Depth, 1, extractMat);

   extractPass.getRenderedTexture().setMagFilter(Texture.MagFilter.Bilinear);
   extractPass.getRenderedTexture().setMinFilter(Texture.MinFilter.Trilinear);
   
   return extractPass;
} // makeExtractPass =========================================================== 
   
  

/**
 * Adds an additional Gaussian blur (one horizontal and one vertical pass) to
 * the specified texture.
 * 
 * @param manager
 * @param texture A single mipmap texture here.
 * @return 
 */   
// =============================================================================
   private Texture2D gaussianBlur(AssetManager manager, final Texture2D texture)
// =============================================================================
{
   final int w=texture.getImage().getWidth();
   final int h=texture.getImage().getHeight();
   
// Configure horizontal blur pass.
// -----------------------------------------------------------------------------   
   final Material hBlurMat=new Material(manager,
    "ShaderBlowEx/MatDefs/MipmapBloom/HGaussianBlur.j3md");
   final Pass hBlur=new Pass()
   {  @Override
      public void beforeRender()
      {  hBlurMat.setTexture("Texture", texture);
         hBlurMat.setFloat("Size", w);
         hBlurMat.setFloat("Scale", 0.666666f);
      }
   };
   hBlur.init(renderManager.getRenderer(), w, h, texFormat, Format.Depth, 1, 
    hBlurMat);
   hBlur.getRenderedTexture().setMagFilter(Texture.MagFilter.Bilinear);
//   hBlur.getRenderedTexture().setMinFilter(Texture.MinFilter.Trilinear);
   postRenderPasses.add(hBlur);

// Configure vertical blur pass.
// -----------------------------------------------------------------------------
   final Material vBlurMat=new Material(manager,
    "ShaderBlowEx/MatDefs/MipmapBloom/VGaussianBlur.j3md");
   final Pass vBlur=new Pass()
   {  @Override
      public void beforeRender()
      {  vBlurMat.setTexture("Texture", hBlur.getRenderedTexture());
         vBlurMat.setFloat("Size", h);
         vBlurMat.setFloat("Scale", 0.666666f);
      }
   };
   vBlur.init(renderManager.getRenderer(), w, h, texFormat, Format.Depth, 1,
    vBlurMat);
   vBlur.getRenderedTexture().setMagFilter(Texture.MagFilter.Bilinear);        
//   vBlur.getRenderedTexture().setMinFilter(Texture.MinFilter.Trilinear);
   postRenderPasses.add(vBlur);

   return vBlur.getRenderedTexture();
} // gaussianBlur ==============================================================



/**
 * Reinitializes the filter by calling initFilter.
 */
// =============================================================================
   protected void reInitFilter()
// =============================================================================
{
   initFilter(assetManager, renderManager, viewPort, initialWidth, 
    initialHeight);
} // ===========================================================================
    

/**
 * Provides the premultiplication factor of the intensity equation.
 * 
 * @return 
 */
// =============================================================================
    public float getBloomFactor() {return bloomFactor;}
// =============================================================================
    

    
// =============================================================================
   @Override
   protected Material getMaterial() {return material;}
// =============================================================================

   

/**
 * Extracts the material's Glow techniques (if specified) and renders it into
 * the preGlowPass, if GlowMode is not Scene.
 * 
 * @param queue   The queue of the rendered scene.
 */
// =============================================================================
   @Override
   protected void postQueue(RenderQueue queue)
// =============================================================================
{  if (glowMode!=GlowMode.Scene)
   {  renderManager.getRenderer().setBackgroundColor(ColorRGBA.BlackNoAlpha);            
      renderManager.getRenderer().setFrameBuffer(
       preGlowPass.getRenderFrameBuffer());
      renderManager.getRenderer().clearBuffers(true, true, true);
      renderManager.setForcedTechnique("Glow");
      renderManager.renderViewPortQueues(viewPort, false);         
      renderManager.setForcedTechnique(null);
      renderManager.getRenderer().setFrameBuffer(
       viewPort.getOutputFrameBuffer());
   }
} // ===========================================================================


   
// =============================================================================
   @Override
   protected void cleanUpFilter(Renderer r)
// =============================================================================
{  if (glowMode!=GlowMode.Scene)
      preGlowPass.cleanup(r);
} // cleanUpFilter =============================================================

   
   
/**
 * Provides the power coefficient of the intensity equation.
 * @return  The power coefficient.
 */
// =============================================================================
   public float getBloomPower() {return bloomPower;}
// =============================================================================

   
   
/**
 * Sets weight factors for each mipmap by calculating the intensity equation
 * (see above).
 * 
 * @param bloomFactor  A constant premultiplication factor that should be
 *                     less than 1.
 * @param bloomPower   A greater value increases the bloom intensity of each
 *                     mipmap level.
 */
// =============================================================================
   public void setBloomIntensity(float bloomFactor, float bloomPower)
// =============================================================================
{  if (material!=null)
   {
      for (int ii=0; ii<numPasses; ii++)
      {
         float wt=bloomFactor*FastMath.pow(bloomPower, ii);
         material.setFloat("Weight"+(ii+1), wt);
      }
   }

   this.bloomFactor=bloomFactor;
   this.bloomPower=bloomPower;
} // setBloomIntensity =========================================================


/**
 * Provides the exposure cutoff.
 * @return  Exposure cutoff.
 */    
// =============================================================================
   public float getExposureCutOff() {return exposureCutOff;}
// =============================================================================

   

/**
 * Define the color threshold on which the bloom will be applied (0.0 to 1.0)
 * @param exposureCutOff   The color threshold value.
 */
// =============================================================================
   public void setExposureCutOff(float exposureCutOff)
// =============================================================================
{  this.exposureCutOff=exposureCutOff;
} // setExposureCutoff =========================================================



/**
 * Provides the exposure power of the intensity equation.
 * @return
 */
// =============================================================================
   public float getExposurePower() {return exposurePower;}
// =============================================================================

   
   
/**
 * Sets the exposure power value of the intensity equation (see above).
 * @param exposurePower
 */
// =============================================================================
   public void setExposurePower(float exposurePower)
// =============================================================================
{  this.exposurePower=exposurePower;
} // setExposurePower ==========================================================

   

/**
 * Sets the glow mode of the bloom filter.
 * @param glowMode   See above.
 */
// =============================================================================
   public void setGlowMode(GlowMode glowMode)
// =============================================================================
{  this.glowMode=glowMode;
   if (assetManager!=null)             // Dirty initialization check.
      reInitFilter();
} // setHighQuality ============================================================

   
   
/**
 * Sets the quality of the bloom filter.
 * @param enabled    <code>true</code> for <code>Quality.High</code>.
 */
// =============================================================================
   public void setHighQuality(boolean enabled)
// =============================================================================
{  this.quality=enabled? Quality.High:Quality.Low;
   if (assetManager!=null)             // Dirty initialization check.
      reInitFilter();
} // setHighQuality ============================================================
   
   
   
/**
 * Provides the downSampling coefficient of the mipmap levels.
 * @return  The downsampling coefficient.
 */
// =============================================================================
   public float getDownSamplingCoef() {return downSamplingCoef;}
// =============================================================================

   

/**
 * Sets the downsampling coefficient.
 * Each mipmap (starting with level 0) will have a reduced size defined by:
 * <code>mipmap_size=framebuffer_size/pow(downSamplingCoef^(level+1))</code>.
 * <p>
 * A lower value means less blur at higher computational cost, as the last level
 * will have a high resolution. The coefficient shall therefore be chosen in
 * such a way, that the highest (7th) level will have a resolution of just a few
 * pixels.
 * 
 * @param downSamplingCoef The downsampling coefficient.
 */
// =============================================================================
   public void setDownSamplingCoef(float downSamplingCoef)
// =============================================================================
{
   this.downSamplingCoef=downSamplingCoef;
   if (assetManager!=null) // dirty isInitialised check
      reInitFilter();
} // setDownSamplingCoef =======================================================



// =============================================================================
   @Override
   public void write(JmeExporter ex) throws IOException
// =============================================================================
{  super.write(ex);
   OutputCapsule oc=ex.getCapsule(this);
   oc.write(quality, "quality", Quality.High);
   oc.write(glowMode, "glowMode", GlowMode.Scene);
   oc.write(exposurePower, "exposurePower", 5.0f);
   oc.write(exposureCutOff, "exposureCutOff", 0.0f);
   oc.write(bloomFactor, "bloomFactor", 0.2f);
   oc.write(bloomPower, "bloomIntensity", 2.0f);
   oc.write(downSamplingCoef, "downSamplingFactor", 1);
} // write =====================================================================

   

// =============================================================================
   @Override
   public void read(JmeImporter im) throws IOException
// =============================================================================
{  super.read(im);
   InputCapsule ic=im.getCapsule(this);
   quality=ic.readEnum("quality", Quality.class, Quality.High);
   glowMode=ic.readEnum("glowMode", GlowMode.class, GlowMode.Scene);
   exposurePower=ic.readFloat("exposurePower", 5.0f);
   exposureCutOff=ic.readFloat("exposureCutOff", 0.0f);
   bloomFactor=ic.readFloat("bloomFactor", 0.2f);
   bloomPower=ic.readFloat("bloomIntensity", 2.0f);
   downSamplingCoef=ic.readFloat("downSamplingFactor", 1);
} // read ======================================================================

} // ***************************************************************************
