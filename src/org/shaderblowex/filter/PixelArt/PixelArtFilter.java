package org.shaderblowex.filter.PixelArt;

 

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.post.Filter.Pass;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.Image.Format;

 
public class PixelArtFilter extends Filter {

    

   public static int  PALETTE_TYPE_LINEAR=0;
   public static int  PALETTE_TYPE_C64=1;
   public static int  PALETTE_TYPE_CGA=2;
   public static int  PALETTE_TYPE_CC=3;
   public static int  PALETTE_TYPE_ANSI=4;
   public static int  PALETTE_TYPE_STRIPE=5;
   public static int  PALETTE_TYPE_GRAYSCALE=6;
   public static int  PALETTE_TYPE_HEATMAP=7;
   public static int  PALETTE_TYPE_RAINBOW=8;
   public static int  PALETTE_TYPE_BRIGHTNESS=9;
   public static int  PALETTE_TYPE_DESERT=10;
   public static int  PALETTE_TYPE_ELECTRICAL=11;
   public static int  PALETTE_TYPE_NEON=12;
   public static int  PALETTE_TYPE_FIRE=13;
   public static int  PALETTE_TYPE_TECH=14;
   public static int  PALETTE_TYPE_HUE=15;
   public static int  PALETTE_TYPE_POSTERIZATION=16;
   public static int  PALETTE_TYPE_TOONIFICATION=17;
    
    
    private Pass normalPass;
    private float edgeWidth = 1.0f;
    private float edgeIntensity = 1.0f;
    private float normalThreshold = 0.5f;
    private float depthThreshold = 0.1f;
    private float normalSensitivity = 1.0f;
    private float depthSensitivity = 10.0f;
    private ColorRGBA edgeColor = new ColorRGBA(0, 0, 0, 1);
    private RenderManager renderManager;
    private ViewPort viewPort;
    private  int paletteType=PALETTE_TYPE_LINEAR;
    private float colorSize= 4.0f;
    private float colorCount=5.0f;
    private float pixelResolution=250;
 
    
    /**
     * Creates a CartoonEdgeFilter
     */
    public PixelArtFilter() {
        super("CartoonEdgeFilter");
    }

    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    protected void postQueue(RenderQueue queue) {
        Renderer r = renderManager.getRenderer();
        r.setFrameBuffer(normalPass.getRenderFrameBuffer());
        renderManager.getRenderer().clearBuffers(true, true, true);
        renderManager.setForcedTechnique("PreNormalPass");
        renderManager.renderViewPortQueues(viewPort, false);
        renderManager.setForcedTechnique(null);
        renderManager.getRenderer().setFrameBuffer(viewPort.getOutputFrameBuffer());
    }

    @Override
    protected Material getMaterial() {
        material.setTexture("NormalsTexture", normalPass.getRenderedTexture());
        return material;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        this.renderManager = renderManager;
        this.viewPort = vp;
        normalPass = new Pass();
        normalPass.init(renderManager.getRenderer(), w, h, Format.RGBA8, Format.Depth);
        material = new Material(manager, "ShaderBlowEx/MatDefs/PixelArt/PixelArt.j3md");
        material.setFloat("EdgeWidth", edgeWidth);
        material.setFloat("EdgeIntensity", edgeIntensity);
        material.setFloat("NormalThreshold", normalThreshold);
        material.setFloat("DepthThreshold", depthThreshold);
        material.setFloat("NormalSensitivity", normalSensitivity);
        material.setFloat("DepthSensitivity", depthSensitivity);
        material.setColor("EdgeColor", edgeColor);
          material.setInt("PaletteType", paletteType);
        material.setFloat("ColorSize", colorSize);
        material.setFloat("ColorCount", colorCount);
        material.setFloat("PixelResolution", pixelResolution);
      
      
    }

    @Override
    protected void cleanUpFilter(Renderer r) {
        normalPass.cleanup(r);
    }

    
    
    /**
     * Return the depth sensitivity<br>
     * for more details see {@link #setDepthSensitivity(float depthSensitivity)}
     * @return the depth sensitivity
     */
    public float getDepthSensitivity() {
        return depthSensitivity;
    }

