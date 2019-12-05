 
package org.shaderblowex.test.BetterColorCorrection;

 
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
import org.shaderblowex.filter.BetterColorCorrection.BetterColorCorrectionFilter;
import org.shaderblowex.filter.BetterToneMap.BetterToneMapFilter;

/**
 *
 * @author xxx
 */
public class ColorCorrectionFilterTest extends SimpleApplication  implements ActionListener {

  BetterColorCorrectionFilter betterColorCorrectionFilter;
    
  BitmapText hintText;  
  BitmapText debugText; 
   
  float currentContrast=1.0f;
  float currentBrightness=0.0f;
  float currentHue=0.0f;
  float currentInvert=0.0f;
  float currentRed=1.0f;
  float currentGreen=1.0f;
  float currentBlue=1.0f;
  float currentGamma=1.0f;
  float currentSaturation=0.0f;
     
   
 public   ColorCorrectionFilterTest()
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
        
        
        //Keys
        inputManager.addMapping("CntrstDec", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("CntrstInc", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("BrghtnssDec", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("BrghtnssInc", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("HueDec", new KeyTrigger(KeyInput.KEY_5));
        inputManager.addMapping("HueInc", new KeyTrigger(KeyInput.KEY_6));
        inputManager.addMapping("SaturDec", new KeyTrigger(KeyInput.KEY_7));
        inputManager.addMapping("SaturInc", new KeyTrigger(KeyInput.KEY_8));
        inputManager.addMapping("InvrtDec", new KeyTrigger(KeyInput.KEY_9));
        inputManager.addMapping("InvrtInc", new KeyTrigger(KeyInput.KEY_0));
        inputManager.addMapping("RedDec", new KeyTrigger(KeyInput.KEY_LBRACKET));
        inputManager.addMapping("RedInc", new KeyTrigger(KeyInput.KEY_RBRACKET));
        inputManager.addMapping("GreenDec", new KeyTrigger(KeyInput.KEY_SEMICOLON));
        inputManager.addMapping("GreenInc", new KeyTrigger(KeyInput.KEY_APOSTROPHE));
        inputManager.addMapping("BlueDec", new KeyTrigger(KeyInput.KEY_COMMA));
        inputManager.addMapping("BlueInc", new KeyTrigger(KeyInput.KEY_PERIOD));
        inputManager.addMapping("GammaDec", new KeyTrigger(KeyInput.KEY_MINUS));
        inputManager.addMapping("GammaInc", new KeyTrigger(KeyInput.KEY_EQUALS));
        
        
     
        
        inputManager.addListener(this, new String[]{"CntrstInc"});
        inputManager.addListener(this, new String[]{"CntrstDec"});
        inputManager.addListener(this, new String[]{"BrghtnssInc"});
        inputManager.addListener(this, new String[]{"BrghtnssDec"});
        inputManager.addListener(this, new String[]{"HueInc"});
        inputManager.addListener(this, new String[]{"HueDec"});
        inputManager.addListener(this, new String[]{"SaturInc"});
        inputManager.addListener(this, new String[]{"SaturDec"});
        inputManager.addListener(this, new String[]{"GammaInc"});
        inputManager.addListener(this, new String[]{"GammaDec"});
        inputManager.addListener(this, new String[]{"RedInc"});
        inputManager.addListener(this, new String[]{"RedDec"});
        inputManager.addListener(this, new String[]{"GreenInc"});
        inputManager.addListener(this, new String[]{"GreenDec"});
        inputManager.addListener(this, new String[]{"BlueInc"});
        inputManager.addListener(this, new String[]{"BlueDec"});
        inputManager.addListener(this, new String[]{"InvrtDec"});
        inputManager.addListener(this, new String[]{"InvrtInc"});
         
        //Text
        BitmapFont font =  getAssetManager().loadFont("Interface/Fonts/Default.fnt");
	//Hint
	hintText = new BitmapText(font);
	hintText.setSize(font.getCharSet().getRenderedSize()*1.5f);
	hintText.setColor(ColorRGBA.White);
	hintText.setText("Cntr:1/2 Brgh:3/4 Hue:5/6 Sat:7/8 Inv: 9/0 Gam:+/- R:[/] G:;/' B:</>");
	hintText.setLocalTranslation(0, this.getCamera().getHeight()-10, 1.0f);
	hintText.updateGeometricState();
        guiNode.attachChild(hintText);
        //Info
	debugText=hintText.clone();
        debugText.setColor(ColorRGBA.White);
	debugText.setText("Cntr:"+currentContrast+" Brgh:"+currentBrightness+" Hue:"+currentHue+" Sat:"+currentSaturation+" Inv:"+currentInvert+" Gam:"+currentGamma+" R:"+currentRed+" G:"+currentGreen+" B:"+currentBlue);
	debugText.setLocalTranslation(0, hintText.getLocalTranslation().y-30, 1.0f);
	debugText.updateGeometricState();
        guiNode.attachChild(debugText);
        
       
        //////////////////Filter//////////////////////
        betterColorCorrectionFilter=new BetterColorCorrectionFilter(currentContrast, currentBrightness,currentHue, currentSaturation,currentInvert,currentRed, currentGreen, currentBlue, currentGamma); 
         FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
         fpp.addFilter(betterColorCorrectionFilter);
         viewPort.addProcessor(fpp);
        
      }
    
  
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        ColorCorrectionFilterTest app = new ColorCorrectionFilterTest();
         app.start();
     
  }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       
        
        if(!isPressed)
            return;
         
     
        if(name.equals("CntrstInc"))
            {
                currentContrast+=0.1;   
                  if(currentContrast>10)
               currentContrast=10;
               refreshDisplay();
                //
               betterColorCorrectionFilter.setContrast(currentContrast);
            }
        else  if(name.equals("CntrstDec"))
            {
             currentContrast-=0.1;   
               if(currentContrast<0)
                  currentContrast=0;
               refreshDisplay();
                //
               betterColorCorrectionFilter.setContrast(currentContrast);
            }
        else  if(name.equals("BrghtnssInc"))
            {
                currentBrightness+=0.1;   
                if(currentBrightness>1)
                  currentBrightness=1;
              refreshDisplay();
                //
               betterColorCorrectionFilter.setBrightness(currentBrightness);
            }
        else  if(name.equals("BrghtnssDec"))
             {
              currentBrightness-=0.1;   
                if(currentBrightness<0)
                   currentBrightness=0;
                refreshDisplay();
                 //
                betterColorCorrectionFilter.setBrightness(currentBrightness);
             }
        
        else   if(name.equals("HueInc"))
            {
                currentHue+=0.1; 
               if(currentHue>1)
                 currentHue=1f;
              refreshDisplay();
                //
               betterColorCorrectionFilter.setHue(currentHue);
            }
        else  if(name.equals("HueDec"))
            {
               currentHue-=0.1;   
               if(currentHue<-1)
                  currentHue=-1;
               refreshDisplay();
                //
               betterColorCorrectionFilter.setHue(currentHue);
            }
        
        else   if(name.equals("SaturInc"))
            {
                currentSaturation+=0.1;   
                if(currentSaturation>1.0f)
               currentSaturation=1f;
              refreshDisplay();
                //
               betterColorCorrectionFilter.setSaturation(currentSaturation);
            }
        else  if(name.equals("SaturDec"))
            {
             currentSaturation-=0.1;   
               if(currentSaturation<-1)
                  currentSaturation=-1;
               refreshDisplay();
                //
               betterColorCorrectionFilter.setSaturation(currentSaturation);
            }
        else if(name.equals("GammaInc"))
            {
               currentGamma+=0.1;   
               refreshDisplay();
              //  
               betterColorCorrectionFilter.setGamma(currentGamma);
            }   
        else if(name.equals("GammaDec"))
            {
               currentGamma-=0.1; 
                 if(currentGamma<0)
                  currentGamma=0;
               refreshDisplay();
              //
               betterColorCorrectionFilter.setGamma(currentGamma);
            }   
           
   else if(name.equals("InvrtInc"))
            {
                currentInvert+=0.1;   
                  if(currentInvert>1)
               currentInvert=1;
              refreshDisplay();
                //
               betterColorCorrectionFilter.setInvert(currentInvert);
            }
   else  if(name.equals("InvrtDec"))
        {
           currentInvert-=0.1;   
           if(currentInvert<0)
              currentInvert=0;
           refreshDisplay();
	    //
           betterColorCorrectionFilter.setInvert(currentInvert);
        }       
   else if(name.equals("RedInc"))
        {
            currentRed+=0.1;   
              if(currentRed>10)
           currentRed=10;
          refreshDisplay();
	    //
           betterColorCorrectionFilter.setRed(currentRed);
        }
   else  if(name.equals("RedDec"))
        {
         currentRed-=0.1;   
           if(currentRed<0)
              currentRed=0;
           refreshDisplay();
	    //
           betterColorCorrectionFilter.setRed(currentRed);
        }         
  else  if(name.equals("GreenInc"))
        {
            currentGreen+=0.1;   
              if(currentGreen>10)
           currentGreen=10;
          refreshDisplay();
	    //
           betterColorCorrectionFilter.setGreen(currentGreen);
        }
   else  if(name.equals("GreenDec"))
        {
         currentGreen-=0.1;   
           if(currentGreen<0)
              currentGreen=0;
           refreshDisplay();
	    //
           betterColorCorrectionFilter.setGreen(currentGreen);
        }       
      else  if(name.equals("BlueInc"))
        {
            currentBlue+=0.1;   
              if(currentBlue>10)
           currentBlue=10;
          refreshDisplay();
	    //
           betterColorCorrectionFilter.setBlue(currentBlue);
        }
   else  if(name.equals("BlueInc"))
        {
         currentBlue-=0.1;   
           if(currentBlue<0)
              currentBlue=0;
           refreshDisplay();
	    //
           betterColorCorrectionFilter.setBlue(currentBlue);
        }       
    }
    
 void refreshDisplay()
    {
      debugText.setText("Cntr:"+currentContrast+" Brgh:"+currentBrightness+" Hue:"+currentHue+" Sat:"+currentSaturation+" Inv:"+currentInvert+" Gam:"+currentGamma+" R:"+currentRed+" G:"+currentGreen+" B:"+currentBlue);
     }
            
            
                
}
