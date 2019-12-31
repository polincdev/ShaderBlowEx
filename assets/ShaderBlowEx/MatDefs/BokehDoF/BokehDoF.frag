#import "Common/ShaderLib/GLSLCompat.glsllib"
//Credits
//http://tuxedolabs.blogspot.com/2018/05/bokeh-depth-of-field-in-single-pass.html
//https://mynameismjp.wordpress.com/the-museum/samples-tutorials-tools/depth-of-field-sample/
//https://github.com/TheRealMJP/BakingLab/blob/master/BakingLab/PostProcessing.hlsl#L160

uniform sampler2D m_Texture;
uniform sampler2D m_DepthTexture;
varying vec2 texCoord;
 

uniform float m_XScale;
uniform float m_YScale;
uniform vec2 g_FrustumNearFar;
 
 vec2 uPixelSize=vec2(m_XScale,m_YScale); //The size of a pixel: vec2(1.0/width, 1.0/height)
 float uFar; // Far plane
uniform float m_FocusPoint;
uniform float m_BlurSize;  //5.
uniform float m_RadiusScale;// Smaller = nicer blur, larger = faster 0.5
uniform float m_FocusScale;

const float GOLDEN_ANGLE = 2.39996323;
 

float getBlurSize(float depth, float focusPoint, float focusScale)
{
 float coc = clamp((1.0 / focusPoint - 1.0 / depth)*focusScale, -1.0, 1.0);
 return abs(coc) * m_BlurSize;
}

vec3 depthOfField(vec2 texCoord, float focusPoint, float focusScale)
{
 float centerDepth = texture2D(m_DepthTexture, texCoord).r * uFar;
 float centerSize = getBlurSize(centerDepth, focusPoint, focusScale);
 vec3 color = texture2D(m_Texture, texCoord).rgb;
 float tot = 1.0;

 float radius = m_RadiusScale;
 for (float ang = 0.0; radius<m_BlurSize; ang += GOLDEN_ANGLE)
    {
     vec2 tc = texCoord + vec2(cos(ang), sin(ang)) * uPixelSize * radius;

     vec3 sampleColor = texture2D(m_Texture, tc).rgb;
     float sampleDepth = texture2D(m_DepthTexture, tc).r * uFar;
     float sampleSize = getBlurSize(sampleDepth, focusPoint, focusScale);
     if (sampleDepth > centerDepth)
      sampleSize = clamp(sampleSize, 0.0, centerSize*2.0);

     float m = smoothstep(radius-0.5, radius+0.5, sampleSize);
     color += mix(color/tot, sampleColor, m);
     tot += 1.0;
     radius += m_RadiusScale/radius;
    }
 return color /= tot;
}
void main() {

   
    float zBuffer = texture2D( m_DepthTexture, texCoord ).r;
    //
    float a = g_FrustumNearFar.y / (g_FrustumNearFar.y - g_FrustumNearFar.x);
    float b = g_FrustumNearFar.y * g_FrustumNearFar.x / (g_FrustumNearFar.x - g_FrustumNearFar.y);
    float z = b / (zBuffer - a);
    //
    uFar=z;
    vec3 color=depthOfField(texCoord, m_FocusPoint, m_FocusScale);
    //
    gl_FragColor = vec4(color,1.0);
 
    }
 