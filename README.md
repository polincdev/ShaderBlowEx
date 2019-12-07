# ShaderBlowEx
Extended filters library for JMonkey Game Engine

https://jmonkeyengine.org

# 1. BetterToneMap

## Usage: 
```
//Init
int currentType=BetterToneMapFilter.BetterToneMapFilter.TYPE_LINEAR;
float currentExposure=1.0;
float currentGamma=1.0;
//
BetterToneMapFilter betterToneMapFilter=new BetterToneMapFilter(currentType,currentExposure,currentGamma);
FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
fpp.addFilter(betterToneMapFilter);
viewPort.addProcessor(fpp);
//Modify
betterToneMapFilter.setType(BetterToneMapFilter.TYPE_WHITE_PRESERVING_REINHARD);
betterToneMapFilter.setExposure(1.1f);
betterToneMapFilter.setGamma(2.2f);
```


#### Credits:

https://github.com/tizian/tonemapper/

https://github.com/GPUOpen-Effects/FidelityFX

https://github.com/cansik/processing-postfx

#### Test:

![ToneMapFilterTest](../master/img/BetterToneMapFilter.jpg)


# 2. BetterColorCorrection

## Usage: 
```
//Init
float currentContrast=1.0f;
float currentBrightness=0.0f;
float currentHue=0.0f;
float currentInvert=0.0f;
float currentRed=1.0f;
float currentGreen=1.0f;
float currentBlue=1.0f;
float currentGamma=1.0f;
float currentSaturation=0.0f;
//
BetterColorCorrectionFilter betterColorCorrectionFilter=new BetterColorCorrectionFilter(currentContrast, currentBrightness,currentHue, currentSaturation,currentInvert,currentRed, currentGreen, currentBlue, currentGamma); 
FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
fpp.addFilter(betterColorCorrectionFilter);
viewPort.addProcessor(fpp);
//Modify
betterColorCorrectionFilter.setContrast(currentContrast);
betterColorCorrectionFilter.setBrightness(currentBrightness);
betterColorCorrectionFilter.setHue(currentHue);
betterColorCorrectionFilter.setSaturation(currentSaturation);
betterColorCorrectionFilter.setGamma(currentGamma);
betterColorCorrectionFilter.setInvert(currentInvert);
betterColorCorrectionFilter.setRed(currentRed);
betterColorCorrectionFilter.setGreen(currentGreen);
betterColorCorrectionFilter.setBlue(currentBlue);
```
#### Credits:

https://hub.jmonkeyengine.org/t/i-made-a-post-filter-color-grading/31626

#### Test:

![ColorCorrectionFilterTest](../master/img/BetterColorCorrectionFilter.jpg)

# 3. BetterDepthOfField

## Usage: 
```
//Init
float currentFocalDepth=0.5f;
float currentFocalLength=75f;
float currentFStop=16.0f;
boolean currentAutoFocus=true;
boolean currentShowFocus=false;
//
betterDepthOfFieldFilter=new BetterDepthOfFieldFilter( );
FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
fpp.addFilter(betterDepthOfFieldFilter);
viewPort.addProcessor(fpp);
//
betterDepthOfFieldFilter.setFocalDepth(currentFocalDepth);
betterDepthOfFieldFilter.setFocalLength(currentFocalLength);
betterDepthOfFieldFilter.setfStop(currentFStop);
betterDepthOfFieldFilter.setAutoFocus(currentAutoFocus);
betterDepthOfFieldFilter.setShowFocus(currentShowFocus);
```

#### Credits:

https://hub.jmonkeyengine.org/t/new-depth-of-field-effect/31036
 
http://devlog-martinsh.blogspot.com/2011/12/glsl-depth-of-field-with-bokeh-v24.html

#### Test:

![ColorCorrectionFilterTest](../master/img/BetterDepthOfFieldFilter.jpg)

# 4. BetterGroundFogFilter

