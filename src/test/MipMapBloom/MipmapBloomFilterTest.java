 
package  test.MipMapBloom;

  
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
import org.shaderblowex.filter.MipMapBloom.MipmapBloomFilter;
 

/**
 *
 * @author xxx
 */
public class MipmapBloomFilterTest extends SimpleApplication  implements ActionListener {

  MipmapBloomFilter mipmapBloomFilter;
     
  BitmapText hintText;  
  BitmapText debugText; 
    
  
   float currentExposurePower=5.0f;
   float currentExposureCutOff=0.0f;
   float currentBloomFactor=1.5f;
   float currentBloomPower=0.5f;
   float currentDownSamplingCoef=2.0f;
  
  
 public   MipmapBloomFilterTest()
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
        Material  geoMat = new Material(this.getAssetManager(),  "Common/MatDefs/Misc/Unshaded.j3md");
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
        inputManager.addMapping("BlmPrwDec", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("BlmPrwInc", new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping("DwnSamDec", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("DwnSamInc", new KeyTrigger(KeyInput.KEY_0));
        
        inputManager.addListener(this, new String[]{"ThInc"});
        inputManager.addListener(this, new String[]{"ThDec"});
        inputManager.addListener(this, new String[]{"SamDec"});
        inputManager.addListener(this, new String[]{"SamInc"});
        inputManager.addListener(this, new String[]{"StrInc"});
        inputManager.addListener(this, new String[]{"StrDec"});
        inputManager.addListener(this, new String[]{"BlmPrwDec"});
        inputManager.addListener(this, new String[]{"BlmPrwInc"});
        inputManager.addListener(this, new String[]{"DwnSamDec"});
        inputManager.addListener(this, new String[]{"DwnSamInc"});
      
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.Red);
	hintText.setText("ExposPwr:1/2 ExposCutOff:3/4 BlmFac:5/6  BlmPwr:7/8 DwnSamp:9/0");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
         
        
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("ExposPwr:"+currentExposurePower+" ExposCutOff:"+currentExposureCutOff+" BlmFac:"+currentBloomFactor+"  BlmPwr:"+currentBloomPower+" DwnSamp:"+currentDownSamplingCoef );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
  
        //////////////////Filter//////////////////////
         mipmapBloomFilter=new MipmapBloomFilter(  );
        
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(mipmapBloomFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        MipmapBloomFilterTest app = new MipmapBloomFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
       
      
         if(name.equals("StrInc"))
        {
           currentExposurePower+=0.1;   
           refreshDisplay();
	  //
           mipmapBloomFilter.setExposurePower(currentExposurePower);
        }   
      else if(name.equals("StrDec"))
        {
           currentExposurePower-=0.1;   
            if(currentExposurePower<0)
              currentExposurePower=0;
           refreshDisplay();
	  //
            mipmapBloomFilter.setExposurePower(currentExposurePower);
        } 
        else if(name.equals("ThInc"))
        {
           currentExposureCutOff+=0.1;   
               if(currentExposureCutOff>5)
              currentExposureCutOff=5;
           refreshDisplay();
	  //  
           mipmapBloomFilter.setExposureCutOff(currentExposureCutOff);
        }   
        else if(name.equals("ThDec"))
        {
           currentExposureCutOff-=0.1; 
             if(currentExposureCutOff<0)
              currentExposureCutOff=0;
           refreshDisplay();
	  //
           mipmapBloomFilter.setExposureCutOff(currentExposureCutOff);
        }  
     else if(name.equals("SamInc"))
        {
           currentBloomFactor+=0.1;
            refreshDisplay();
	  //  
           mipmapBloomFilter.setBloomIntensity(currentBloomFactor,currentBloomPower );
        }   
        else if(name.equals("SamDec"))
        {
           currentBloomFactor-=0.1; 
             if(currentBloomFactor<0)
              currentBloomFactor=0;
           refreshDisplay();
	  //
           mipmapBloomFilter.setBloomIntensity(currentBloomFactor,currentBloomPower );
        }  
         
      else if(name.equals("BlmPrwInc"))
        {
           currentBloomFactor+=0.1;
            refreshDisplay();
	  //  
           mipmapBloomFilter.setBloomIntensity(currentBloomFactor,currentBloomPower );
        }   
        else if(name.equals("BlmPrwDec"))
        {
           currentBloomFactor-=0.1; 
             if(currentBloomFactor<0)
              currentBloomFactor=0;
           refreshDisplay();
	  //
           mipmapBloomFilter.setBloomIntensity(currentBloomFactor,currentBloomPower );
        }  
          else if(name.equals("DwnSamInc"))
        {
           currentDownSamplingCoef+=0.1;
            refreshDisplay();
	  //  
           mipmapBloomFilter.setDownSamplingCoef(currentDownSamplingCoef);
        }   
        else if(name.equals("DwnSamDec"))
        {
           currentDownSamplingCoef-=0.1; 
             if(currentDownSamplingCoef<0)
              currentDownSamplingCoef=0;
           refreshDisplay();
	  //
           mipmapBloomFilter.setDownSamplingCoef(currentDownSamplingCoef);
        }  
    }

void refreshDisplay()
  {
debugText.setText("ExposPwr:"+currentExposurePower+" ExposCutOff:"+currentExposureCutOff+" BlmFac:"+currentBloomFactor+"  BlmPwr:"+currentBloomPower+" DwnSamp:"+currentDownSamplingCoef );
	
  }
}
