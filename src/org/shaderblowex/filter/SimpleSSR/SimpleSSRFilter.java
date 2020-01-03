/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shaderblowex.filter.SimpleSSR;
 
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.Image;
import java.util.ArrayList;

 
   
public class SimpleSSRFilter extends Filter{

    private Vector2f frustumNearFar;

    private RenderManager renderManager;
    private ViewPort viewPort;
    private Pass normalPass;
    private Pass ssrPass;
    private boolean approximateNormals = false;
    private boolean approximateGlossiness = true;

    private float downSampleFactor = 1f;

    private Material ssrMaterial;
    private boolean fastBlur = false;
    private int raySteps = 16;
    private boolean sampleNearby = true;
    private float stepLength = 1.0f;
    private float blurScale = 1f;
    private float sigma = 5f;
    private float reflectionFactor = 1f;
    private Vector2f nearFade = new Vector2f(0.01f, 1.0f);
    private Vector2f farFade = new Vector2f(200f, 300f);


    public SimpleSSRFilter(){
        super("SSR Filter");
    }

    @Override
    protected boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {

        this.renderManager = renderManager;
        this.viewPort = vp;
        int screenWidth = w;
        int screenHeight = h;
        postRenderPasses = new ArrayList<>();
 
        frustumNearFar = new Vector2f();

        float farY = (vp.getCamera().getFrustumTop() / vp.getCamera().getFrustumNear()) * vp.getCamera().getFrustumFar();
        float farX = farY * ((float) screenWidth / (float) screenHeight);
        frustumNearFar.x = vp.getCamera().getFrustumNear();
        frustumNearFar.y = vp.getCamera().getFrustumFar();


//        if(!approximateNormals){
            normalPass = new Pass();
            normalPass.init(renderManager.getRenderer(), (int) (screenWidth / downSampleFactor), (int) (screenHeight / downSampleFactor), Image.Format.RGBA8, Image.Format.Depth);
//        }

        ssrMaterial = new Material(manager, "ShaderBlowEx/MatDefs/SimpleSSR/SimpleSSR.j3md"); 
//        if(!approximateNormals){
            ssrMaterial.setTexture("Normals", normalPass.getRenderedTexture());
//        }
        ssrMaterial.setInt("RaySamples", raySteps);
        ssrMaterial.setInt("NearbySamples", sampleNearby ? 4 : 0);
        ssrMaterial.setFloat("StepLength", stepLength);
        ssrMaterial.setFloat("ReflectionFactor", reflectionFactor);
        ssrMaterial.setBoolean("ApproximateNormals", approximateNormals);
        ssrMaterial.setVector2("NearReflectionsFade", nearFade);
        ssrMaterial.setVector2("FarReflectionsFade", farFade);

        ssrPass = new Pass("SSR pass") {

            @Override
            public boolean requiresDepthAsTexture() {
                return true;
            }

            @Override
            public boolean requiresSceneAsTexture() {
                return true; //To change body of generated methods, choose Tools | Templates.
            }


        };

        ssrPass.init(renderManager.getRenderer(), (int) (screenWidth / downSampleFactor), (int) (screenHeight / downSampleFactor), Image.Format.RGBA8, Image.Format.Depth, 1, ssrMaterial);
       
        postRenderPasses.add(ssrPass);
 
        material = new Material(manager, "ShaderBlowEx/MatDefs/SimpleSSR/SimpleSSRBlur.j3md"); 
        material.setTexture("SSR", ssrPass.getRenderedTexture());
        material.setBoolean("Horizontal", true);
        material.setBoolean("FastBlur", fastBlur);
        material.setFloat("BlurScale", blurScale);
        material.setFloat("Sigma", sigma);

}

    @Override
    protected void postQueue(RenderQueue queue) {
        if(!approximateNormals) {
            Renderer r = renderManager.getRenderer();
            r.setFrameBuffer(normalPass.getRenderFrameBuffer());
            renderManager.getRenderer().clearBuffers(true, true, true);
            renderManager.setForcedTechnique("PreNormalPass");
        
     renderManager.renderViewPortQueues(viewPort, false);
            renderManager.setForcedTechnique(null);
            renderManager.getRenderer().setFrameBuffer(viewPort.getOutputFrameBuffer());
        }
    }

