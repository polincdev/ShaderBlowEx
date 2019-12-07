#import "Common/ShaderLib/GLSLCompat.glsllib"
//Credits 
//https://hub.jmonkeyengine.org/t/i-made-a-post-filter-color-grading/31626

uniform sampler2D m_Texture;
uniform float m_Brightness;
uniform float m_Contrast;
uniform float m_Hue;
uniform float m_Saturation;
 varying vec2 texCoord;
 uniform float m_Invert;
 uniform float m_Red;
 uniform float m_Green;
 uniform float m_Blue;
 uniform float m_Gamma;
  
 
vec3 contrast_PerPixel( vec3 color,  float contrast){
    color = ((color - 0.5) * max(contrast, 0.0)) + 0.5;
    return color;
}


vec3 brightness_PerPixel( vec3 color,  float brightness){
     color  += vec3(brightness, brightness, brightness);
     return color;
}

vec3 hue_PerPixel( vec3 color,  float hue){
       float angle = hue * 3.14159265;
       float s = sin(angle);
       float c = cos(angle);
       vec3 weights = (vec3(2.0 * c, -sqrt(3.0) * s - c, sqrt(3.0) * s - c) + 1.0) / 3.0;
       float len = length(color);
       color  = vec3(dot(color, weights.xyz), dot(color, weights.zxy), dot(color, weights.yzx));
       return color;
}

vec3 saturate_PerPixel(  vec3 color,   float sat){
	vec3 luminanceW = vec3(0.229, 0.587, 0.114); //luminance weights standard
	vec3 color2 = vec3( dot(color , luminanceW) );
        color  = vec3( mix(color , color2, -sat) );
        return color;
}


vec3 invert_PerPixel(vec3 color, float invFac)
   {
         color.r = abs(invFac - color.r);
        color.g = abs(invFac - color.g);
        color.b = abs(invFac - color.b);
        return color;
   }

vec3 red_PerPixel(vec3 color, float redFac)
   {
    color.r *=redFac;
    return color;
   }
 vec3 green_PerPixel(vec3 color, float greenFac)
   {
    color.g *=greenFac;
    return color;
   }
 vec3 blue_PerPixel(vec3 color, float blueFac)
   {
    color.b *=blueFac;
    return color;
   }
   
  vec3 gammaCorrection(vec3 color, float gamma)
   {
	// gamma correction 
	color = pow(color, vec3(1.0/gamma)); 
	return color;
   }

 
void main() {
       vec4 color = texture2D(m_Texture, texCoord);
       vec3 texVal=color.rgb; 
        #ifdef USE_CONTRAST
        texVal=contrast_PerPixel(texVal,m_Contrast);
       #endif

       #ifdef USE_BRIGHTNESS
        texVal=brightness_PerPixel(texVal, m_Brightness);
       #endif

       #ifdef USE_HUE
        texVal=hue_PerPixel(texVal, m_Hue);
       #endif

       #ifdef USE_SATURATION
       texVal=saturate_PerPixel(texVal, m_Saturation);
       #endif
       
       #ifdef USE_INVERT
       texVal=invert_PerPixel(texVal, m_Invert);
       #endif
       
       #ifdef USE_RED
       texVal=red_PerPixel(texVal, m_Red);
       #endif
       
       #ifdef USE_GREEN
       texVal=green_PerPixel(texVal, m_Green);
       #endif
       
       #ifdef USE_BLUE
       texVal=blue_PerPixel(texVal, m_Blue);
       #endif
        
        #ifdef USE_GAMMA
       texVal= gammaCorrection( texVal,  m_Gamma) ;
       #endif
       

      gl_FragColor = vec4(texVal,1.0);
}