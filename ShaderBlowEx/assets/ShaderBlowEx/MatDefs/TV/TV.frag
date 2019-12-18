#import "Common/ShaderLib/GLSLCompat.glsllib"
//Credits 
 
uniform float g_Time;
uniform sampler2D m_Texture;
 uniform vec2 g_Resolution;
 varying vec2 texCoord;
 uniform  bool m_EnabledVHS;
 uniform  bool m_EnabledLine;
  uniform  bool m_EnabledGrain;
 uniform   bool m_EnabledScanline;
 uniform   bool m_EnabledVignette;
  float lineHeight = 5.;
 float lineSpeed = 5.0;
 float lineOverflow = 1.4;
  float noise = .70;
  float pixelDensity = 450.;
   
 float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}
void main() {
    
    vec2 uv = texCoord;
   vec3 col=vec3(texture2D(m_Texture,uv));
     
     //VHS dirt
    if(m_EnabledVHS)
    {
        vec2 pos=vec2(0.5+0.5*sin(g_Time),uv.y);
       vec3 col2=vec3(texture2D(m_Texture,pos))*0.2;
       col+=col2;
    }
    
     // Moving strip effect
    if(m_EnabledLine)
     {
      float blurLine = clamp(sin(uv.y * lineHeight + g_Time * lineSpeed) + 1.22, 0., 1.);
      float line = clamp(floor(sin(uv.y * lineHeight + g_Time * lineSpeed) + 1.90), 0., lineOverflow);
      col = mix(col - noise * vec3(.08), col, line);
      col = mix(col - noise * vec3(.25), col, blurLine);
    }
    
    //Grain 
    if(m_EnabledGrain)
      {
      col *= vec3(clamp(rand(vec2(floor(uv.x * pixelDensity ), floor(uv.y * pixelDensity)) *g_Time / 1000.) + 1. - noise, 0., 1.));
     }
     
    //Scanlines
   if(m_EnabledScanline)
     {
      float d = length(uv - vec2(0.5,0.5));
      float scanline = sin(uv.y* g_Resolution.y )*0.04;
      col  -= scanline;
    }
    
     if(m_EnabledVignette)
      {
      // Vignette
       col *= vec3(1.0 - pow(distance(uv, vec2(0.5, 0.5)), 3.0) * 3.0);
     }	 
         
     gl_FragColor = vec4(col ,1.0);
}