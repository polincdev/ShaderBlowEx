/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shaderblowex.test.BetterToneMap;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author xxx
 */
public class BetterTMAndCCTestApp  extends SimpleApplication  {
    
    
    public BetterTMAndCCTestApp()
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
         //
         BetterTMAndCCTestState betterTMAndCCTestState=new BetterTMAndCCTestState(  rootNode,   guiNode,  inputManager, assetManager, cam, viewPort);
         stateManager.attach(betterTMAndCCTestState);
     }
        
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
       
        BetterTMAndCCTestApp app = new BetterTMAndCCTestApp();
         app.start();
     
  }
}
