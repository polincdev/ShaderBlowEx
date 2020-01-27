 
package  test.LensFlare;
 
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
import main.java.org.shaderblowex.filter.LensFlare.LensFlareFilter;
 
 
public class LensFlareFilterTest extends SimpleApplication  implements ActionListener {

  LensFlareFilter lensFlareFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
   
  float currentSpacing=0.125f;
  float currentDistance=0.5f;
  float currentThreshold=0.2f;
   
 public   LensFlareFilterTest()
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
         inputManager.addMapping("StrInc", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("StrDec", new KeyTrigger(KeyInput.KEY_1));
       inputManager.addMapping("ThInc", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("ThDec", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("SamDec", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("SamInc", new KeyTrigger(KeyInput.KEY_6));
         inputManager.addListener(this, new String[]{"ThInc"});
        inputManager.addListener(this, new String[]{"ThDec"});
        inputManager.addListener(this, new String[]{"SamDec"});
        inputManager.addListener(this, new String[]{"SamInc"});
        inputManager.addListener(this, new String[]{"StrInc"});
        inputManager.addListener(this, new String[]{"StrDec"});
         
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.Red);
	hintText.setText("Spacing:1/2 Distance:3/4 Threshold: 5/6  ");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("Spacing:"+currentSpacing+" Distance:"+currentDistance +" Threshold:"+currentThreshold   );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
  
        //////////////////Filter//////////////////////
         lensFlareFilter   = new LensFlareFilter( ); 
        lensFlareFilter.setGhostSpacing(currentSpacing);
        lensFlareFilter.setHaloDistance(currentDistance);
        lensFlareFilter.setLightMapThreshold(currentThreshold);
        
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(lensFlareFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        LensFlareFilterTest app = new LensFlareFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        
        if(!isPressed)
            return;
        
       if(name.equals("StrInc"))
        {
           currentSpacing+=0.01f;   
            if(currentSpacing>0.5f)
              currentSpacing=0.5f;
           refreshDisplay();
	  //
           lensFlareFilter.setGhostSpacing(currentSpacing);
        }   
      else if(name.equals("StrDec"))
        {
           currentSpacing-=.01f;   
            if(currentSpacing<0)
              currentSpacing=0;
           refreshDisplay();
	  //
           lensFlareFilter.setGhostSpacing(currentSpacing);
        } 
        else if(name.equals("ThInc"))
        {
           currentDistance+=0.01f;   
               if(currentDistance>0.5f)
              currentDistance=0.5f;
           refreshDisplay();
	  //  
           lensFlareFilter.setHaloDistance(currentDistance);
        }   
        else if(name.equals("ThDec"))
        {
           currentDistance-=0.01f; 
             if(currentDistance<0)
              currentDistance=0;
           refreshDisplay();
	  //
           lensFlareFilter.setHaloDistance(currentDistance);
        }  
          else if(name.equals("SamInc"))
        {
           currentThreshold+=0.01f;
           if(currentThreshold>0.5f)
              currentThreshold=0.5f;
           refreshDisplay();
	  //  
           lensFlareFilter.setLightMapThreshold(currentThreshold);
        }   
        else if(name.equals("SamDec"))
        {
           currentThreshold-=0.01f; 
             if(currentThreshold<0)
              currentThreshold=0;
           refreshDisplay();
	  //
           lensFlareFilter.setLightMapThreshold(currentThreshold);
        }  
    }

void refreshDisplay()
  {
   debugText.setText("Spacing:"+currentSpacing+" Distance:"+currentDistance +" Threshold:"+currentThreshold   );
   }
}
