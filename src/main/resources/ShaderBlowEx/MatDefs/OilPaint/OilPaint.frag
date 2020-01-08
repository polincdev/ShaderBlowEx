#import "Common/ShaderLib/GLSLCompat.glsllib"
 //Credits 
 //https://www.reddit.com/r/shaders/comments/5e7026/help_making_an_oil_paint_post_processing_shader/
 
 
uniform sampler2D m_Texture;
varying vec2 texCoord;
uniform vec2 g_Resolution;
uniform int m_Strength ;
float   minSigma =  4.7;
void main() {

    
   
    float   strengthSquare = float((m_Strength + 1) * (m_Strength + 1));
    vec3    colorVar[4];
    vec3    colorSquare[4];

    for (int lIndex = 0; lIndex < 4; lIndex++)
    {
        colorVar[lIndex] = vec3(0.0);
        colorSquare[lIndex] = vec3(0.0);
    }

    for (int j = -m_Strength; j <= 0; j++)
    {
        for (int i = -m_Strength; i <= 0; i++)
        {
            vec3    texColor = texture2D(m_Texture, (gl_FragCoord.xy + vec2(i,j)) / g_Resolution).rgb;
            colorVar[0] += texColor;
            colorSquare[0] += texColor * texColor;
        }
    }

    for (int j = -m_Strength; j <= 0; j++)
    {
        for (int i = 0; i <= m_Strength; i++)
        {
            vec3    texColor = texture2D(m_Texture, (gl_FragCoord.xy + vec2(i,j)) / g_Resolution).rgb;
            colorVar[1] += texColor;
            colorSquare[1] += texColor * texColor;
        }
    }

    for (int j = 0; j <= m_Strength; j++)
    {
        for (int i = 0; i <= m_Strength; i++)
        {
            vec3    texColor = texture2D(m_Texture, (gl_FragCoord.xy + vec2(i,j)) / g_Resolution).rgb;
            colorVar[2] += texColor;
            colorSquare[2] += texColor * texColor;
        }
    }

    for (int j = 0; j <= m_Strength; j++)
    {
        for (int i = -m_Strength; i <= 0; i++)
        {
            vec3    texColor = texture2D(m_Texture, (gl_FragCoord.xy + vec2(i,j)) / g_Resolution).rgb;
            colorVar[3] += texColor;
            colorSquare[3] += texColor * texColor;
        }
    }

   

    for (int i = 0; i < 4; i++)
    {
        colorVar[i] /= strengthSquare;
        colorSquare[i] = abs(colorSquare[i] / strengthSquare - colorVar[i] * colorVar[i]);
        float   sigmaVal = colorSquare[i].r + colorSquare[i].g + colorSquare[i].b;
        if (sigmaVal < minSigma)
        {
            minSigma = sigmaVal;
            gl_FragColor = vec4(colorVar[i], texture2D(m_Texture, gl_FragCoord.xy/g_Resolution).a);
        }
    }
   
      
      
 
    }
 