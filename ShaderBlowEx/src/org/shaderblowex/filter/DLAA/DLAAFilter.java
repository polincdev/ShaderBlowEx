 
package org.shaderblowex.filter.DLAA;

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

 
public class DLAAFilter extends Filter {
 
  
    /**
     * Creates a DLAA antialiasing . 
      
     */
    public DLAAFilter(  ) {
      super("DLAAFilter");
   
    }
     

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/NFAA/NFAA.j3md");
        
   
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

     
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
         
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
         
        
    }
 
 
}