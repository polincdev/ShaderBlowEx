uniform float g_Time;

uniform sampler2D m_Texture;
 
uniform float m_InnerVignetting;
uniform float m_OuterVignetting;
uniform  float m_VignetteStrength;//=0.5;
uniform  float m_VignetteExtent;//=50.;
varying vec2 texCoord;
 
 
void main() {
   
    vec3 color = texture2D(m_Texture, texCoord).rgb;
    vec3 finalColour = color;
    vec2 uv = texCoord;
    uv *=  1.0 - uv.yx;   
    float vig = uv.x*uv.y * (100.0-m_VignetteExtent );  
     vig = pow(vig, m_VignetteStrength );  
    gl_FragColor = vec4(finalColour*vig,1.0); 
    
}