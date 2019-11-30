# ShaderBlowEx
Extended filters library for JMonkey

# 1. BetterToneMap

Usage:
int currentType=BetterToneMapFilter.BetterToneMapFilter.TYPE_LINEAR;

float currentExposure=1.0;

float currentGamma=1.0;

BetterToneMapFilter betterToneMapFilter=new BetterToneMapFilter(currentType,currentExposure,currentGamma);

FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

fpp.addFilter(betterToneMapFilter);

viewPort.addProcessor(fpp);



Credits:

https://github.com/tizian/tonemapper/

https://github.com/GPUOpen-Effects/FidelityFX

https://github.com/cansik/processing-postfx
