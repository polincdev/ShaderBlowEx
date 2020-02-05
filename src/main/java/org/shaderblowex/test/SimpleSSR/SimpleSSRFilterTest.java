 
package  main.java.org.shaderblowex.test.SimpleSSR;
 
 

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
import org.shaderblowex.filter.SimpleSSR.SimpleSSRFilter;
 
 

/**
 *
 * @author xxx
 */
public class SimpleSSRFilterTest extends SimpleApplication  implements ActionListener {

  SimpleSSRFilter simpleSSRFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
   
 float currentDownSample=2f;
 float currentStepLength=0.5f;
 int currentRayStepLength=32;
 float currentSigma= 5f;
 float currentReflectionFactor=0.5f;
  
 public   SimpleSSRFilterTest()
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
         inputManager.addMapping("SLDec", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("SLInc", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("RSDec", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("RSInc", new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("SigDec", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("SigInc", new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping("RFDec", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("RFInc", new KeyTrigger(KeyInput.KEY_0));
         inputManager.addListener(this, new String[]{"SLDec"});
         inputManager.addListener(this, new String[]{"SLInc"});
         inputManager.addListener(this, new String[]{"RSDec"});
         inputManager.addListener(this, new String[]{"RSInc"});
         inputManager.addListener(this, new String[]{"SigDec"});
         inputManager.addListener(this, new String[]{"SigInc"});
         inputManager.addListener(this, new String[]{"RFDec"});
         inputManager.addListener(this, new String[]{"RFInc"});
  
 
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.Red);
	hintText.setText("StpLen:3/4 RayStp:5/6 Sig:7/8 RefFac:9/0");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("StpLen:"+currentStepLength+" RayStp:"+currentRayStepLength+" Sig:"+currentSigma+" RefFac:"+currentReflectionFactor );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
      
        
        //////////////////Filter//////////////////////
         simpleSSRFilter=new SimpleSSRFilter();
         simpleSSRFilter.setDownSampleFactor(currentDownSample);
         simpleSSRFilter.setApproximateNormals(false);
         simpleSSRFilter.setFastBlur(true);
         simpleSSRFilter.setStepLength(currentStepLength);
         simpleSSRFilter.setRaySteps(currentRayStepLength);
         simpleSSRFilter.setSigma(currentSigma);
         simpleSSRFilter.setSampleNearby(false);
         simpleSSRFilter.setReflectionFactor(currentReflectionFactor);
        
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(simpleSSRFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        SimpleSSRFilterTest app = new SimpleSSRFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
        
       if(name.equals("SLInc"))
        {
           currentStepLength+=0.1f;   
           if(currentStepLength>10.0)
               currentStepLength=10.0f;
           refreshDisplay();
	    //
           simpleSSRFilter.setStepLength(currentStepLength);
        }
        else  if(name.equals("SLDec"))
        {
           currentStepLength-=0.1f;   
           if(currentStepLength<0)
              currentStepLength=0;
           refreshDisplay();
	    //
           simpleSSRFilter.setStepLength(currentStepLength);
        }
        else if(name.equals("RSInc"))
        {
           currentRayStepLength+=1;   
           if(currentRayStepLength>100.0)
               currentRayStepLength=100;
           refreshDisplay();
	    //
           simpleSSRFilter.setRaySteps(currentRayStepLength);
        }
        else  if(name.equals("RSDec"))
        {
           currentRayStepLength-=1f;   
           if(currentRayStepLength<0)
              currentRayStepLength=0;
           refreshDisplay();
	    //
           simpleSSRFilter.setRaySteps(currentRayStepLength);
        }
       else if(name.equals("SigInc"))
        {
           currentSigma+=0.1f;   
           if(currentSigma>10.0)
               currentSigma=10.0f;
           refreshDisplay();
	    //
           simpleSSRFilter.setSigma(currentSigma);
        }
        else  if(name.equals("SigDec"))
        {
           currentSigma-=0.1f;   
           if(currentSigma<0)
              currentSigma=0;
           refreshDisplay();
	    //
           simpleSSRFilter.setSigma(currentSigma);
        }
          else if(name.equals("RFInc"))
        {
           currentReflectionFactor+=0.1f;   
           if(currentReflectionFactor>10.0)
               currentReflectionFactor=10.0f;
           refreshDisplay();
	    //
           simpleSSRFilter.setReflectionFactor(currentReflectionFactor);
        }
        else  if(name.equals("RFDec"))
        {
           currentReflectionFactor-=0.1f;   
           if(currentReflectionFactor<0)
              currentReflectionFactor=0;
           refreshDisplay();
	    //
           simpleSSRFilter.setReflectionFactor(currentReflectionFactor);
        }
    }
void refreshDisplay()
  {
    debugText.setText("StpLen:"+currentStepLength+" RayStp:"+currentRayStepLength+" Sig:"+currentSigma+" RefFac:"+currentReflectionFactor );
	
	  
  }    
    
}
