package org.shaderblowex.filter.RadialHaloGlow;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.image.ImageRaster;
import java.io.IOException;
 
public class RadialHaloGlow extends Filter {
 
private Material material;
private float strength=50;
private float brightness=0.5f;

 
public RadialHaloGlow() {
super("RadialHaloGlow");
}

@Override
protected void initFilter(AssetManager assetManager, RenderManager arg1, ViewPort arg2, int w, int h) {
 
material = new Material(assetManager, "ShaderBlowEx/MatDefs/RadialHaloGlow/RadialHaloGlow.j3md");
material.setFloat("Brightness", brightness);
material.setFloat("Strength", strength);
 
this.material=material;
}

@Override
protected Material getMaterial() {
return material;
}
    

public void setStrength(float newStrength) {
   strength=checkFloatArgument(newStrength, 0.0f, 100.0f );
   //
  material.setFloat("Strength", strength);
}
public void setBrightness(float newStrength) {
   brightness=checkFloatArgument(newStrength, 0.0f, 1.0f );
   //
  material.setFloat("Brightness", brightness);
}

 public float getStrength() {
        return strength;
    }

    public float getBrightness() {
        return brightness;
    }

 
  private   float checkFloatArgument(float value, float min, float max ) {
    if (value < min  ) 
             return min;
       else if(value > max  )
           return max;
    else
           return value;
    }
@Override
public void write(JmeExporter ex) throws  IOException {
super.write(ex);
OutputCapsule oc = ex.getCapsule(this);
 
}

@Override
public void read(JmeImporter im) throws IOException {
super.read(im);
InputCapsule ic = im.getCapsule(this);
 
}
}