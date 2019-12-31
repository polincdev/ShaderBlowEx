#import "Common/ShaderLib/GLSLCompat.glsllib"
 //Credits
 //https://rosenzweig.io/blog/monotone-portraits-with-glsl.html
//https://www.geeks3d.com/20091027/shader-library-posterization-post-processing-effect-glsl/

uniform sampler2D m_Texture;
varying vec2 texCoord;
uniform float m_Step;
//float gamma=1.0;  
 



void main() 
 
  { 
    vec3 c = texture2D(m_Texture, texCoord).rgb;
    //c = pow(c, vec3(gamma, gamma, gamma));
    c = c * m_Step;
    c = floor(c);
    c = c / m_Step;
   // c = pow(c, vec3(1.0/gamma));
    gl_FragColor = vec4(c, 1.0);

 
    }
 