 
 #import "Common/ShaderLib/GLSLCompat.glsllib"
 //CREDITS
 //https://github.com/cansik/processing-postfx
 //https://www.shadertoy.com/view/XsjSzR
 //http://unusedino.de/ec64/technical/misc/vic656x/colors/
 
uniform vec4 m_EdgeColor;

uniform float m_EdgeWidth;
uniform float m_EdgeIntensity;

uniform float m_NormalThreshold;
uniform float m_DepthThreshold;

uniform float m_NormalSensitivity;
uniform float m_DepthSensitivity;

varying vec2 texCoord;

uniform sampler2D m_Texture;
uniform sampler2D m_NormalsTexture;
uniform sampler2D m_DepthTexture;

uniform vec2 g_ResolutionInverse;
uniform vec2 g_Resolution;
 //TOON
uniform float m_ColorSize ;
uniform float m_ColorCount ;
uniform float m_PixelResolution ;


//EDGE
vec4 fetchNormalDepth(vec2 tc){
    vec4 nd;
    nd.xyz = texture2D(m_NormalsTexture, tc).rgb;
    nd.w   = texture2D(m_DepthTexture,   tc).r;
    return nd;
}


vec4 toonify(vec4 clr)
{
    for (float y = -m_ColorSize; y <= m_ColorSize; ++y)
                {
		for (float x = -m_ColorSize; x <= m_ColorSize; ++x)
                        {
			clr += texture2D(m_Texture, (texCoord + vec2(x, y)/g_Resolution) );
                        }
                }

	clr /= float((2.0 * m_ColorSize + 1.0) * (2.0 * m_ColorSize + 1.0));
	for (int c = 0; c < 3; ++c)
            {
		clr[c] = floor(m_ColorCount * clr[c]) / m_ColorCount;
            }
    
      return clr;
    }
  

 #define RGB(r, g, b) vec3(float(r)/255., float(g)/255., float(b)/255.)
 #define NUM_COLORS 16
vec3 palette[NUM_COLORS];

//  
void fillPaletteC64()
{
    palette[0]  = RGB(0, 0, 0);
    palette[1]  = RGB(255, 255, 255);
    palette[2]  = RGB(116, 67, 53);
    palette[3]  = RGB(124, 172, 186);
    palette[4]  = RGB(123, 72, 144);
    palette[5]  = RGB(100, 151, 79);
    palette[6]  = RGB(64, 50, 133);
    palette[7]  = RGB(191, 205, 122);
    palette[8]  = RGB(123, 91, 47);
    palette[9]  = RGB(79, 69, 0);
    palette[10] = RGB(163, 114, 101);
    palette[11] = RGB(80, 80, 80);
    palette[12] = RGB(120, 120, 120);
    palette[13] = RGB(164, 215, 142);
    palette[14] = RGB(120, 106, 189);
    palette[15] = RGB(159, 159, 150);
}
void fillPaletteLinear()
{
    palette[0]  = vec3(0, 0, 0); // black;
    palette[1]  =  vec3(0.5, 0, 0); // maroon
    palette[2]  = vec3(0, 0.5, 0); // green
    palette[3]  =  vec3(0.5, 0.5, 0); // olive
    palette[4]  = vec3(0, 0, 0.5); // navy
    palette[5]  =  vec3(0.5, 0, 0.5); // purple
    palette[6]  = vec3(0, 0.5, 0.5); // teal
    palette[7]  = vec3(0.75, 0.75, 0.75); //silver
    palette[8]  = vec3(0.5, 0.5, 0.5); // gray
    palette[9]  =  vec3(1, 0, 0); // red
    palette[10] = vec3(0, 0, 1); // lime
    palette[11] = vec3(1, 1, 0); // yellow
    palette[12] = vec3(0, 0, 1); // blue
    palette[13] =  vec3(1, 0, 1); // fuchsia
    palette[14] = vec3(0, 1, 1); // aqua
    palette[15] = vec3(0, 0, 0); // white
}
 void fillPaletteCC()
{
     palette[ 0] = vec3(0., 0., 0. );
    palette[ 1] = vec3(76./255., 76./255., 76./255.);
    palette[ 2] = vec3(37./255., 49./255., 146./255.);
    palette[ 3] = vec3(37./255., 49./255., 146./255.);
    palette[ 4] = vec3(87./255., 166./255., 78./255.);
    palette[ 5] = vec3(127./255., 204./255., 25./255.);
    palette[ 6] = vec3(76./255., 153./255., 178./255.);
    palette[ 7] = vec3(153./255., 178./255., 242./255.);
    palette[ 8] = vec3(204./255., 76./255., 76./255.);
    palette[ 9] = vec3(204./255., 76./255., 76./255.);
    palette[10] = vec3(178./255., 102./255., 229./255.);
    palette[11] = vec3(229./255., 127./255., 216./255.);
    palette[12] = vec3(127./255., 102./255., 76./255.);
    palette[13] = vec3(222./255., 222./255., 108./255.);
    palette[14] = vec3(153./255., 153./255., 153./255.);
    palette[15] = vec3(240./255., 240./255., 240./255. );
    
 }
 void fillPaletteCGA()
{
    palette[0]  = RGB(0, 0, 0);
    palette[1]  = RGB(85, 85, 85);
    palette[2]  = RGB(0, 0, 170);
    palette[3]  = RGB(0, 170, 0); 
    palette[4]  = RGB(170, 0, 0); 
    palette[5]  = RGB(170, 85, 0); 
    palette[6]  = RGB(0, 170, 170); 
    palette[7]  = RGB(191, 205, 122); 
    palette[8]  = RGB(170, 170, 170); 
    palette[9]  = RGB(85, 85, 255);
    palette[10] = RGB(85, 255, 85);
    palette[11] = RGB(255, 85, 85);
    palette[12] = RGB(85,255, 255);
    palette[13] = RGB(255, 85, 255);
    palette[14] = RGB(255, 255, 85);
    palette[15] = RGB(255, 255, 255);
}
  

 
float colorDistance(vec3 color1, vec3 color2)
{
    return sqrt(
        pow(color2.r - color1.r, 2.0) +
        pow(color2.g - color1.g, 2.0) +
        pow(color2.b - color1.b, 2.0));
}

