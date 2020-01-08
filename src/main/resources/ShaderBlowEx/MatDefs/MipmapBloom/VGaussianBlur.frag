#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform sampler2D m_Texture;  // The texture to be blurred.
uniform float m_Size;         // The height of the texture.
uniform float m_Scale;        // The step size in pixels.
varying vec2 texCoord;        // Texture coordinate.


// =============================================================================
   void main(void)
// =============================================================================
{  float blurSize = m_Scale/m_Size;
   vec4 sum = vec4(0.0);

// Take nine samples, with the distance (u,v) blurSize between them
   vec2 delta=blurSize*vec2(0.0,1.0);

   sum+=texture2D(m_Texture, texCoord.xy-4.0*delta)*0.06;
   sum+=texture2D(m_Texture, texCoord.xy-3.0*delta)*0.09;
   sum+=texture2D(m_Texture, texCoord.xy-2.0*delta)*0.12;
   sum+=texture2D(m_Texture, texCoord.xy-delta)*0.15;
   sum+=texture2D(m_Texture, texCoord.xy)*0.16;
   sum+=texture2D(m_Texture, texCoord.xy+delta)*0.15;
   sum+=texture2D(m_Texture, texCoord.xy+2.0*delta)*0.12;
   sum+=texture2D(m_Texture, texCoord.xy+3.0*delta)*0.09;
   sum+=texture2D(m_Texture, texCoord.xy+4.0*delta)*0.06;

   gl_FragColor=sum;
} // main ======================================================================