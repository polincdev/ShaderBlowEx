#import "Common/ShaderLib/GLSLCompat.glsllib"
 //Credits
 //https://www.geeks3d.com/20140204/glsl-volumetric-light-post-processing-filter-for-webcam-video/
 uniform sampler2D m_Texture;
 varying vec2 texCoord;
 uniform  float m_Strength;
 uniform float m_Brightness;

 
 /*---------------------------------------------------------------------------*/
 
 #define T texture2D(m_Texture,.5+(p.xy*=.992))
/*----------------------------------------------------------------------------*/
void main() {
  
    vec2 p = texCoord.xy-.5;
    vec3 o = T.rgb;
  
   float pz=0.0;
   for (float i=0.;i<m_Strength;i++) 
        pz += pow(max(0.,0.+length(T.rgb)/3.0),2.0+(1.-m_Brightness))*exp(-i*.08);
  gl_FragColor=vec4( o+pz*.5,1);
  
     
    
}
