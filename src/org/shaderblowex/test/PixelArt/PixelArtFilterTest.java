 
package org.shaderblowex.test.PixelArt;


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
 
import org.shaderblowex.filter.PixelArt.PixelArtFilter;
 

/**
 *
 * @author xxx
 */
public class PixelArtFilterTest extends SimpleApplication  implements ActionListener {

  PixelArtFilter pixelArtFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
   
  float currentEdgeWidth=1.0f;
  float currentColorSize=4f;
  float currentColorCount=5.0f;
  float currentPixelResolution=250.0f;
  int currentpPaletteType=0;
  
  
  
  Integer[] types= new Integer[]{ PixelArtFilter.PALETTE_TYPE_LINEAR, 
                                 PixelArtFilter.PALETTE_TYPE_C64, 
                                  PixelArtFilter.PALETTE_TYPE_CGA, 
                                   PixelArtFilter.PALETTE_TYPE_CC, 
                                    PixelArtFilter.PALETTE_TYPE_ANSI, 
                                     PixelArtFilter.PALETTE_TYPE_STRIPE, 
                                      PixelArtFilter.PALETTE_TYPE_GRAYSCALE,
                                      PixelArtFilter.PALETTE_TYPE_HEATMAP, 
                                       PixelArtFilter.PALETTE_TYPE_RAINBOW, 
                                        PixelArtFilter.PALETTE_TYPE_BRIGHTNESS, 
                                         PixelArtFilter.PALETTE_TYPE_DESERT, 
                                          PixelArtFilter.PALETTE_TYPE_ELECTRICAL,
                                          PixelArtFilter.PALETTE_TYPE_NEON,
                                          PixelArtFilter.PALETTE_TYPE_FIRE,
                                          PixelArtFilter.PALETTE_TYPE_TECH,
                                          PixelArtFilter.PALETTE_TYPE_HUE,
                                            PixelArtFilter.PALETTE_TYPE_POSTERIZATION,
                                            PixelArtFilter.PALETTE_TYPE_TOONIFICATION,

                                          };
  
     
  String[] names= new  String[]{"Linear",
                                "C64",
                                "CGA",
                                "CC",
                                "ANSI",
                                "Stripe",
                                "Gray",
                                "Heat",
                                "Rainbow",
                                "Bright",
                                "Desert",
                                "Electric",
                                "Neon",
                                "Fire",
                                "Tech",
                                "Hue",
                                "Poster",
                                "Toon" 
                                 
                                };
 
     
  
 public   PixelArtFilterTest()
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
        //
        viewPort.setBackgroundColor(ColorRGBA.Gray);
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
        inputManager.addMapping("EdgWidDec", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("EdgWidInc", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("ClrSzDec", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("ClrSzInc", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("ClrCntDec", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("ClrCntInc", new KeyTrigger(KeyInput.KEY_6));
         inputManager.addMapping("PxResDec", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("PxResInc", new KeyTrigger(KeyInput.KEY_8));
         inputManager.addMapping("PalTpDec", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("PalTpInc", new KeyTrigger(KeyInput.KEY_0));
        
          
        
        inputManager.addListener(this, new String[]{"EdgWidDec"});
        inputManager.addListener(this, new String[]{"EdgWidInc"});
        inputManager.addListener(this, new String[]{"ClrSzDec"});
        inputManager.addListener(this, new String[]{"ClrSzInc"});
        inputManager.addListener(this, new String[]{"ClrCntDec"});
        inputManager.addListener(this, new String[]{"ClrCntInc"});
        inputManager.addListener(this, new String[]{"PxResDec"});
        inputManager.addListener(this, new String[]{"PxResInc"});
        inputManager.addListener(this, new String[]{"PalTpDec"});
        inputManager.addListener(this, new String[]{"PalTpInc"});
        
        
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.Red);
	hintText.setText("EdgeWid:1/2 ClrScal:3/4 ClrCount:5/6 PxlRes:7/8 PaletteType:9/0" );
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("EdgWid:"+currentEdgeWidth+" ClrScl:"+currentColorSize+" ClrCnt:"+currentColorCount+" PxlRes:"+currentPixelResolution+" PalTyp:"+names[currentpPaletteType]  );
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
         
  
        //////////////////Filter//////////////////////
         pixelArtFilter=new PixelArtFilter( );
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(pixelArtFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        PixelArtFilterTest app = new PixelArtFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
         
        if(name.equals("PalTpInc"))
        {
           currentpPaletteType++;   
            if(currentpPaletteType>types.length-1)
              currentpPaletteType=0;
            refreshDisplay();
	    //
           pixelArtFilter.setPaletteType(types[currentpPaletteType]);
        }
        else  if(name.equals("PalTpDec"))
        {
           currentpPaletteType--;   
           if(currentpPaletteType<0)
              currentpPaletteType=types.length-1;
            refreshDisplay();
	    //
          pixelArtFilter.setPaletteType(types[currentpPaletteType]);
        }
        else if(name.equals("EdgWidInc"))
        {
           currentEdgeWidth+=0.1;   
           if(currentEdgeWidth>10.0)
               currentEdgeWidth=10.0f;
           refreshDisplay(); 
	  //
           pixelArtFilter.setEdgeWidth(currentEdgeWidth);
        }   
      else if(name.equals("EdgWidDec"))
        {
           currentEdgeWidth-=0.1;   
            if(currentEdgeWidth<0)
              currentEdgeWidth=0;
          refreshDisplay(); 
	  //
           pixelArtFilter.setEdgeWidth(currentEdgeWidth);
        } 
        else if(name.equals("ClrSzInc"))
        {
           currentColorSize+=1.0;   
           refreshDisplay();
           //  
           pixelArtFilter.setColorSize(currentColorSize);
        }   
        else if(name.equals("ClrSzDec"))
        {
          currentColorSize-=1.0f; 
          if(currentColorSize<0)
              currentColorSize=0;
           refreshDisplay();
	  //
           pixelArtFilter.setColorSize(currentColorSize);
        }   
        if(name.equals("ClrCntInc"))
        {
            currentColorCount++;   
            refreshDisplay();
	    //
           pixelArtFilter.setColorCount(currentColorCount);
        }
        else  if(name.equals("ClrCntDec"))
        {
           currentColorCount--;   
           if(currentColorCount<0)
              currentColorCount=0;
            refreshDisplay();
	    //
          pixelArtFilter.setColorCount(currentColorCount);
        }
       else if(name.equals("PxResInc"))
        {
          currentPixelResolution+=10;   
            refreshDisplay();
	    //
           pixelArtFilter.setPixelResolution(currentPixelResolution);
        }
        else  if(name.equals("PxResDec"))
        {
           currentPixelResolution-=10;   
           if(currentPixelResolution<0)
              currentPixelResolution=0;
            refreshDisplay();
	    //
          pixelArtFilter.setPixelResolution(currentPixelResolution);
        }
        
        
    }
    
 void refreshDisplay()
   {
 debugText.setText("EdgWid:"+currentEdgeWidth+" ClrScl:"+currentColorSize+" ClrCnt:"+currentColorCount+" PxlRes:"+currentPixelResolution+" PalTyp:"+names[currentpPaletteType]  );
  }
 
}