    /**
     * sets the depth sensitivity<br>
     * defines how much depth will influence edges, default is 10
     * @param depthSensitivity 
     */
    public void setDepthSensitivity(float depthSensitivity) {
        this.depthSensitivity = depthSensitivity;
        if (material != null) {
            material.setFloat("DepthSensitivity", depthSensitivity);
        }
    }

    /**
     * returns the depth threshold<br>
     * for more details see {@link #setDepthThreshold(float depthThreshold)}
     * @return the threshold
     */
    public float getDepthThreshold() {
        return depthThreshold;
    }

    /**
     * sets the depth threshold<br>
     * Defines at what threshold of difference of depth an edge is outlined default is 0.1f
     * @param depthThreshold 
     */
    public void setDepthThreshold(float depthThreshold) {
        this.depthThreshold = depthThreshold;
        if (material != null) {
            material.setFloat("DepthThreshold", depthThreshold);
        }
    }

    /**
     * returns the edge intensity<br>
     * for more details see {@link #setEdgeIntensity(float edgeIntensity) }
     * @return the intensity
     */
    public float getEdgeIntensity() {
        return edgeIntensity;
    }

    /**
     * sets the edge intensity<br>
     * Defineshow visible will be the outlined edges
     * @param edgeIntensity 
     */
    public void setEdgeIntensity(float edgeIntensity) {
        this.edgeIntensity = edgeIntensity;
        if (material != null) {
            material.setFloat("EdgeIntensity", edgeIntensity);
        }
    }

    /**
     * returns the width of the edges
     * @return the width
     */
    public float getEdgeWidth() {
        return edgeWidth;
    }

    /**
     * sets the witdh of the edge in pixels default is 1
     * @param edgeWidth 
     */
    public void setEdgeWidth(float edgeWidth) {
        this.edgeWidth = edgeWidth;
        if (material != null) {
            material.setFloat("EdgeWidth", edgeWidth);
        }

    }

    /**
     * returns the normals sensitivity<br>
     * form more details see {@link #setNormalSensitivity(float normalSensitivity)}
     * @return the sensitivity
     */
    public float getNormalSensitivity() {
        return normalSensitivity;
    }

    /**
     * sets the normals sensitivity default is 1
     * @param normalSensitivity 
     */
    public void setNormalSensitivity(float normalSensitivity) {
        this.normalSensitivity = normalSensitivity;
        if (material != null) {
            material.setFloat("NormalSensitivity", normalSensitivity);
        }
    }

    /**
     * returns the normal threshold<br>
     * for more details see {@link #setNormalThreshold(float normalThreshold)}
     * 
     * @return the threshold
     */
    public float getNormalThreshold() {
        return normalThreshold;
    }

    /**
     * sets the normal threshold default is 0.5
     * @param normalThreshold 
     */
    public void setNormalThreshold(float normalThreshold) {
        this.normalThreshold = normalThreshold;
        if (material != null) {
            material.setFloat("NormalThreshold", normalThreshold);
        }
    }

    /**
     * returns the edge color
     * @return the pre-existing instance
     */
    public ColorRGBA getEdgeColor() {
        return edgeColor;
    }

    /**
     * Sets the edge color, default is black
     * @param edgeColor
     */
    public void setEdgeColor(ColorRGBA edgeColor) {
        this.edgeColor = edgeColor;
        if (material != null) {
            material.setColor("EdgeColor", edgeColor);
        }
    }
    
    public int getPaletteType() {
        return paletteType;
        
    }

    public void setPaletteType(int paletteType) {
        this.paletteType = paletteType;
         if (material != null)  
            material.setInt("PaletteType", paletteType);
    }

    public float getColorSize() {
        return colorSize;
    }

    public void setColorSize(float colorSize) {
        this.colorSize = colorSize;
         if (material != null)  
            material.setFloat("ColorSize", colorSize);
    }

    public float getColorCount() {
        return colorCount;
    }

    public void setColorCount(float colorCount) {
        this.colorCount = colorCount;
         if (material != null) 
            material.setFloat("ColorCount", colorCount);
    }

    public float getPixelResolution() {
        return pixelResolution;
    }

    public void setPixelResolution(float pixelResolution) {
        this.pixelResolution = pixelResolution;
         if (material != null)  
            material.setFloat("PixelResolution", pixelResolution);
    }
}