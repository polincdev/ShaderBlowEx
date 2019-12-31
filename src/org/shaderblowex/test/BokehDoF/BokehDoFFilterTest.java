 
package org.shaderblowex.test.BokehDoF;

 
import org.shaderblowex.test.BetterToneMap.*;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.shaderblowex.filter.BokehDoF.BokehDoFFilter;

/**
 *
 * @author xxx
 */
public class BokehDoFFilterTest extends SimpleApplication  implements ActionListener {

  BokehDoFFilter bokehDoFFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
   
  float currentFocusPoint=8.0f;
  float currentRadiusScale=0.5f;
  float currentBlurSize=3.0f;
  float currentFocusScale=5.0f;
    
  
 public   BokehDoFFilterTest()
    {
        
    }
    
    @Override
    public void simpleInitApp() {
        
        //No stats
        setDisplayStatView(false);
        //setDisplayFps(false);
        //faster cam
        cam.setLocation(cam.getLocation().addLocal(0, 2f, 0));
        flyCam.setMoveSpeed(2.0f);
        //Background color
        viewPort.setBackgroundColor(ColorRGBA.Gray);
        //Test  image
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
        inputManager.addMapping("FocPntDec", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("FocPntInc", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("RdScaDec", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("RdScaInc", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("BrlSizeDec", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("BrlSizeInc", new KeyTrigger(KeyInput.KEY_6));
         inputManager.addMapping("FocScaDec", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("FocScaInc", new KeyTrigger(KeyInput.KEY_8));
        inputManager.addListener(this, new String[]{"FocPntDec"});
        inputManager.addListener(this, new String[]{"FocPntInc"});
        inputManager.addListener(this, new String[]{"RdScaDec"});
        inputManager.addListener(this, new String[]{"RdScaInc"});
        inputManager.addListener(this, new String[]{"BrlSizeDec"});
        inputManager.addListener(this, new String[]{"BrlSizeInc"});
        inputManager.addListener(this, new String[]{"FocScaDec"});
        inputManager.addListener(this, new String[]{"FocScaInc"});
        
        
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.Red);
	hintText.setText("FocusPoint:1/2 RadiusScale:3/4 BlurSize:5/6 FocusScale:7/8" );
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("FocusPoint:"+currentFocusPoint+" RadiusScale:"+currentRadiusScale+" BlurSize:"+currentBlurSize+" FocusScale:"+currentFocusScale  );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
   
        //////////////////Filter//////////////////////
         bokehDoFFilter=new BokehDoFFilter( );
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(bokehDoFFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        BokehDoFFilterTest app = new BokehDoFFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
         
        if(name.equals("FocPntInc"))
        {
           currentFocusPoint++;   
            refreshDisplay();
	    //
           bokehDoFFilter.setFocusPoint(currentFocusPoint);
        }
        else  if(name.equals("FocPntDec"))
        {
           currentFocusPoint--;   
           if(currentFocusPoint<0)
              currentFocusPoint=0;
            refreshDisplay();
	    //
          bokehDoFFilter.setFocusPoint(currentFocusPoint);
        }
        else if(name.equals("RdScaInc"))
        {
           currentRadiusScale+=0.1;   
           if(currentRadiusScale>1.0)
               currentRadiusScale=1.0f;
           refreshDisplay(); 
	  //
           bokehDoFFilter.setRadiusScale(currentRadiusScale);
        }   
      else if(name.equals("RdScaDec"))
        {
           currentRadiusScale-=0.1;   
            if(currentRadiusScale<0)
              currentRadiusScale=0;
          refreshDisplay(); 
	  //
           bokehDoFFilter.setRadiusScale(currentRadiusScale);
        } 
        else if(name.equals("BrlSizeInc"))
        {
           currentBlurSize+=1.0;   
           refreshDisplay();
           //  
           bokehDoFFilter.setBlurSize(currentBlurSize);
        }   
        else if(name.equals("BrlSizeDec"))
        {
          currentBlurSize-=1.0f; 
          if(currentBlurSize<0)
              currentBlurSize=0;
           refreshDisplay();
	  //
           bokehDoFFilter.setBlurSize(currentBlurSize);
        }   
          if(name.equals("FocScaInc"))
        {
           currentFocusScale++;   
            refreshDisplay();
	    //
           bokehDoFFilter.setFocusScale(currentFocusScale);
        }
        else  if(name.equals("FocScaDec"))
        {
           currentFocusScale--;   
           if(currentFocusScale<0)
              currentFocusScale=0;
            refreshDisplay();
	    //
          bokehDoFFilter.setFocusScale(currentFocusScale);
        }
        
        
        
    }
    
 void refreshDisplay()
   {
    debugText.setText("FocusPoint:"+currentFocusPoint+" RadiusScale:"+currentRadiusScale+" BlurSize:"+currentBlurSize+" FocusScale:"+currentFocusScale  );
    }
 
}
