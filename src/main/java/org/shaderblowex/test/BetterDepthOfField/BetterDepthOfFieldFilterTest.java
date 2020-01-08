 
package main.java.org.shaderblowex.test.BetterDepthOfField;


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
import org.shaderblowex.filter.BetterDepthOfField.BetterDepthOfFieldFilter;

/**
 *
 * @author xxx
 */
public class BetterDepthOfFieldFilterTest extends SimpleApplication  implements ActionListener {

  BetterDepthOfFieldFilter betterDepthOfFieldFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
   
  float currentFocalDepth=0.5f;
  float currentFocalLength=75f;
  float currentFStop=16.0f;
  boolean currentAutoFocus=true;
  boolean currentShowFocus=false;
     
  
 public   BetterDepthOfFieldFilterTest()
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
        inputManager.addMapping("FocDepDec", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("FocDepInc", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("FocLenDec", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("FocLenInc", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("FStopDec", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("FStopInc", new KeyTrigger(KeyInput.KEY_6));
         inputManager.addMapping("AutoFocDec", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("AutoFocInc", new KeyTrigger(KeyInput.KEY_8));
         inputManager.addMapping("ShowFocDec", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("ShowFocInc", new KeyTrigger(KeyInput.KEY_0));
        inputManager.addListener(this, new String[]{"FocDepDec"});
        inputManager.addListener(this, new String[]{"FocDepInc"});
        inputManager.addListener(this, new String[]{"FocLenDec"});
        inputManager.addListener(this, new String[]{"FocLenInc"});
        inputManager.addListener(this, new String[]{"FStopDec"});
        inputManager.addListener(this, new String[]{"FStopInc"});
        inputManager.addListener(this, new String[]{"AutoFocDec"});
        inputManager.addListener(this, new String[]{"AutoFocInc"});
        inputManager.addListener(this, new String[]{"ShowFocDec"});
        inputManager.addListener(this, new String[]{"ShowFocInc"});
        
        
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.White);
	hintText.setText("FocDepth:1/2 FocLength:3/4 FStop:5/6 AutoFocus:7/8 ShowFocus:9/0" );
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.Red);
	debugText.setText("FocDepth:"+currentFocalDepth+" FocLength:"+currentFocalLength+" FStop:"+currentFStop+" AutoFocus:"+currentAutoFocus+" ShowFocus:"+currentShowFocus   );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
      
     
        //////////////////Filter//////////////////////
         betterDepthOfFieldFilter=new BetterDepthOfFieldFilter( );
        
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(betterDepthOfFieldFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        BetterDepthOfFieldFilterTest app = new BetterDepthOfFieldFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
       
        if(name.equals("FocDepInc"))
        {
            currentFocalDepth+=0.1;   
            
            refreshDisplay();
	    //
           betterDepthOfFieldFilter.setFocalDepth(currentFocalDepth);
        }
        else  if(name.equals("FocDepDec"))
        {
          currentFocalDepth-=0.1;   
            if(currentFocalDepth<0)
              currentFocalDepth=0;
            refreshDisplay();
	    //
          betterDepthOfFieldFilter.setFocalDepth(currentFocalDepth);
        }
        else if(name.equals("FocLenInc"))
        {
           currentFocalLength++;   
           refreshDisplay(); 
	  //
           betterDepthOfFieldFilter.setFocalLength(currentFocalLength);
        }   
      else if(name.equals("FocLenDec"))
        {
           currentFocalLength-=1.0;   
            if(currentFocalLength<0)
              currentFocalLength=0;
          refreshDisplay(); 
	  //
           betterDepthOfFieldFilter.setFocalLength(currentFocalLength);
        } 
        else if(name.equals("FStopInc"))
        {
           currentFStop+=1.0;   
           refreshDisplay();
           //  
           betterDepthOfFieldFilter.setfStop(currentFStop);
        }   
        else if(name.equals("FStopDec"))
        {
          currentFStop-=1.0f; 
          if(currentFStop<0)
              currentFStop=0;
           refreshDisplay();
	  //
           betterDepthOfFieldFilter.setfStop(currentFStop);
        }   
      else if(name.equals("AutoFocInc"))
        {
            
          if(currentAutoFocus)
            currentAutoFocus=false;   
            refreshDisplay();
	    //
           betterDepthOfFieldFilter.setAutoFocus(currentAutoFocus);
        }
        else  if(name.equals("AutoFocDec"))
        {
          if(!currentAutoFocus)
            currentAutoFocus=true;   
             refreshDisplay();
	    //
           betterDepthOfFieldFilter.setAutoFocus(currentAutoFocus);
        }
      else if(name.equals("AutoFocInc"))
        {
            
          if(currentAutoFocus)
            currentAutoFocus=false;   
            refreshDisplay();
	    //
           betterDepthOfFieldFilter.setAutoFocus(currentAutoFocus);
        }
        else  if(name.equals("AutoFocDec"))
        {
          if(!currentAutoFocus)
            currentAutoFocus=true;   
             refreshDisplay();
	    //
           betterDepthOfFieldFilter.setAutoFocus(currentAutoFocus);
        }
         else if(name.equals("ShowFocInc"))
        {
            
          if(currentShowFocus)
            currentShowFocus=false;   
            refreshDisplay();
	    //
           betterDepthOfFieldFilter.setShowFocus(currentShowFocus);
        }
        else  if(name.equals("ShowFocDec"))
        {
          if(!currentShowFocus)
            currentShowFocus=true;   
             refreshDisplay();
	    //
           betterDepthOfFieldFilter.setShowFocus(currentShowFocus);
        }
        
    }
    
 void refreshDisplay()
   {
    debugText.setText("FocDepth:"+currentFocalDepth+" FocLength:"+currentFocalLength+" FStop:"+currentFStop+" AutoFocus:"+currentAutoFocus+" ShowFocus:"+currentShowFocus   );
   }
 
}
