 
package org.shaderblowex.test.BetterToneMap;

 
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
import org.shaderblowex.filter.BetterToneMap.BetterToneMapFilter;

/**
 *
 * @author xxx
 */
public class BetterToneMapFilterTest extends SimpleApplication  implements ActionListener {

  BetterToneMapFilter betterToneMapFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
  
  int currentType=BetterToneMapFilter.TYPE_LINEAR;
  float currentExposure=1.0f;
  float currentGamma=1.0f;
  
  Integer[] types= new Integer[]{ BetterToneMapFilter.TYPE_LINEAR, 
                                 BetterToneMapFilter.TYPE_SIMPLE_REINHARD, 
                                  BetterToneMapFilter.TYPE_LUMA_BASED_REINHARD, 
                                   BetterToneMapFilter.TYPE_WHITE_PRESERVING_REINHARD, 
                                    BetterToneMapFilter.TYPE_RONBIN_DAHOUSE, 
                                     BetterToneMapFilter.TYPE_ACES_FILM, 
                                      BetterToneMapFilter.TYPE_ACES_ABSOLUTE_FILM,
                                      BetterToneMapFilter.TYPE_FILMIC, 
                                       BetterToneMapFilter.TYPE_UNCHARTED2, 
                                        BetterToneMapFilter.TYPE_DX11DSK, 
                                         BetterToneMapFilter.TYPE_TIMOTHY, 
                                          BetterToneMapFilter.TYPE_EXPONENTIAL,
                                          BetterToneMapFilter.TYPE_UNREAL,
                                          BetterToneMapFilter.TYPE_AMD_LOTTES,
                                          BetterToneMapFilter.TYPE_REINHARD2,
                                          BetterToneMapFilter.TYPE_UCHIMURA, 
                                          BetterToneMapFilter.TYPE_FERWERDA ,  
                                          BetterToneMapFilter.TYPE_HAARNPIETERDUIKER,  
                                          BetterToneMapFilter.TYPE_WARD, 
                                          BetterToneMapFilter.TYPE_TUMBLIN_RESHMEIER,
                                          BetterToneMapFilter.TYPE_SCHICK,
                                           BetterToneMapFilter.TYPE_REINHARD_EXTENDED, 
                                             BetterToneMapFilter.TYPE_REINHARD_DEVLIN,
                                              BetterToneMapFilter.TYPE_MEAN_VALUE,
                                                BetterToneMapFilter.TYPE_MAX_DIVISION,
                                                BetterToneMapFilter.TYPE_LOGARITMIC,
                                                BetterToneMapFilter.TYPE_INSOMNIAC, 
                                                BetterToneMapFilter.TYPE_GRAHAM_ALDRIDGE_FILMIC,
                                                 BetterToneMapFilter.TYPE_EXPONENTIATION,
                                                BetterToneMapFilter.TYPE_EXPONENTIAL2,
                                                BetterToneMapFilter.TYPE_DROGO ,
                                                BetterToneMapFilter.TYPE_CLAMPING ,
                                                BetterToneMapFilter.TYPE_JODIE_ROBO, 
                                                 BetterToneMapFilter.TYPE_JODIE_REINHARD,
                                                 BetterToneMapFilter.TYPE_BARTEROPE,
                                                  BetterToneMapFilter.TYPE_GIFFORD,
                                                 
                                             
                                          };
    
          
  String[] names= new  String[]{"Linear",
                                "SimpleReinhard",
                                "LumaBasedReinhard",
                                "WhitePreservingLumaBasedReinhard",
                                "RomBinDaHouse",
                                "ACESFilm",
                                "ACESAbsoluteFilm",
                                "Filmic",
                                "Uncharted2",
                                "DX11DSK",
                                "Timothy",
                                "Exponential",
                                "Unreal",
                                "AMDLottes",
                                "Reinhard2",
                                "Uchimura",
                                "Ferwerda",
                                "HaarmPieterDuiker",
                                "Ward",
                                "TumblinRushmeier",
                                "Schlick",
                                "ReinhardExtended",
                                "ReinhardDevlin",
                                "MeanValue",
                                "MaximumDivision",
                                "Logarithmic",
                                "Insomniac",
                                "GrahamAldridgeFilmic",
                                "Exponentiation",
                                "Exponential2",
                                "Drogo",
                                "Clamping",
                                "JodieRobo",
                                "JodieReinhard",
                                "Barterope",
                                "GiffordTonemap"
                                 
                                };
 
  
 public   BetterToneMapFilterTest()
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
        
        /*
        //Shadows
         DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, 1024, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
        //
        for(Spatial child: sceneAsNode.getChildren())
           child.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);//CastAndReceive
         */
        
        //Keys
        inputManager.addMapping("NextType", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("PrevType", new KeyTrigger(KeyInput.KEY_BACK));
        inputManager.addMapping("ExpInc", new KeyTrigger(KeyInput.KEY_EQUALS));
        inputManager.addMapping("ExpDec", new KeyTrigger(KeyInput.KEY_MINUS));
        inputManager.addMapping("GammaDec", new KeyTrigger(KeyInput.KEY_LBRACKET));
        inputManager.addMapping("GammaInc", new KeyTrigger(KeyInput.KEY_RBRACKET));
        inputManager.addListener(this, new String[]{"NextType"});
        inputManager.addListener(this, new String[]{"PrevType"});
        inputManager.addListener(this, new String[]{"ExpInc"});
        inputManager.addListener(this, new String[]{"ExpDec"});
        inputManager.addListener(this, new String[]{"GammaInc"});
        inputManager.addListener(this, new String[]{"GammaDec"});
        
        
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.White);
	hintText.setText("Exposure:+/- Gamma:[/] Type:Space/Backspace");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("Exp:"+currentExposure+" Gam:"+currentGamma +" Type:"+names[currentType]);
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
       
        //////////////////Filter//////////////////////
         betterToneMapFilter=new BetterToneMapFilter(currentType,currentExposure,currentGamma);
        
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(betterToneMapFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        BetterToneMapFilterTest app = new BetterToneMapFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
        
        if(name.equals("NextType"))
        {
           currentType++;   
           currentType=currentType%names.length;
          refreshDisplay();
	    //
           betterToneMapFilter.setType(types[currentType]);
        }
        else  if(name.equals("PrevType"))
        {
           currentType--;   
           if(currentType<0)
              currentType=(names.length-1);
             refreshDisplay();
	    //
           betterToneMapFilter.setType(types[currentType]);
        }
       else if(name.equals("ExpInc"))
        {
           currentExposure+=0.1;   
           refreshDisplay();
	  //
           betterToneMapFilter.setExposure(currentExposure);
        }   
      else if(name.equals("ExpDec"))
        {
           currentExposure-=0.1;   
            if(currentExposure<0)
              currentExposure=0;
           refreshDisplay();
	  //
           betterToneMapFilter.setExposure(currentExposure);
        } 
        else if(name.equals("GammaInc"))
        {
           currentGamma+=0.1;   
           refreshDisplay();
	  //  
           betterToneMapFilter.setGamma(currentGamma);
        }   
        else if(name.equals("GammaDec"))
        {
           currentGamma-=0.1; 
             if(currentGamma<0)
              currentGamma=0;
           refreshDisplay();
	  //
           betterToneMapFilter.setGamma(currentGamma);
        }   
    }

void refreshDisplay()
  {
     debugText.setText("Exp:"+currentExposure+" Gamma:"+currentGamma +" Type:"+names[currentType]);
	  
  }
}
