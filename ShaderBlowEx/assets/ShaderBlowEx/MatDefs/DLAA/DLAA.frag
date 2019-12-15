#import "Common/ShaderLib/GLSLCompat.glsllib"
 //Credits 
 //https://github.com/microsoft/Imagine_fudge-roll
 //https://github.uconn.edu/eec09006/breakout
 
 
uniform sampler2D m_Texture;
varying vec2 texCoord;
uniform vec2 g_Resolution;
vec3 oneThird = vec3(0.33, 0.33, 0.33) ;

#define PXL_SIZE vec2(1.0/g_Resolution.x, 1.0/g_Resolution.y)

vec4 LD(  sampler2D mainTex,   vec2 texCoord,  vec2 dOffset )
{
   return texture2D(mainTex, ( texCoord + dOffset * PXL_SIZE) );
}

float getIntensity( vec3 value)
{
    return dot(value.xyz, oneThird );
}


vec4 highPassPre( vec2 texCoord )
{
   vec4 sCenter    = LD(m_Texture, texCoord, vec2( 0.0,  0.0) );
   vec4 sUpLeft    = LD(m_Texture, texCoord, vec2(-1.0, -1.0) );
   vec4 sUpRight   = LD(m_Texture, texCoord, vec2( 1.0, -1.0) );
   vec4 sDownLeft  = LD(m_Texture, texCoord, vec2(-1.0,  1.0) );
   vec4 sDownRight = LD(m_Texture, texCoord, vec2( 1.0,  1.0) );
 
   vec4 diff= 4.0 * abs( (sUpLeft + sUpRight + sDownLeft + sDownRight) - 4.0 * sCenter );
   float edgeMask       = getIntensity(diff.xyz);

   return vec4(sCenter.rgb, edgeMask);
}
 
