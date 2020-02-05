 
package  main.java.org.shaderblowex.test.RadialHaloGlow;

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
import org.shaderblowex.filter.RadialHaloGlow.RadialHaloGlowFilter;
 
 

/**
 *
 * @author xxx
 */
public class RadialHaloGlowFilterTest extends SimpleApplication  implements ActionListener {

  RadialHaloGlowFilter radialHaloGlow;
    
  BitmapText hintText;  
  BitmapText debugText; 
  
  
  float currentStrength=30.0f;
  float currentBrightness=0.5f;
   
  
 public   RadialHaloGlowFilterTest()
    {
        
    }
    
    @Override
    public void simpleInitApp() {
        
        //No stats
        setDisplayStatView(false);
        setDisplayFps(true);
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
        inputManager.addMapping("StrDec", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("StrInc", new KeyTrigger(KeyInput.KEY_2));
         inputManager.addMapping("BrDec", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("BrInc", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addListener(this, new String[]{"StrInc"});
        inputManager.addListener(this, new String[]{"StrDec"});
        inputManager.addListener(this, new String[]{"BrInc"});
        inputManager.addListener(this, new String[]{"BrDec"});
         
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.Red);
	hintText.setText("Strength:1/2 Brightness:3/4");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("Strength:"+currentStrength+" Brightness:"+currentBrightness );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
       
        //////////////////Filter//////////////////////
         radialHaloGlow=new RadialHaloGlowFilter( );
        
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(radialHaloGlow);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        RadialHaloGlowFilterTest app = new RadialHaloGlowFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
       
        if(name.equals("StrInc"))
        {
           currentStrength+=1.0f;   
           if(currentStrength>100.0)
               currentStrength=100.0f;
           refreshDisplay();
	    //
           radialHaloGlow.setStrength(currentStrength);
        }
        else  if(name.equals("StrDec"))
        {
           currentStrength-=1.0f;   
           if(currentStrength<0)
              currentStrength=0;
           refreshDisplay();
	    //
           radialHaloGlow.setStrength(currentStrength);
        }
            if(name.equals("BrInc"))
        {
           currentBrightness+=0.1f;   
           if(currentBrightness>1.0)
               currentBrightness=1.0f;
           refreshDisplay();
	    //
           radialHaloGlow.setBrightness(currentBrightness);
        }
        else  if(name.equals("BrDec"))
        {
           currentBrightness-=0.1f;   
           if(currentBrightness<0)
              currentBrightness=0;
           refreshDisplay();
	    //
           radialHaloGlow.setBrightness(currentBrightness);
        }
    }
void refreshDisplay()
  {
   debugText.setText("Strength:"+currentStrength+" Brightness:"+currentBrightness );
	  
  }    
    
}
