 #import "Common/ShaderLib/GLSLCompat.glsllib"

 uniform float g_Time;
uniform sampler2D m_Texture;
 
uniform float m_InnerVignetting;
uniform float m_OuterVignetting;
uniform  float m_VignetteStrength;//=0.5;
uniform  float m_VignetteExtent;//=50.;
varying vec2 texCoord;
uniform float m_BlurQuality;//=20.0
uniform float m_BlurSize;//=0.1
uniform float m_GrayPower;// = 2.0; 
uniform float m_GrayMargin;// = 4.0;


float blendAdd(float base, float blend) {
	return min(base+blend,1.0);
}

vec3 blendAdd(vec3 base, vec3 blend) {
	return min(base+blend,vec3(1.0));
}

vec3 blendAdd(vec3 base, vec3 blend, float opacity) {
	return (blendAdd(base, blend) * opacity + base * (1.0 - opacity));
}
void main() {
   
    //
    vec3 color = texture2D(m_Texture, texCoord).rgb;
    vec3 finalColour = color;
    vec2 uv = texCoord;
    uv *=  1.0 - uv.yx;   
    float vig = uv.x*uv.y * (100.0-m_VignetteExtent );  
     vig = pow(vig, m_VignetteStrength );  
    gl_FragColor = vec4(finalColour*vig,1.0); 
      
    //
     uv = texCoord;
     color = texture2D(m_Texture, texCoord).rgb;
   float power = (distance(uv, vec2(0.5)) * distance(uv, vec2(0.5))) * -abs(  m_BlurSize);
    vec2 dir = normalize(uv - vec2(0.5, 0.5)) * power;
    vec3 finalCol = vec3(0.0);
    for(float i = 0.; i < m_BlurQuality; ++i)
       {
        finalCol += texture2D( m_Texture, uv + dir * (i / m_BlurQuality) ).rgb / m_BlurQuality;
       }
    gl_FragColor=vec4(blendAdd(finalCol,gl_FragColor.rgb,0.5),1.0); 
     //gl_FragColor+=vec4(finalCol, 1.0);
     //
    
    uv = texCoord;
     float vignette = distance(uv, vec2(0.5) );
    vec3 grey = vec3( dot(gl_FragColor.rgb, vec3(0.3, 0.59, 0.11))); 
    gl_FragColor.rgb = mix(grey, gl_FragColor.rgb, clamp((5.0-m_GrayPower)- vignette * m_GrayMargin, 0.0, 1.0));
    gl_FragColor=vec4(gl_FragColor.rgb, 1.0);
 
    
}