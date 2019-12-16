#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"

uniform sampler2D m_Texture;
uniform sampler2D m_DepthTexture;
varying vec2 texCoord;

uniform vec4 m_AirColor;
uniform float m_AirDensity;
uniform float m_AirDistance;
uniform float m_AirDesaturation;

const float LOG2 = 1.442695;

vec3 saturate(  vec3 color,   float sat){
	vec3 luminanceW = vec3(0.229, 0.587, 0.114);  
	vec3 color2 = vec3( dot(color , luminanceW) );
        color  = vec3( mix(color , color2, -sat) );
        return color;
}
void main() {
       vec2 m_FrustumNearFar=vec2(1.0,m_AirDistance);
       vec4 texVal = texture2D(m_Texture, texCoord);
       float fogVal = texture2D(m_DepthTexture,texCoord).r;
       float depth= (2.0 * m_FrustumNearFar.x) / (m_FrustumNearFar.y + m_FrustumNearFar.x - fogVal* (m_FrustumNearFar.y-m_FrustumNearFar.x));

       float factor = exp2( -m_AirDensity * m_AirDensity * depth *  depth * LOG2 );
       float fogFactor = clamp(factor, 0.5, 1.0);
      texVal.rgb=saturate(texVal.rgb,clamp(-1. + factor/m_AirDesaturation , -1., .0) );
     
       gl_FragColor = mix(m_AirColor,texVal,fogFactor);

}