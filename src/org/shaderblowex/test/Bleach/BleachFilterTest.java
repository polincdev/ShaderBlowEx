 
package org.shaderblowex.test.Bleach;
 
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
import org.shaderblowex.filter.Bleach.BleachFilter;
 
 

/**
 *
 * @author xxx
 */
public class BleachFilterTest extends SimpleApplication  implements ActionListener {

  BleachFilter bleachMapFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
  
  
  float currentStrength=1.0f;
   
  
 public   BleachFilterTest()
    {
        
    }
    
    @Override
    public void simpleInitApp() {
        
        //No stats
        setDisplayStatView(false);
        setDisplayFps(false);
        //Background color
        viewPort.setBackgroundColor(ColorRGBA.Gray);
        //faster cam
        cam.setLocation(cam.getLocation().addLocal(0, 2f, 0));
        flyCam.setMoveSpeed(2.0f);
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
        inputManager.addMapping("StrInc", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("StrDec", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addListener(this, new String[]{"StrInc"});
        inputManager.addListener(this, new String[]{"StrDec"});
         
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.Red);
	hintText.setText("Strength:1/2");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("Strength:"+currentStrength );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
       
        //////////////////Filter//////////////////////
         bleachMapFilter=new BleachFilter(currentStrength);
        
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(bleachMapFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        BleachFilterTest app = new BleachFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
       
        if(name.equals("StrInc"))
        {
           currentStrength+=0.1f;   
           if(currentStrength>5.0)
               currentStrength=5.0f;
           refreshDisplay();
	    //
           bleachMapFilter.setStrength(currentStrength);
        }
        else  if(name.equals("StrDec"))
        {
           currentStrength-=0.1f;   
           if(currentStrength<0)
              currentStrength=0;
           refreshDisplay();
	    //
           bleachMapFilter.setStrength(currentStrength);
        }
       
    }
void refreshDisplay()
  {
    debugText.setText("Strength:"+currentStrength );
	  
  }    
    
}