vec3 conformColor(vec3 color)
{
    vec3 closestColor = palette[0];
    float currentDistance = 255.0;
    
    for(int i = 0; i <NUM_COLORS; i++)
    {
    	float dist = colorDistance(palette[i], color);
        if(dist < currentDistance)
        {
            currentDistance = dist;
            closestColor = palette[i];
        }
    }
    
    return closestColor;
}

 
 
 void fillHuePalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       {
           vec3 p = abs(fract(t + vec3(1.0, 2.0 / 3.0, 1.0 / 3.0)) * 6.0 - 3.0);
            palette[i] = (clamp(p - 1.0, 0.0, 1.0));
        }
}  
void fillTechPalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       palette[i] = pow(vec3(t + 0.01), vec3(120.0, 10.0, 180.0));	
}  
void fillFirePalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       palette[i] =max(pow(vec3(min(t * 1.02, 1.0)), vec3(1.7, 25.0, 100.0)),  vec3(0.06 * pow(max(1.0 - abs(t - 0.35), 0.0), 5.0)));
}   

void fillDesertPalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
        {
        float s = sqrt(clamp(1.0 - (t - 0.4) / 0.6, 0.0, 1.0));
	vec3 sky = sqrt(mix(vec3(1, 1, 1), vec3(0, 0.8, 1.0), smoothstep(0.4, 0.9, t)) * vec3(s, s, 1.0));
	vec3 land = mix(vec3(0.7, 0.3, 0.0), vec3(0.85, 0.75 + max(0.8 - t * 20.0, 0.0), 0.5), sqrt(t / 0.4));
        palette[i] =clamp((t > 0.4) ? sky : land, 0.0, 1.0) * clamp(1.5 * (1.0 - abs(t - 0.4)), 0.0, 1.0);
        }
}      
void fillElectricPalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       palette[i] =clamp( vec3(t * 8.0 - 6.3, sqrt(smoothstep(0.6, 0.9, t)), pow(t, 3.0) * 1.7), 0.0, 1.0);	
}    
void fillNeonPalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       palette[i] =clamp(vec3(t * 1.3 + 0.1, sqrt(abs(0.43 - t) * 1.7), (1.0 - t) * 1.7), 0.0, 1.0);
}   
void fillHeatmapPalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       palette[i] =clamp((pow(t, 1.5) * 0.8 + 0.2) * vec3(smoothstep(0.0, 0.35, t) + t * 0.5, smoothstep(0.5, 1.0, t), max(1.0 - t * 1.7, t * 7.0 - 6.0)), 0.0, 1.0);
}   
 
void fillRainbowPalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
        {
        vec3 c = 1.0 - pow(abs(vec3(t) - vec3(0.65, 0.5, 0.2)) * vec3(3.0, 3.0, 5.0), vec3(1.5, 1.3, 1.7));
	c.r = max((0.15 - sqrt(abs(t - 0.04) * 5.0)), c.r);
	c.g = (t < 0.5) ? smoothstep(0.04, 0.45, t) : c.g;
         palette[i] =clamp(c, 0.0, 1.0);
        }
}   

 
void fillBrightnessPalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       palette[i] =vec3(t * t);
}   

void fillGrayscalePalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       palette[i] =vec3(t);
}   
 
void fillStripePalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       palette[i] = vec3(mod(floor(t * 32.0), 2.0) * 0.2 + 0.8);
}  
    
void fillAnsiPalette()
{
    float t=0.0;
    for(int i=0;i<16;t=t+0.0625,i++) 
       palette[i] = mod(floor(t * vec3(8.0, 4.0, 2.0)), 2.0);
}  
    

void main(){
    
    //Palette
     #if PALETTE==0
          fillPaletteLinear(); //Linear
     #elif PALETTE==1
          fillPaletteC64(); //C64
     #elif PALETTE==2
          fillPaletteCGA(); //CGA
     #elif PALETTE==3
          fillPaletteCC(); //CGA     
      #elif PALETTE==4
          fillAnsiPalette(); //ANSI     
       #elif PALETTE==5
          fillStripePalette(); //Stripe     
       #elif PALETTE==6
          fillGrayscalePalette(); //Gray     
      #elif PALETTE==7
          fillHeatmapPalette();   
      #elif PALETTE==8
          fillRainbowPalette();      
      #elif PALETTE==9
          fillBrightnessPalette();     
       #elif PALETTE==10
          fillDesertPalette();     
       #elif PALETTE==11
          fillElectricPalette();     
       #elif PALETTE==12
          fillNeonPalette();     
       #elif PALETTE==13
          fillFirePalette();     
       #elif PALETTE==14
          fillTechPalette();     
       #elif PALETTE==15
          fillHuePalette();     
            
           
      #else 
        fillPaletteLinear();
     #endif
  
     // 
    vec4 clr = vec4(0.0);
         
      /////////////////Pixel
    vec2 uv2 = texCoord ;
    float aspectRatio = g_Resolution.x / g_Resolution.y;
     vec2 newRes = vec2(m_PixelResolution);
    newRes.x *= aspectRatio;
    vec3 pal = vec3(m_ColorSize ); //levels per color channel
    uv2 = floor( uv2 * newRes ) / newRes; //the actual magic. 
    //Palette
    //Normal palette vs C4 pallete vs toon
    //vec4 color2 = texture2D( m_Texture, uv2 ); 
      vec4 color2 =vec4(conformColor(texture2D( m_Texture, uv2 ).xyz), 1.0);
   //  vec4 color2 =toonify(texture2D( m_Texture, uv2 ));
     color2.xyz = floor( color2.xyz * pal  ) / pal.xyz;
     clr  = color2;
	 
     /////////////////edge    
     vec3 color = clr.rgb;
     // vec3 color =texture2D(m_Texture, texCoord).rgb;
    vec2 edgeOffset = vec2(m_EdgeWidth) * g_ResolutionInverse;
    vec4 n1 = fetchNormalDepth(uv2 + vec2(-1.0, -1.0) * edgeOffset);
    vec4 n2 = fetchNormalDepth(uv2 + vec2( 1.0,  1.0) * edgeOffset);
    vec4 n3 = fetchNormalDepth(uv2 + vec2(-1.0,  1.0) * edgeOffset);
    vec4 n4 = fetchNormalDepth(uv2 + vec2( 1.0, -1.0) * edgeOffset);
    // Work out how much the normal and depth values are changing.
    vec4 diagonalDelta = abs(n1 - n2) + abs(n3 - n4);
    float normalDelta = dot(diagonalDelta.xyz, vec3(1.0));
    float depthDelta = diagonalDelta.w;
    // Filter out very small changes, in order to produce nice clean results.
    normalDelta = clamp((normalDelta - m_NormalThreshold) * m_NormalSensitivity, 0.0, 1.0);
    depthDelta  = clamp((depthDelta - m_DepthThreshold) * m_DepthSensitivity,    0.0, 1.0);
    // Does this pixel lie on an edge?
    float edgeAmount = clamp(normalDelta + depthDelta, 0.0, 1.0) * m_EdgeIntensity;
    // Apply the edge detection result to the main scene color.
    //color *= (1.0 - edgeAmount);
    color = mix (color,m_EdgeColor.rgb,edgeAmount);
     gl_FragColor = vec4(color, 1.0);
 
 
      //SCANLINES
        // distance  
        float d = length(uv2 - vec2(0.5,0.5));
	 // scanline
	float scanline = sin(uv2.y* m_PixelResolution )*0.04;
	gl_FragColor.rgb -= scanline;
	 // vignette
	gl_FragColor.rgb *= 1.0 - d * 0.5;
	
        
}