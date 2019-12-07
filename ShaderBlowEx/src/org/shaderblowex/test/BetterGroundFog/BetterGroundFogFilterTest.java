 
package org.shaderblowex.test.BetterGroundFog;

 
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
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.shaderblowex.filter.BetterGroundFog.BetterGroundFogState;
import org.shaderblowex.filter.BetterToneMap.BetterToneMapFilter;

/**
 *
 * @author xxx
 */
public class BetterGroundFogFilterTest extends SimpleApplication  implements ActionListener {

  BetterGroundFogState betterGroundFogState;
    
  BitmapText hintText;  
  BitmapText debugText; 
 
   
 private float currentSunShininess = 8.0f;
 private float currentFogDensity = 0.08f;
  private float currentGroundLevel = -10f;
  private float currentFogBoundaryX=3;
   private float currentFogBoundaryY=100;
    private float currentFogBoundaryZ=50;
 private float currentFogBoundaryW=0;
 private Vector4f currentFogBoundary = new Vector4f(currentFogBoundaryX, currentFogBoundaryY, currentFogBoundaryZ, currentFogBoundaryW);
  
 public   BetterGroundFogFilterTest()
    {
        
    }
    
    @Override
    public void simpleInitApp() {
        
        //No stats
        setDisplayStatView(false);
        setDisplayFps(false);
        //faster cam
        cam.setLocation(cam.getLocation().addLocal(0, 2f, 0));
        flyCam.setMoveSpeed(2.0f);
        //Test image
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
        inputManager.addMapping("LevelDec", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("LevelInc", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("DensityDec", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("DensityInc", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("ShineDec", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("ShineInc", new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("BndXDec", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("BndXInc", new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping("BndYDec", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("BndYInc", new KeyTrigger(KeyInput.KEY_0));
        inputManager.addMapping("BndZDec", new KeyTrigger(KeyInput.KEY_MINUS));
        inputManager.addMapping("BndZInc", new KeyTrigger(KeyInput.KEY_EQUALS));
        inputManager.addMapping("BndWDec", new KeyTrigger(KeyInput.KEY_LBRACKET));
        inputManager.addMapping("BndWInc", new KeyTrigger(KeyInput.KEY_RBRACKET));
        inputManager.addListener(this, new String[]{"LevelInc"});
        inputManager.addListener(this, new String[]{"LevelDec"});
        inputManager.addListener(this, new String[]{"DensityDec"});
        inputManager.addListener(this, new String[]{"DensityInc"});
        inputManager.addListener(this, new String[]{"ShineInc"});
        inputManager.addListener(this, new String[]{"ShineDec"});
         inputManager.addListener(this, new String[]{"BndXInc"});
        inputManager.addListener(this, new String[]{"BndXDec"});
         inputManager.addListener(this, new String[]{"BndYInc"});
        inputManager.addListener(this, new String[]{"BndYDec"});
         inputManager.addListener(this, new String[]{"BndZInc"});
        inputManager.addListener(this, new String[]{"BndZDec"});
         inputManager.addListener(this, new String[]{"BndWInc"});
        inputManager.addListener(this, new String[]{"BndWDec"});
        
        
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.White);
	hintText.setText("Lvl:1/2 Dens:3/4 Shin:5/6 BndX:7/8 BndY:9/0 BndZ:-/+ BndW:[/]");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("Lvl:"+currentGroundLevel+" Dens:"+currentFogDensity +" Shin:"+currentSunShininess +" BndX:"+currentFogBoundaryX+" BndY:"+currentFogBoundaryY+" BndZ:"+currentFogBoundaryZ+" BndW:"+currentFogBoundaryW);
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
       
        //////////////////STATE and Filter//////////////////////
        betterGroundFogState = new BetterGroundFogState();
        betterGroundFogState.setSun( sun); //put reference to sunlight, so it automatically updates
        betterGroundFogState.getFilter().setGroundLevel(currentGroundLevel); 
        betterGroundFogState.getFilter().setFogDensity(currentFogDensity);
         betterGroundFogState.getFilter().setSunShininess(currentSunShininess);
         betterGroundFogState.getFilter().setFogBoundary(currentFogBoundary); //x y and z need to be unitvector pointing away from the fog, w is the distance from the origin, so this is horizontal fog  
        betterGroundFogState.setIsNight(true);              
        betterGroundFogState.setSunColorNight(new ColorRGBA(0.6f,0.6f, 0.6f, 1f));
        betterGroundFogState.setFogColorNight(new ColorRGBA(0.5f, 0.5f, 0.5f, 1f));
        stateManager.attach(betterGroundFogState);
        //
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(betterGroundFogState.getFilter());
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        BetterGroundFogFilterTest app = new BetterGroundFogFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
        /*
        if(name.equals("NextType"))
        {
           currentType++;   
           currentType=currentType%names.length;
           debugText.setText("Exp:"+currentExposure+" Gam:"+currentGamma +" Type:"+names[currentType]);
	    //
           betterToneMapFilter.setType(types[currentType]);
        }
        else  if(name.equals("PrevType"))
        {
           currentType--;   
           if(currentType<0)
              currentType=(names.length-1);
           debugText.setText("Exp:"+currentExposure+" Gam:"+currentGamma +" Type:"+names[currentType]);
	    //
           betterToneMapFilter.setType(types[currentType]);
        }
        */
       if(name.equals("LevelInc"))
        {
           currentGroundLevel+=1.0;   
          //
           refreshDisplay();
           betterGroundFogState.getFilter().setGroundLevel(currentGroundLevel);
        }   
      else if(name.equals("LevelDec"))
        {
           currentGroundLevel-=1.0;   
           //
           refreshDisplay();
           betterGroundFogState.getFilter().setGroundLevel(currentGroundLevel);
        } 
        else if(name.equals("DensityInc"))
        {
           currentFogDensity+=0.1;   
          //  
          refreshDisplay();
           betterGroundFogState.getFilter().setFogDensity(currentFogDensity);
        }   
        else if(name.equals("DensityDec"))
        {
           currentFogDensity-=0.1; 
             if(currentFogDensity<0)
              currentFogDensity=0;
           //  
           refreshDisplay();
           betterGroundFogState.getFilter().setFogDensity(currentFogDensity);
        }
        else   if(name.equals("ShineInc"))
        {
           currentSunShininess+=1.0;   
          //
           refreshDisplay();
           betterGroundFogState.getFilter().setSunShininess(currentSunShininess);
        }   
      else if(name.equals("ShineDec"))
        {
           currentSunShininess-=1.0;   
           if(currentSunShininess<0)
              currentSunShininess=0;
           //
           refreshDisplay();
           betterGroundFogState.getFilter().setSunShininess(currentSunShininess);
        } 
      else if(name.equals("BndXInc"))
        {
           currentFogBoundaryX+=1.0;   
          //
           refreshDisplay();
          betterGroundFogState.getFilter().getFogBoundary().setX(currentFogBoundaryX);
        }   
      else if(name.equals("BndXDec"))
        {
           currentFogBoundaryX-=1.0;   
           //
           refreshDisplay();
           betterGroundFogState.getFilter().getFogBoundary().setX(currentFogBoundaryX);
        } 
     else if(name.equals("BndYInc"))
        {
           currentFogBoundaryY+=1.0;   
          //
           refreshDisplay();
          betterGroundFogState.getFilter().getFogBoundary().setY(currentFogBoundaryY);
        }   
      else if(name.equals("BndYDec"))
        {
           currentFogBoundaryY-=1.0;   
           //
           refreshDisplay();
           betterGroundFogState.getFilter().getFogBoundary().setY(currentFogBoundaryY);
        } 
        else if(name.equals("BndZInc"))
        {
           currentFogBoundaryZ+=1.0;   
          //
           refreshDisplay();
          betterGroundFogState.getFilter().getFogBoundary().setZ(currentFogBoundaryZ);
        }   
      else if(name.equals("BndZDec"))
        {
           currentFogBoundaryZ-=1.0;   
           //
           refreshDisplay();
           betterGroundFogState.getFilter().getFogBoundary().setZ(currentFogBoundaryZ);
        } 
          else if(name.equals("BndWInc"))
        {
           currentFogBoundaryW+=1.0;   
          //
           refreshDisplay();
          betterGroundFogState.getFilter().getFogBoundary().setW(currentFogBoundaryW);
        }   
      else if(name.equals("BndWDec"))
        {
           currentFogBoundaryW-=1.0;   
           //
           refreshDisplay();
           betterGroundFogState.getFilter().getFogBoundary().setW(currentFogBoundaryW);
        } 
    }
    
     
 void refreshDisplay()
    {
   debugText.setText("Lvl:"+currentGroundLevel+" Dens:"+currentFogDensity +" Shin:"+currentSunShininess +" BndX:"+currentFogBoundaryX+" BndY:"+currentFogBoundaryY+" BndZ:"+currentFogBoundaryZ+" BndW:"+currentFogBoundaryW);
    }
 
}