## Usage: 
```
//Init
float currentSunShininess = 8.0f;
float currentFogDensity = 0.08f;
float currentGroundLevel = -10f;
float currentFogBoundaryX=3;
float currentFogBoundaryY=100;
float currentFogBoundaryZ=50;
float currentFogBoundaryW=0;
Vector4f currentFogBoundary = new Vector4f(currentFogBoundaryX, currentFogBoundaryY, currentFogBoundaryZ, currentFogBoundaryW);
//
BetterGroundFogState betterGroundFogState = new BetterGroundFogState();
betterGroundFogState.setSun( sun); //put reference to sunlight 
betterGroundFogState.getFilter().setGroundLevel(currentGroundLevel); 
betterGroundFogState.getFilter().setFogDensity(currentFogDensity);
betterGroundFogState.getFilter().setSunShininess(currentSunShininess);
betterGroundFogState.getFilter().setFogBoundary(currentFogBoundary); 
betterGroundFogState.setIsNight(true);              
betterGroundFogState.setSunColorNight(new ColorRGBA(0.6f,0.6f, 0.6f, 1f));
betterGroundFogState.setFogColorNight(new ColorRGBA(0.5f, 0.5f, 0.5f, 1f));
stateManager.attach(betterGroundFogState);
//
FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
fpp.addFilter(betterGroundFogState.getFilter());
viewPort.addProcessor(fpp);
//Modify
betterGroundFogState.getFilter().setGroundLevel(currentGroundLevel);
betterGroundFogState.getFilter().setFogDensity(currentFogDensity);
betterGroundFogState.getFilter().setSunShininess(currentSunShininess);
betterGroundFogState.getFilter().getFogBoundary().setX(currentFogBoundaryX);
betterGroundFogState.getFilter().getFogBoundary().setY(currentFogBoundaryY);
betterGroundFogState.getFilter().getFogBoundary().setZ(currentFogBoundaryZ);
betterGroundFogState.getFilter().getFogBoundary().setW(currentFogBoundaryW);
```

#### Credits:

https://hub.jmonkeyengine.org/t/bettergroundfogfilter/41452

#### Test:

![ColorCorrectionFilterTest](../master/img/BetterGroundFogFilter.jpg)


# 5. BleachFilter

#### Info:

http://en.wikipedia.org/wiki/Bleach_bypass

## Usage: 
```
//Init
float currentStrength=1.0f;
//
BleachMapFilter bleachMapFilter=new BleachMapFilter(currentStrength);
FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
fpp.addFilter(bleachMapFilter);
viewPort.addProcessor(fpp);
//Modify
bleachMapFilter.setStrength(currentStrength);
```

#### Test:

![ColorCorrectionFilterTest](../master/img/BleachFilter.jpg)


# 6. BokeyDoFFilter

## Usage: 
```
//Init
float currentFocusPoint=8.0f;
float currentRadiusScale=0.5f;
float currentBlurSize=3.0f;
float currentFocusScale=5.0f;
//
BokehDoFFilter  bokehDoFFilter=new BokehDoFFilter( );
FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
fpp.addFilter(bokehDoFFilter);
viewPort.addProcessor(fpp);
//Modify
bokehDoFFilter.setFocusPoint(currentFocusPoint);
bokehDoFFilter.setRadiusScale(currentRadiusScale);
bokehDoFFilter.setBlurSize(currentBlurSize);
bokehDoFFilter.setFocusScale(currentFocusScale); 
```
#### Credits:

http://tuxedolabs.blogspot.com/2018/05/bokeh-depth-of-field-in-single-pass.html

https://mynameismjp.wordpress.com/the-museum/samples-tutorials-tools/depth-of-field-sample/

https://github.com/TheRealMJP/BakingLab/blob/master/BakingLab/PostProcessing.hlsl#L160

#### Test:

![ColorCorrectionFilterTest](../master/img/BokehDoFFilter.jpg)
