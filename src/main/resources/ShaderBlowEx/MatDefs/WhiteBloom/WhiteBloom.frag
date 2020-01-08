#import "Common/ShaderLib/GLSLCompat.glsllib"
 
 
uniform sampler2D m_Texture;
varying vec2 texCoord;
uniform float m_Strength;  
uniform float m_Scale;
uniform vec2 g_Resolution;
  

vec3 getTex(vec2 uv,float m){
    return pow(texture2D(m_Texture,uv,m).rgb,vec3(2.2));
}

 vec3 whiteBloom (vec2 uv,vec2 res){
    
    float scale = exp2(m_Strength);
    vec3 c =
        getTex((uv+vec2(-1.5,-0.5)*scale)/res,m_Strength)*.1+
        getTex((uv+vec2( 0.5,-1.5)*scale)/res,m_Strength)*.1+
        getTex((uv+vec2( 1.5, 0.5)*scale)/res,m_Strength)*.1+
        getTex((uv+vec2(-0.5, 1.5)*scale)/res,m_Strength)*.1+
        getTex((uv)/res,m_Strength)*.7+
        getTex(uv/res,0.)*.7;
    return c;
}
 
    
void main() {
 
  float blurScale = 1.0/m_Scale;
  
  vec4 sum = vec4(0);
  //
   // sum =  vec4(whiteBloom(gl_FragCoord.xy, g_Resolution.xy),1.0);  
    
   sum += texture2D(m_Texture, vec2(texCoord.x - 4.0*blurScale, texCoord.y)) * 0.05;
   sum += texture2D(m_Texture, vec2(texCoord.x - 3.0*blurScale, texCoord.y)) * 0.09;
   sum += texture2D(m_Texture, vec2(texCoord.x - 2.0*blurScale, texCoord.y)) * 0.12;
   sum += texture2D(m_Texture, vec2(texCoord.x - blurScale, texCoord.y)) * 0.15;
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y)) * 0.16;
   sum += texture2D(m_Texture, vec2(texCoord.x + blurScale, texCoord.y)) * 0.15;
   sum += texture2D(m_Texture, vec2(texCoord.x + 2.0*blurScale, texCoord.y)) * 0.12;
   sum += texture2D(m_Texture, vec2(texCoord.x + 3.0*blurScale, texCoord.y)) * 0.09;
   sum += texture2D(m_Texture, vec2(texCoord.x + 4.0*blurScale, texCoord.y)) * 0.05;
	
	 
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y - 4.0*blurScale)) * 0.05;
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y - 3.0*blurScale)) * 0.09;
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y - 2.0*blurScale)) * 0.12;
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y - blurScale)) * 0.15;
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y)) * 0.16;
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y + blurScale)) * 0.15;
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y + 2.0*blurScale)) * 0.12;
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y + 3.0*blurScale)) * 0.09;
   sum += texture2D(m_Texture, vec2(texCoord.x, texCoord.y + 4.0*blurScale)) * 0.05;

   // 
   gl_FragColor = sum*m_Strength + texture2D(m_Texture, texCoord); 
     
    }
 