    @Override
    protected void cleanUpFilter(Renderer r) {
        normalPass.cleanup(r);
    }    

    @Override
    protected Material getMaterial() {
        return material;
    }

    /**
     * If true, any passed normals won't be used. Instead they will be approximated using the depth map
     * @param approximateNormals 
     */
    public void setApproximateNormals(boolean approximateNormals) {
        this.approximateNormals = approximateNormals;
        if(ssrMaterial != null){
            ssrMaterial.setBoolean("ApproximateNormals", approximateNormals);
        }
    }

    /**
     * Value to scale (down) the textures the filter uses.
     * @param downSampleFactor 
     */
    public void setDownSampleFactor(float downSampleFactor) {
        this.downSampleFactor = downSampleFactor;
    }

    /**
     * Using a faster blur function in the blur pass. Default false
     * @param fastBlur 
     */
    public void setFastBlur(boolean fastBlur) {
        this.fastBlur = fastBlur;
        if(material != null){
            material.setBoolean("FastBlur", fastBlur);
        }
    }

    /**
     * Amount of steps
     * @param raySteps 
     */
    public void setRaySteps(int raySteps) {
        this.raySteps = raySteps;
        if(ssrMaterial != null){
            ssrMaterial.setInt("RaySamples", raySteps);
        }
    }

    /**
     * Whether to sample nearby ray hits for better accuracy
     * @param sampleNearby 
     */
    public void setSampleNearby(boolean sampleNearby) {
        this.sampleNearby = sampleNearby;
        if(ssrMaterial != null){
            ssrMaterial.setInt("NearbySamples", sampleNearby ? 4 : 0);
        }
    }

    /**
     * Length of each ray step
     * @param stepLength 
     */
    public void setStepLength(float stepLength) {
        this.stepLength = stepLength;
        if(ssrMaterial != null){
            ssrMaterial.setFloat("StepLength", stepLength);
        }
    }

    /**
     * Scale for blur. Only used if fastBlur is true
     * @param blurScale 
     */
    public void setBlurScale(float blurScale) {
        this.blurScale = blurScale;
        if(material != null){
            material.setFloat("BlurScale", blurScale);
        }
    }

    /**
     * Sigma for regular gaussian blur. Only used if fastBlur is false
     * @param sigma 
     */
    public void setSigma(float sigma) {
        this.sigma = sigma;
        if(material != null){
            material.setFloat("Sigma", sigma);
        }
    }

    public float getReflectionFactor() {
        return reflectionFactor;
    }

    /**
     * Sets the overall reflection amount. Scales between 0 and 1
     * @param reflectionFactor 
     */
    public void setReflectionFactor(float reflectionFactor) {
        this.reflectionFactor = reflectionFactor;
        if(ssrMaterial != null){
            ssrMaterial.setFloat("ReflectionFactor", reflectionFactor);
        }
    }



    public boolean isApproximateGlossiness() {
        return approximateGlossiness;
    }

    public void setApproximateGlossiness(boolean approximateGlossiness) {
        this.approximateGlossiness = approximateGlossiness;
        if(ssrMaterial != null){
            ssrMaterial.setBoolean("ApproximateGlossiness", approximateGlossiness);
        }
    }

    public Vector2f getNearFade() {
        return nearFade;
    }

    public void setNearFade(Vector2f nearFade) {
        this.nearFade = nearFade;
        if(ssrMaterial != null){
            ssrMaterial.setVector2("NearReflectionsFade", nearFade);
        }
    }

    public Vector2f getFarFade() {
        return farFade;
    }

    public void setFarFade(Vector2f farFade) {
        this.farFade = farFade;
        if(ssrMaterial != null){
            ssrMaterial.setVector2("FarReflectionsFade", farFade);
        }
    }


}