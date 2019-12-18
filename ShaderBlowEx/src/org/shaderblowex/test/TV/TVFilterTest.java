 
package org.shaderblowex.test.TV;

 
import org.shaderblowex.filter.TV.*;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import org.shaderblowex.filter.BetterColorCorrection.BetterColorCorrectionFilter;
 

/**
 *
 * @author xxx
 */
public class TVFilterTest extends SimpleApplication  implements ActionListener {

  TVFilter tVFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
   
  boolean currentVhs=true;
   boolean currentLine=true;
   boolean currentGrain=true;
   boolean currentScanline=true;
   boolean currentVignette=true;    
   
 public   TVFilterTest()
    {
        
    }
    
    @Override
    public void simpleInitApp() {
        
        //No stats
        setDisplayStatView(false);
        setDisplayFps(true);
        //Background color
        viewPort.setBackgroundColor(ColorRGBA.Gray);
        //faster cam
        cam.setLocation(cam.getLocation().addLocal(0, 2f, 0));
        flyCam.setMoveSpeed(2.0f);
        //2D  reference image
         Material    geoMat = new Material(this.getAssetManager(),  "Common/MatDefs/Misc/Unshaded.j3md");
        geoMat.setTexture("ColorMap", assetManager.loadTexture("ShaderBlowEx/Textures/test.png")); 
       
        
        
         //Scene
         Spatial scene= assetManager.loadModel("ShaderBlowEx/Models/testScene.j3o");
         Node sceneAsNode=((Node)((Node)scene).getChild("Scene"));
         rootNode.attachChild(sceneAsNode);
         //Tex for the ground for comparison
         sceneAsNode.getChild("Plane").setMaterial(geoMat);
         
        //Light
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f( -.5f, -.5f, -.5f).normalizeLocal());
        sceneAsNode.addLight(sun);
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.1f));
        sceneAsNode.addLight(al);
        
        
        //Keys
        inputManager.addMapping("VhsEn", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("LineEn", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("GrainEn", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("ScEn", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("VinEn", new KeyTrigger(KeyInput.KEY_5));
        
        inputManager.addListener(this, new String[]{"VhsEn"});
        inputManager.addListener(this, new String[]{"LineEn"});
        inputManager.addListener(this, new String[]{"GrainEn"});
        inputManager.addListener(this, new String[]{"ScEn"});
        inputManager.addListener(this, new String[]{"VinEn"});
       
 
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.Red);
	hintText.setText("VHS:1 Line:2 Grain:3 Scanline:4 Vignette: 5");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("VHS:"+currentVhs+" Line:"+currentLine+" Grain:"+currentGrain+" Scanline:"+currentScanline+" Vignette:"+currentVignette );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
        
        //////////////////Filter//////////////////////
        tVFilter=new TVFilter(currentVhs, currentLine,currentGrain, currentScanline,currentVignette ); 
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(tVFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        TVFilterTest app = new TVFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
         
     
        if(name.equals("VhsEn"))
            {
             currentVhs=!currentVhs;   
             refreshDisplay();
             tVFilter.setVhs(currentVhs);
            }
        else if(name.equals("LineEn"))
            {
             currentLine=!currentLine;   
             refreshDisplay();
             tVFilter.setLine(currentLine);
            }
          else if(name.equals("GrainEn"))
            {
             currentGrain=!currentGrain;   
             refreshDisplay();
             tVFilter.setGrain(currentGrain);
            }
            else if(name.equals("ScEn"))
            {
             currentScanline=!currentScanline;   
             refreshDisplay();
             tVFilter.setScanline(currentScanline);
            }
         else if(name.equals("VinEn"))
            {
             currentVignette=!currentVignette;   
             refreshDisplay();
             tVFilter.setVignette(currentVignette);
            }
    }
    
 void refreshDisplay()
    {
     debugText.setText("VHS:"+currentVhs+" Line:"+currentLine+" Grain:"+currentGrain+" Scanline:"+currentScanline+" Vignette:"+currentVignette );
    }
            
            
                
}
