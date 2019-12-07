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

