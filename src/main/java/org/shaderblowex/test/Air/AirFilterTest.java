 
package  main.java.org.shaderblowex.test.Air;

 
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
import org.shaderblowex.filter.Air.AirFilter;
 

/**
 *
 * @author xxx
 */
public class AirFilterTest extends SimpleApplication  implements ActionListener {

  AirFilter airFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
   
 float currentAirDensity = 0.4f;
 float currentAirDistance = 10;
 float currentAirDesaturation = 1f;
    
  
 public   AirFilterTest()
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
        viewPort.setBackgroundColor(ColorRGBA.Black);
         //2D  reference image
        Geometry geometry= new Geometry("ToneMapFilterGeo",new Quad( this.getCamera().getWidth()/3  , this.getCamera().getHeight()/3  ));
        Material    geoMat = new Material(this.getAssetManager(),  "Common/MatDefs/Misc/Unshaded.j3md");
        geoMat.setTexture("ColorMap", assetManager.loadTexture("ShaderBlowEx/Textures/test.png")); 
        geometry.setMaterial(geoMat);
        guiNode.attachChild(geometry);
        
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
        inputManager.addMapping("DenDec", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("DenInc", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("DisDec", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("DisInc", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("DesDec", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("DesInc", new KeyTrigger(KeyInput.KEY_6));
       
        inputManager.addListener(this, new String[]{"DenDec"});
        inputManager.addListener(this, new String[]{"DenInc"});
        inputManager.addListener(this, new String[]{"DisDec"});
        inputManager.addListener(this, new String[]{"DisInc"});
        inputManager.addListener(this, new String[]{"DesDec"});
        inputManager.addListener(this, new String[]{"DesInc"});
     
        
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.Red);
	hintText.setText("AirDensity:1/2 AirDistance:3/4 Desaturation:5/6" );
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
         
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("AirDensity:"+currentAirDensity+" AirDistance:"+currentAirDistance+" Desaturation:"+currentAirDesaturation  );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
   
        //////////////////Filter//////////////////////
         airFilter=new AirFilter( );
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(airFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        AirFilterTest app = new AirFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
         
        if(name.equals("DenInc"))
        {
           currentAirDensity+=0.1;   
           if(currentAirDensity>1)
              currentAirDensity=1;
            refreshDisplay();
	    //
           airFilter.setAirDensity(currentAirDensity);
        }
        else  if(name.equals("DenDec"))
        {
         currentAirDensity-=0.1;   
           if(currentAirDensity<0)
              currentAirDensity=0;
            refreshDisplay();
	    //
           airFilter.setAirDensity(currentAirDensity);
        }
        else if(name.equals("DisInc"))
        {
           currentAirDistance+=1.0;   
           if(currentAirDistance>200.0)
               currentAirDistance=200.0f;
           refreshDisplay(); 
	  //
           airFilter.setAirDistance(currentAirDistance);
        }   
      else if(name.equals("DisDec"))
        {
           currentAirDistance-=1.0;   
            if(currentAirDistance<0)
              currentAirDistance=0;
          refreshDisplay(); 
	  //
           airFilter.setAirDistance(currentAirDistance);
        } 
        else if(name.equals("DesInc"))
        {
           currentAirDesaturation+=0.1;  
             if(currentAirDesaturation>5)
              currentAirDesaturation=5;
           refreshDisplay();
           //  
           airFilter.setAirDesaturation(currentAirDesaturation);
        }   
        else if(name.equals("DesDec"))
        {
          currentAirDesaturation-=0.1f; 
          if(currentAirDesaturation<0)
              currentAirDesaturation=0;
           refreshDisplay();
	  //
           airFilter.setAirDesaturation(currentAirDesaturation);
        }   
      
        
        
    }
    
 void refreshDisplay()
   {
  debugText.setText("AirDensity:"+currentAirDensity+" AirDistance:"+currentAirDistance+" Desaturation:"+currentAirDesaturation  );
  }
 
}
