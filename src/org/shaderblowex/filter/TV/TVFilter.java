package org.shaderblowex.filter.TV;
 
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


public class TVFilter extends Filter {
    
      private boolean vhs=true;
       private boolean line=true;
       private boolean grain=true;
       private boolean scanline=true;
        private boolean vignette=true;

     
    
    /**
     * Creates a FFilter with default settings.
     */
    public TVFilter() {
        super("TVFilter");
    }
    
    /**
     *@param vhs default is true 
     * @param  line default is true 
     * @param  grain default true 
     * @param scanline  default true 
     * @param vignette  default true 
     
     */
     public TVFilter(  boolean vhs,boolean line,boolean grain,boolean scanline,boolean vignette) 
       {
        super("TVFilter");
         //
        this.vhs = vhs;
        this.line = line;
        this.grain = grain;
        this.scanline = scanline;
        this.vignette = vignette;
         
        }
     
      
    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "ShaderBlowEx/MatDefs/TV/TV.j3md");
         material.setBoolean("EnabledVHS", vhs);
         material.setBoolean("EnabledLine", line);
         material.setBoolean("EnabledGrain", grain);
         material.setBoolean("EnabledScanline", scanline);
         material.setBoolean("EnabledVignette", vignette);
        
       
    }

    @Override
    protected Material getMaterial() {
         return material;
    }
    
       
    
    public boolean isVhs() {
        return vhs;
    }

    public void setVhs(boolean vhs) {
          material.setBoolean("EnabledVHS", vhs);
        this.vhs = vhs;
    }

    public boolean isLine() {
        return line;
    }

    public void setLine(boolean line) {
         material.setBoolean("EnabledLine", line);
        this.line = line;
    }

    public boolean isGrain() {
        return grain;
    }

    public void setGrain(boolean grain) {
        material.setBoolean("EnabledGrain", grain);
        this.grain = grain;
    }

    public boolean isScanline() {
        return scanline;
    }

    public void setScanline(boolean scanline) {
         material.setBoolean("EnabledScanline", scanline);
        this.scanline = scanline;
    }

    public boolean isVignette() {
        return vignette;
    }

    public void setVignette(boolean vignette) {
          material.setBoolean("EnabledVignette", vignette);
        this.vignette = vignette;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(vhs, "vhs", true);
        oc.write(line, "line", true);
        oc.write(grain, "grain", true);
        oc.write(scanline, "scanline", true);
        oc.write(vignette, "vignette", true);
       
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        line = ic.readBoolean("line",true );
        line = ic.readBoolean("line",true );
        grain = ic.readBoolean("grain", true );
        scanline = ic.readBoolean("scanline", true );
        vignette = ic.readBoolean("vignette", true);
        
    }
  
 
 

}