void main() {

     
       vec4 color= texture2D( m_Texture, texCoord ) ;
  
       //
    // Short Edges
    //
        vec4 center     = LD(m_Texture, texCoord, vec2( 0.0,  0.0) );   
        vec4 left_01   = LD(m_Texture, texCoord, vec2(-1.5,  0.0) );
        vec4 right_01   = LD(m_Texture, texCoord, vec2( 1.5,  0.0) ); 
        vec4 top_01   = LD(m_Texture, texCoord, vec2( 0.0, -1.5) ); 
        vec4 bottom_01   = LD(m_Texture, texCoord, vec2( 0.0,  1.5) );

        vec4 w_h = left_01 + right_01;
        vec4 w_v =  top_01  + bottom_01;

         // Softer (5-pixel wide high-pass)
        vec4 edge_h  = abs( w_h  - (2.0 * center) ) / 2.0;  
        vec4 edge_v   = abs( w_h  - (2.0 * center) ) / 2.0;

        float valueEdgeHoriz    = getIntensity( edge_h .xyz );
        float valueEdgeVert     = getIntensity( edge_v .xyz );
        
        float edgeDetectHoriz   = clamp( (3.0 * valueEdgeHoriz) - 0.1,0.0,1.0);
        float edgeDetectVert    = clamp( (3.0 * valueEdgeVert)  - 0.1,0.0,1.0);

        vec4 avgHoriz         	= ( w_h  + center) / 3.0;
        vec4 avgVert            = ( w_v   + center) / 3.0;

        float valueHoriz        = getIntensity( avgHoriz.xyz );
        float valueVert         = getIntensity( avgVert.xyz );

        float blurAmountHoriz   = clamp( edgeDetectHoriz / valueHoriz ,0.0,1.0);
        float blurAmountVert    = clamp( edgeDetectVert  / valueVert ,0.0,1.0);

        vec4 aaResult         	= mix( center,  avgHoriz, blurAmountHoriz );
        aaResult                = mix( aaResult,       avgVert,  blurAmountVert );
    //
     // Long Edges
     //
      // sample 16x16 cross 
        vec4 sampleVertNeg1   = LD(m_Texture, texCoord, vec2(0.0, -3.5) ); 
        vec4 sampleVertNeg2   = LD(m_Texture, texCoord, vec2(0.0, -7.5) );
        vec4 sampleVertPos1   = LD(m_Texture, texCoord, vec2(0.0,  3.5) ); 
        vec4 sampleVertPos2   = LD(m_Texture, texCoord, vec2(0.0,  7.5) ); 

        vec4 sampleHorizNeg1   = LD(m_Texture, texCoord, vec2(-3.5, 0.0) ); 
        vec4 sampleHorizNeg2   = LD(m_Texture, texCoord, vec2(-7.5, 0.0) );
        vec4 sampleHorizPos1   = LD(m_Texture, texCoord, vec2( 3.5, 0.0) ); 
        vec4 sampleHorizPos2   = LD(m_Texture, texCoord, vec2( 7.5, 0.0) ); 

        float pass1EdgeAvgHoriz  = ( sampleHorizNeg2.a + sampleHorizNeg1.a + center.a + sampleHorizPos1.a + sampleHorizPos2.a ) / 5.0;
        float pass1EdgeAvgVert   = ( sampleVertNeg2.a  + sampleVertNeg1.a  + center.a + sampleVertPos1.a  + sampleVertPos2.a  ) / 5.0;
        pass1EdgeAvgHoriz        = clamp( pass1EdgeAvgHoriz * 2.0 - 1.0 ,0.0,1.0);
        pass1EdgeAvgVert         = clamp( pass1EdgeAvgVert  * 2.0 - 1.0 ,0.0,1.0);
        float longEdge           = max( pass1EdgeAvgHoriz, pass1EdgeAvgVert);

        if ( longEdge > 1.0 )
        {
        vec4 avgHorizLong = ( sampleHorizNeg2 + sampleHorizNeg1 + center + sampleHorizPos1 + sampleHorizPos2 ) / 5.0;
        vec4 avgVertLong   = ( sampleVertNeg2  + sampleVertNeg1  + center + sampleVertPos1  + sampleVertPos2  ) / 5.0;
        float valueHorizLong   	= getIntensity(avgHorizLong.xyz);
        float valueVertLong     = getIntensity(avgVertLong.xyz);

        vec4 sampleLeft = LD(m_Texture, texCoord, vec2(-1.0,  0.0) );
        vec4 sampleRight= LD(m_Texture, texCoord, vec2( 1.0,  0.0) );
        vec4 sampleUp = LD(m_Texture, texCoord, vec2( 0.0, -1.0) );
        vec4 sampleDown = LD(m_Texture, texCoord, vec2( 0.0,  1.0) );

        float valueCenter= getIntensity(center.xyz);
        float valueLeft= getIntensity(sampleLeft.xyz);
        float valueRight = getIntensity(sampleRight.xyz);
        float valueTop = getIntensity(sampleUp.xyz);
        float valueBottom       = getIntensity(sampleDown.xyz);

        vec4 diffToCenter = valueCenter - vec4(valueLeft, valueTop, valueRight, valueBottom);      
        float blurAmountLeft = clamp( 0.0 + ( valueVertLong  - valueLeft   ) / diffToCenter.x ,0.0,1.0);
        float blurAmountUp= clamp( 0.0 + ( valueHorizLong - valueTop      ) / diffToCenter.y ,0.0,1.0);
        float blurAmountRight= clamp( 1.0 + ( valueVertLong  - valueCenter ) / diffToCenter.z ,0.0,1.0);
        float blurAmountDown = clamp( 1.0 + ( valueHorizLong - valueCenter ) / diffToCenter.w ,0.0,1.0);     

        vec4 blurAmounts = vec4( blurAmountLeft, blurAmountRight, blurAmountUp, blurAmountDown );
        blurAmounts  = (blurAmounts == vec4(0.0, 0.0, 0.0, 0.0)) ? vec4(1.0, 1.0, 1.0, 1.0) : blurAmounts;

        vec4 longBlurHoriz = mix( sampleLeft,  center,  blurAmounts.x );
        longBlurHoriz = mix( sampleRight, longBlurHoriz, blurAmounts.y );
        vec4 longBlurVert  = mix( sampleUp,  center,  blurAmounts.z );
        longBlurVert= mix( sampleDown,  longBlurVert,  blurAmounts.w );

        aaResult  = mix( aaResult, longBlurHoriz, pass1EdgeAvgVert);
        aaResult  = mix( aaResult, longBlurVert,  pass1EdgeAvgHoriz);
        }

     gl_FragColor = vec4(aaResult.rgb, 1.0);
      
 
    }
 