/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shaderblowex.filter.BetterDepthOfField;
 
 

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
 

 
public class BetterDepthOfFieldFilter extends Filter {
    
     private float focalDepth = 0.5f;
      private float focalLength= 75.0f;
       private float fStop =16.0f;
      private boolean autoFocus= true;
      private boolean showFocus = false;
     

    public BetterDepthOfFieldFilter() {
    }

    @Override
    public boolean isRequiresSceneTexture() {
        return true;
    }

    @Override
    public boolean isRequiresDepthTexture() {
        return true;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/BetterDepthOfField/BetterDepthOfField.j3md");
        material.setFloat("FocalDepth", focalDepth);
        material.setFloat("FocalLength", focalLength);
        material.setFloat("Fstop", fStop);
        material.setFloat("Width", w);
        material.setFloat("Height", h);
        material.setVector2("FrustumNearFar", new Vector2f(vp.getCamera().getFrustumNear(), vp.getCamera().getFrustumFar()));
        material.setBoolean("AutoFocus", autoFocus);
        material.setBoolean("ShowFocus", showFocus);
    }

    @Override
    protected Material getMaterial() {
        return material;
    }
    
     public float getFocalDepth() {
     
        return focalDepth;
    }

    public void setFocalDepth(float focalDepth) {
        checkFloatArgument(focalDepth, 0.0f,  5.0f, "FocalDepth"); 
        this.focalDepth = focalDepth;
        if (material != null)
        material.setFloat("FocalDepth", focalDepth);
    }

    public float getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(float focalLength) {
         checkFloatArgument(focalLength, 0.0f,  150.0f, "FocalLength"); 
        this.focalLength = focalLength;
        if (material != null)
         material.setFloat("FocalLength", focalLength);
    }

    public float getfStop() {
        return fStop;
    }

    public void setfStop(float fStop) {
        checkFloatArgument(fStop, 0.0f,   50.0f, "Fstop"); 
        this.fStop = fStop;
        if (material != null)
        material.setFloat("Fstop", fStop);
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
        if (material != null)
          material.setBoolean("AutoFocus", autoFocus);
    }

    public boolean isShowFocus() {
        return showFocus;
    }

    public void setShowFocus(boolean showFocus) {
        this.showFocus = showFocus;
        if (material != null)
         material.setBoolean("ShowFocus", showFocus);
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