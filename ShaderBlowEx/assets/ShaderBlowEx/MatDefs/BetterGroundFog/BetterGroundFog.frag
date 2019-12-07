#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"

//Credits
//https://hub.jmonkeyengine.org/t/bettergroundfogfilter/41452
// by Alexander Kasigkeit  
// based on by Eric Lengyel - Terathon Software - lengyel@terathon.com http://www.terathon.com/lengyel/Lengyel-UnifiedFog.pdf
// position calculation and multisampling taken from Remy Bouquet (nehon)'s Water pixel shader https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/jme3-effects/src/main/resources/Common/MatDefs/Water/Water.frag

uniform mat4 g_ViewProjectionMatrixInverse;
uniform vec3 g_CameraPosition;

uniform COLORTEXTURE m_Texture;
uniform DEPTHTEXTURE m_DepthTexture;

uniform float m_FogDensity; 
uniform vec4 m_FogColor; 
uniform vec4 m_FogBoundary; 
uniform float m_K; 
uniform float m_CF;

#ifdef GROUND
    uniform float m_GroundLevel;
#endif
#ifdef SUN
    uniform vec3 m_SunDirection; 
    uniform vec4 m_SunColor; 
    uniform float m_SunShininess;
#endif

in vec2 texCoord;
out vec4 color;

vec4 main_multiSample(in int sampleNum){
    vec4 colorTex = fetchTextureSample(m_Texture, texCoord, sampleNum);
    float depth = fetchTextureSample(m_DepthTexture, texCoord, sampleNum).r;
    vec4 fragPos = g_ViewProjectionMatrixInverse * (vec4(texCoord, depth, 1.0) * 2.0 - 1.0);
    vec3 wsFragPos = fragPos.xyz /  fragPos.w;
    vec3 wsFragDelta = g_CameraPosition - wsFragPos;
    #ifdef GROUND
        //adjust frags worldspace position in case its below groundLevel
        float d = 1.0 - max(0.0, m_GroundLevel - wsFragPos.y) / wsFragDelta.y;
        wsFragPos = g_CameraPosition + d * -wsFragDelta;
        wsFragDelta = g_CameraPosition - wsFragPos;
    #endif
    vec3 wsFragDir = normalize(wsFragDelta);
    //calculate fog color based on viewDirection and sunDirection
    //this does not work for indoors, the fogs color would still change when looking into the direction of the sun
    float t = 0.0;
    #ifdef SUN
        t = pow(depth, m_SunShininess * m_SunShininess);
        t = pow(t * max( dot( wsFragDir, m_SunDirection ), 0.0 ), m_SunShininess);
    #endif
    vec3 colorFog = mix( m_FogColor.rgb, 
                         m_SunColor.rgb, 
                         t );
    //calculate amount to apply to this fragment
    float FP = dot(m_FogBoundary, vec4(wsFragPos, 1.0)); 
    float fc = min((1.0 - 2.0 * m_K) * FP, 0.0); 
    fc = -length((m_FogDensity * 0.01) * wsFragDelta) * ((m_K * (FP + m_CF)) - pow(fc, 2.0) / abs(dot(m_FogBoundary, vec4(wsFragDelta, 0.0)))); 
    fc = (1.0 - clamp(exp2(-fc), 0.0, 1.0));
    #ifdef GROUND
        fc *= step(m_GroundLevel, g_CameraPosition.y);
    #endif
    return vec4(mix(colorTex.rgb, 
                    colorFog, 
                    fc), 
                1.0); 
}

void main() {
    #ifdef RESOLVE_MS
        vec4 col = vec4(0.0);
        for (int i = 0; i < m_NumSamples; i++){
            col += main_multiSample(i);
        }
        color = col / m_NumSamples;
    #else
        color = main_multiSample(0);
    #endif
}