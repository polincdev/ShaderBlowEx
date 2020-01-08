 
package  test.SimpleBloom;

 
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
import org.shaderblowex.filter.SimpleBloom.SimpleBloomFilter;

/**
 *
 * @author xxx
 */
public class SimpleBloomFilterTest extends SimpleApplication  implements ActionListener {

  SimpleBloomFilter simpleBloomFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
   
  float currentStrength=0.5f;
  float currentSize=3.0f;
  int currentSamples=15;
   
 public   SimpleBloomFilterTest()
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
	hintText.setText("Strength:1/2 Size:3/4 Samples: 5/6  ");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("Strength:"+currentStrength+" Size:"+currentSize +" Samples:"+currentSamples   );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
  
        //////////////////Filter//////////////////////
         simpleBloomFilter=new SimpleBloomFilter( currentStrength,currentSize,currentSamples);
        
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(simpleBloomFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        SimpleBloomFilterTest app = new SimpleBloomFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
       
       
         if(name.equals("StrInc"))
        {
           currentStrength+=0.1;   
           refreshDisplay();
	  //
           simpleBloomFilter.setStrength(currentStrength);
        }   
      else if(name.equals("StrDec"))
        {
           currentStrength-=0.1;   
            if(currentStrength<0)
              currentStrength=0;
           refreshDisplay();
	  //
           simpleBloomFilter.setStrength(currentStrength);
        } 
        else if(name.equals("ThInc"))
        {
           currentSize+=0.1;   
               if(currentSize>5)
              currentSize=5;
           refreshDisplay();
	  //  
           simpleBloomFilter.setSize(currentSize);
        }   
        else if(name.equals("ThDec"))
        {
           currentSize-=0.1; 
             if(currentSize<0)
              currentSize=0;
           refreshDisplay();
	  //
           simpleBloomFilter.setSize(currentSize);
        }  
          else if(name.equals("SamInc"))
        {
           currentSamples+=1;
           if(currentSamples>20)
              currentSamples=20;
           refreshDisplay();
	  //  
           simpleBloomFilter.setSamples(currentSamples);
        }   
        else if(name.equals("SamDec"))
        {
           currentSamples-=1.0; 
             if(currentSamples<0)
              currentSamples=0;
           refreshDisplay();
	  //
           simpleBloomFilter.setSamples(currentSamples);
        }  
    }

void refreshDisplay()
  {
   debugText.setText("Strength:"+currentStrength+" Size:"+currentSize +" Samples:"+currentSamples );
  }
}
