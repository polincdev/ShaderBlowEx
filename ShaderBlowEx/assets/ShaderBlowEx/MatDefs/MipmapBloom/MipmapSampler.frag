#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform sampler2D m_Texture;  // The texture to make a mipmap from.
uniform float m_Dx;           // The step size in x direction in (u,v) space.
uniform float m_Dy;           // The step size in y direction in (u,v) space.
varying vec2 texCoord;        // The texture coordinate of the center pixel.


/**
 * Sample the texture at the center pixel and its eight neighbors.
 * The center pixel's weight is slightly higher.
 */
// =============================================================================
   void main()
// =============================================================================
{
#ifdef MULTISAMPLE
   gl_FragColor=0.25*(texture2D(m_Texture, texCoord+vec2(-m_Dx,-m_Dy))
    +texture2D(m_Texture, texCoord+vec2( m_Dx,-m_Dy))
    +texture2D(m_Texture, texCoord+vec2( m_Dx, m_Dy))
    +texture2D(m_Texture, texCoord+vec2(-m_Dx, m_Dy)));
#else
   gl_FragColor=texture2D(m_Texture, texCoord);
#endif

} // main ======================================================================