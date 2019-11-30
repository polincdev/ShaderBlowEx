 //Credits
// https://github.com/tizian/tonemapper/
// https://github.com/GPUOpen-Effects/FidelityFX
// https://github.com/cansik/processing-postfx
 
uniform vec3 m_WhitePoint;
varying vec2 texCoord;
uniform float m_Gamma;
uniform float m_Exposure;
uniform sampler2D m_Texture;
uniform vec3 m_Color;
uniform int  m_ToneMapType;
 
 
//Linear
 vec3 LinearToneMapping( vec3 color,float exposure)
{
     color *= exposure;
	 return color;
}

//Reinhard/////////
vec3 SimpleReinhardToneMapping(vec3 color,float exposure)
{
	 color *= exposure/(1. + color / exposure);
	 return color;
}

vec3 LumaBasedReinhardToneMapping(vec3 color,float exposure)
{
        color *= exposure;
	float luma = dot(color, vec3(0.2126, 0.7152, 0.0722));
	float toneMappedLuma = luma / (1. + luma);
	color *= toneMappedLuma / luma;
	 return color;
}

vec3 WhitePreservingLumaBasedReinhardToneMapping(vec3 color,float exposure)
{
        color *= exposure;
	float white = 2.;
	float luma = dot(color, vec3(0.2126, 0.7152, 0.0722));
	float toneMappedLuma = luma * (1. + luma / (white*white)) / (1. + luma);
	color *= toneMappedLuma / luma;
	 
	return color;
}
//RomBon
vec3 RomBinDaHouseToneMapping(vec3 color,float exposure)
{
      color *= exposure;
      color = exp( -1.0 / ( 2.72*color + 0.15 ) );
      return color;
}

 // https://knarkowicz.wordpress.com/2016/01/06/aces-filmic-tone-mapping-curve/
 #define saturate(x) clamp(x, 0., 1.)
 
 vec3  ACESFilmToneMapping(vec3 x,float exposure)
{
     x *= exposure;
    float a = 2.51;
    float b = 0.03;
    float c = 2.43;
    float d = 0.59;
    float e = 0.14;
    return saturate((x*(a*x + b)) / (x*(c*x + d) + e));
}

//http://www.oscars.org/science-technology/sci-tech-projects/aces
 vec3 ACESAbsoluteFilmToneMapping(vec3 color,float exposure)
         {	
             color *= exposure;
	mat3 m1 = mat3(
		0.59719, 0.07600, 0.02840,
		0.35458, 0.90834, 0.13383,
		0.04823, 0.01566, 0.83777
	);
	mat3 m2 = mat3(
		1.60475, -0.10208, -0.00327,
		-0.53108,  1.10813, -0.07276,
		-0.07367, -0.00605,  1.07602
	);
	vec3 v = m1 * color;    
	vec3 a = v * (v + 0.0245786) - 0.000090537;
	vec3 b = v * (0.983729 * v + 0.4329510) + 0.238081;
	return  m2 * (a / b) ;	
}
//Filmic http://filmicworlds.com/blog/filmic-tonemapping-operators/
//"Filmic Mapping 1\n\nBy Jim Hejl and Richard Burgess-Dawson from the \"Filmic Tonemapping for Real-time Rendering\" Siggraph 2010 Course by Haarm-Pieter Duiker."
vec3 FilmicToneMapping(vec3 color,float exposure)
{
       color *= exposure;
	color = max(vec3(0.), color - vec3(0.004));
	color = (color * (6.2 * color + .5)) / (color * (6.2 * color + 1.7) + 0.06);
	return color;
}

// Hable's filmic http://www.gdcvault.com/play/1012351/Uncharted-2-HDR
vec3 Uncharted2ToneMapping(vec3 color,float exposure)
{
	float A = 0.15;
	float B = 0.50;
	float C = 0.10;
	float D = 0.20;
	float E = 0.02;
	float F = 0.30;
	float W = 11.2;
	 color *= exposure;
	color = ((color * (A * color + C * B) + D * E) / (color * (A * color + B) + D * F)) - E / F;
	float white = ((W * (A * W + C * B) + D * E) / (W * (A * W + B) + D * F)) - E / F;
	color /= white;
	 
	return color;
}
 //HDRToneMappingCS11
 vec3 DX11DSKToneMapping(vec3 color,float exposure)
{
    float  MIDDLE_GRAY = 0.72;
    float  LUM_WHITE = 1.5;

    color *= exposure;
    // Tone mapping
    color.rgb *= MIDDLE_GRAY;
    color.rgb *= (1.0  + color/LUM_WHITE);
    color.rgb /= (1.0  + color);
    
    return color;
}
 
 // Timothy Lottes tone mapper
 // General tonemapping operator, build 'b' term.
float ColToneB(float hdrMax, float contrast, float shoulder, float midIn, float midOut) 
{
    return
        -((-pow(midIn, contrast) + (midOut*(pow(hdrMax, contrast*shoulder)*pow(midIn, contrast) -
            pow(hdrMax, contrast)*pow(midIn, contrast*shoulder)*midOut)) /
            (pow(hdrMax, contrast*shoulder)*midOut - pow(midIn, contrast*shoulder)*midOut)) /
            (pow(midIn, contrast*shoulder)*midOut));
}

// General tonemapping operator, build 'c' term.
float ColToneC(float hdrMax, float contrast, float shoulder, float midIn, float midOut) 
{
    return (pow(hdrMax, contrast*shoulder)*pow(midIn, contrast) - pow(hdrMax, contrast)*pow(midIn, contrast*shoulder)*midOut) /
           (pow(hdrMax, contrast*shoulder)*midOut - pow(midIn, contrast*shoulder)*midOut);
}

// General tonemapping operator, p := {contrast,shoulder,b,c}.
float ColTone(float x, vec4 p) 
{ 
    float z = pow(x, p.r); 
    return z / (pow(z, p.g)*p.b + p.a); 
}
#define lerp(a,b,w) a+w*(b-a)
 
vec3 TimothyTonemapper(vec3 color,float exposure)
{
      float hdrMax = 16.0; // How much HDR range before clipping. HDR modes likely need this pushed up to say 25.0.
      float contrast = 2.0; // Use as a baseline to tune the amount of contrast the tonemapper has.
      float shoulder = 1.0; // Likely don’t need to mess with this factor, unless matching existing tonemapper is not working well..
      float midIn = 0.18; // most games will have a {0.0 to 1.0} range for LDR so midIn should be 0.18.
      float midOut = 0.18; // Use for LDR. For HDR10 10:10:10:2 use maybe 0.18/25.0 to start. For scRGB, I forget what a good starting point is, need to re-calculate.
    color *= exposure;
    float b = ColToneB(hdrMax, contrast, shoulder, midIn, midOut);
    float c = ColToneC(hdrMax, contrast, shoulder, midIn, midOut);

    float peak = max(color.r, max(color.g, color.b));
    vec3 ratio = color / peak;
    peak = ColTone(peak, vec4(contrast, shoulder, b, c) );
    
    float crosstalk = 4.0; // controls amount of channel crosstalk
    float saturation = contrast; // full tonal range saturation control
    float crossSaturation = contrast*16.0; // crosstalk saturation

    float white = 1.0;

    // wrap crosstalk in transform
    ratio = pow(abs(ratio), vec3(saturation / crossSaturation));
    ratio = lerp(ratio, white, pow(peak, crosstalk));
    ratio = pow(abs(ratio), vec3(crossSaturation));

    // then apply ratio to peak
    color = peak * ratio;
    return color;
}

vec3 ExponentialToneMapping(vec3 color, float exposure ) {
  color *= exposure;

  color.r = color.r < 1.413 ? pow(color.r * 0.38317, 1.0 ) : 1.0 - exp2(-exposure * color.r);
  color.g = color.g < 1.413 ? pow(color.g * 0.38317, 1.0  ) : 1.0 - exp2(-exposure * color.g);
  color.b = color.b < 1.413 ? pow(color.b * 0.38317, 1.0  ) : 1.0 - exp2(-exposure * color.b);

  return color;
}
// Unreal 3, Documentation: "Color Grading" Gamma 2.2 correction is baked in 
 vec3 UnrealToneMapping(vec3 x, float exposure ) {
   x *= exposure;
  return x / (x + 0.155) * 1.019;
}

 // Lottes 2016, "Advanced Techniques and Optimization of HDR Color Pipelines"
vec3 AMDLottesToneMapping(vec3 x, float exposure) {
  const vec3 a = vec3(1.6);
  const vec3 d = vec3(0.977);
  const vec3 hdrMax = vec3(8.0);
  const vec3 midIn = vec3(0.18);
  const vec3 midOut = vec3(0.267);
  x *= exposure;
  const vec3 b =
      (-pow(midIn, a) + pow(hdrMax, a) * midOut) /
      ((pow(hdrMax, a * d) - pow(midIn, a * d)) * midOut);
  const vec3 c =
      (pow(hdrMax, a * d) * pow(midIn, a) - pow(hdrMax, a) * pow(midIn, a * d) * midOut) /
      ((pow(hdrMax, a * d) - pow(midIn, a * d)) * midOut);

  return pow(x, a) / (pow(x, a * d) * b + c);
}

vec3 Reinhard2ToneMapping(vec3 x, float exposure) {
   x *= exposure;
  const float L_white = 4.0;

  return (x * (1.0 + x / (L_white * L_white))) / (1.0 + x);
}

// Uchimura 2017 aka gran_turismo, https://www.desmos.com/calculator/gslcdxvipg
//   https://www.slideshare.net/nikuque/hdr-theory-and-practicce-jp
vec3 uchimura(vec3 x, float P, float a, float m, float l, float c, float b) {
  float l0 = ((P - m) * l) / a;
  float L0 = m - m / a;
  float L1 = m + (1.0 - m) / a;
  float S0 = m + l0;
  float S1 = m + a * l0;
  float C2 = (a * P) / (P - S1);
  float CP = -C2 / P;

  vec3 w0 = vec3(1.0 - smoothstep(0.0, m, x));
  vec3 w2 = vec3(step(m + l0, x));
  vec3 w1 = vec3(1.0 - w0 - w2);

  vec3 T = vec3(m * pow(x / m, vec3(c)) + b);
  vec3 S = vec3(P - (P - S1) * exp(CP * (x - S0)));
  vec3 L = vec3(m + a * (x - m));

  return T * w0 + L * w1 + S * w2;
}

vec3  UchimuraToneMapping(vec3 x, float exposure) {
  const float P = 1.0;  // max display brightness
  const float a = 1.0;  // contrast
  const float m = 0.22; // linear section start
  const float l = 0.4;  // linear section length
  const float c = 1.33; // black
  const float b = 0.0;  // pedestal
  x *= exposure;
  return uchimura(x, P, a, m, l, c, b);
}
//"Ferwerda Mapping\n\nProposed in \"A Model of Visual Adaptation for Realistic Image Synthesis\" by Ferwerda et al. 1996.";
   float Ldmax=80.; //Maximum luminance capability of the display
    float Lwa=Ldmax/2.; //
    
    vec3 clampedValue(vec3 color) {
        return clamp(color, 0.0, 1.0);
    }
   
    float tp(float La) {
        float logLa = log(La)/log(10.0);
        float result;
        if (logLa <= -2.6) {
            result = -0.72;
        }
        else if (logLa >= 1.9) {
            result = logLa - 1.255;
        }
        else {
            result = pow(0.249 * logLa + 0.65, 2.7) - 0.72;
        }
        return pow(10.0, result);
    }
    float ts(float La) {
        float logLa = log(La)/log(10.0);
        float result;
        if (logLa <= -3.94) {
            result = -2.86;
        }
        else if (logLa >= -1.44) {
            result = logLa - 0.395;
        }
        else {
            result = pow(0.405 * logLa + 1.6, 2.18) -2.86;
        }
        return pow(10.0, result);
    }
    float getLuminance(vec3 color) {
        return 0.212671 * color.r + 0.71516 * color.g + 0.072169 * color.b;
    }
    vec3 adjustColor(vec3 color, float L, float Ld) {
        return Ld * color / L;
    }

vec3  FerwerdaToneMapping(vec3 color, float exposure)
     {
        color*= exposure;
        float Lda = Ldmax / 2.0;
        float L = getLuminance(color);
        float mP = tp(Lda) / tp(exposure * Lwa);
        float mS = ts(Lda) / ts(exposure * Lwa);
        float k = (1.0 - (Lwa/2.0 - 0.01)/(10.0-0.01));
        k = clamp(k * k, 0.0, 1.0);
        float Ld = mP * L + k * mS * L;
        color = adjustColor(color, L, Ld);
        return   clampedValue(color);
       
    }
//Haarm-Pieter Duiker curve by Jim Hejl and Richard Burgess-Dawson.
vec3  HaarmPieterDuikerToneMapping(vec3 color, float exposure)
     {
        color*= exposure;
        color-=0.004;
        vec3 x = max(vec3(0),color);
	vec3 retColor = (x*(6.2*x+.5))/(x*(6.2*x+1.7)+0.06);
        return retColor;
     }
     
     
   //   Ward Ward Mapping\n\nProposed in "A contrast-based scalefactor for luminance display " by Ward 1994.
    vec3  WardToneMapping(vec3 color, float exposure) 
      {
       color*= exposure;
       
        float Lda = Ldmax / 2.0;
        float m = pow((1.219 + pow(Lda, 0.4)) / (1.219 + pow(Lwa * exposure, 0.4)), 2.5);
         float L = getLuminance(color);
        float Ld = m * L / Ldmax;
        color = adjustColor(color, L, Ld);
       return   clampedValue(color);
      }
  //"Tumblin-Rushmeier Mapping\n\nProposed in\"Tone Reproduction for Realistic Images\" by Tumblin and Rushmeier 1993."        
float Cmax =  50.; // "Maximum contrast ratio between on-screen luminances" ;
 float Lavg  = 0.2; // AverageLuminance 
 vec3  TumblinRushmeierToneMapping(vec3 color, float exposure) 
      {
       color*= exposure;
         float log10Lrw = log(exposure * Lavg)/log(10.0);
        float alpha_rw = 0.4 * log10Lrw + 2.92;
        float beta_rw = -0.4 * log10Lrw*log10Lrw - 2.584 * log10Lrw + 2.0208;
        float log10Ld = log(Ldmax / sqrt(Cmax))/log(10.0);
        float alpha_d = 0.4 * log10Ld + 2.92;
        float beta_d = -0.4 * log10Ld*log10Ld - 2.584 * log10Ld + 2.0208;
         float L = getLuminance(color);
        float Ld = pow(L, alpha_rw/alpha_d) / Ldmax * pow(10.0, (beta_rw - beta_d) / alpha_d) - (1.0 / Cmax);
         color = adjustColor(color, L, Ld);
        return   clampedValue(color);
       }
 //Schlick Mapping\n\nProposed in \"Quantization Techniques for Visualization of High Dynamic Range Pictures\" by Schlick 1994.
  float pParam = 200.;//"Rational mapping curve parameter ;
   float Lmax=500. ;//MaximumLuminance
   vec3  SchlickToneMapping(vec3 color, float exposure) 
      {
       color*= exposure;
        color = pParam * color / (pParam * color - color + exposure * Lmax);
         return   clampedValue(color);
       }
  //Extended Reinhard Mapping\n\nProposed in \"Photographic Tone Reproduction for Digital Images\" by Reinhard et al. 2002.\n(Extension that allows high luminances to burn out.)"     
 float Lwhite=2.;  //Smallest luminance that will be mapped to pure white.
  vec3  ReinhardExtendedToneMapping(vec3 color, float exposure) 
      {
       color*= exposure;
         float L = getLuminance(color);
        float Ld = (L * (1.0 + L / (Lwhite * Lwhite))) / (1.0 + L);
        color = adjustColor(color, L, Ld);
        return   clampedValue(color);
       }
       
 //"Reinhard-Devlin Mapping\n\nPropsed in \"Dynamic Range Reduction Inspired by Photoreceptor Physiology\" by Reinhard and Devlin 2005
    float mParam=0.5;// "Compression curve adjustment parameter ;
    float fParam=1.0; // "Intensity adjustment parameter ;
    float cParam=0.0;// "Chromatic adaptation\nBlend between color channels and luminance.");
    float aParam=1.0; //"Light adaptation\nBlend between pixel intensity and average scene intensity.");
    float Lav = 2.0;
    float Iav_r =100.0;
    float Iav_g =100.0;
    float Iav_b =100.0;
       

    float sigmaIa(float Ia, float Iav_a, float L, float exposure) {
        float Ia_local = cParam * Ia + (1.0 - cParam) * L;
        float Ia_global = cParam * Iav_a + (1.0 - cParam) * exposure * Lav;
        float result = aParam * Ia_local + (1.0 - aParam) * Ia_global;
        return pow(fParam * result, mParam);
    }
    
 vec3  ReinhardDevlinToneMapping(vec3 color, float exposure) 
      {
       color*= exposure;
       float L = getLuminance(color);
        float sigmaIr = sigmaIa(color.r, exposure * Iav_r, L,exposure);
        float sigmaIg = sigmaIa(color.g, exposure * Iav_g, L,exposure);
        float sigmaIb = sigmaIa(color.b, exposure * Iav_b, L,exposure);
        color.r = color.r / (color.r + sigmaIr);
        color.g = color.g / (color.g + sigmaIg);
        color.b = color.b / (color.b + sigmaIb);

        return   clampedValue(color);
       }
       
   //Mean Value Mapping\n\nMean value is mapped to 0.5.    
  vec3  MeanValueToneMapping(vec3 color, float exposure) 
      {
       color*= exposure;
        float L = getLuminance(color);
        float Ld = 0.5 * L / (exposure * Lavg);
       color = adjustColor(color, L, Ld);
       return   clampedValue(color);
       }
  //"Division by maximum\n\nMaximum value is mapped to 1.";
  vec3  MaximumDivisionToneMapping(vec3 color, float exposure) 
      {
       color*= exposure;
       float L = getLuminance(color);
        float Ld = L / (exposure * Lmax);
        color = adjustColor(color, L, Ld);
        return   clampedValue(color);
       }
  //Logarthmic Mapping\n\nDiscussed in \"Quantization Techniques for Visualization of High Dynamic Range Pictures\" by Schlick 1994.     
 float  ppParam=1.;//"Exponent numerator scale factor");
 float  qqParam=1.;//"Exponent denominator scale factor");
 
 vec3  LogarithmicToneMapping(vec3 color, float exposure) 
      {
       color*= exposure;
         float L = getLuminance(color);
        float Ld = (log(1.0 + ppParam * L)/log(10.0)) / (log(1.0 + qqParam * exposure * Lmax)/log(10.0));
        color = adjustColor(color, L, Ld);
        return   clampedValue(color);
       }
 //"Insomniac Mapping\n\nFrom \"An efficient and user-friendly tone mapping operator\" by Mike Day (Insomniac Games).";  
    float  wParam  =  10.;//White point\nMinimal value that is mapped to 1.");
    float bParam  = 0.1;//"Black point\nMaximal value that is mapped to 0.");
    float tParam =  0.7;//"Toe strength\nAmount of blending between a straight-line curve and a purely asymptotic curve for the toe.");
    float sParam  =  0.8;// "Shoulder strength\nAmount of blending between a straight-line curve and a purely asymptotic curve for the shoulder.");
    float ccParam =  2.;// "Cross-over point\nPoint where the toe and shoulder are pieced together into a single curve.");
    
    float instonemap(float x, float k) {
            if (x < ccParam) {
                return k * (1.0-tParam)*(x-bParam) / (ccParam - (1.0-tParam)*bParam - tParam*x);
            }
            else {
                return (1.0-k)*(x-ccParam) / (sParam*x + (1.0-sParam)*wParam - ccParam) + k;
            }
        }
   vec3 InsomniacToneMapping(vec3 color, float exposure) 
      {
       color*= exposure;
        color = color / Lavg;
       float k = (1.0-tParam)*(ccParam-bParam) / ((1.0-sParam)*(wParam-ccParam) + (1.0-tParam)*(ccParam-bParam));
       color = vec3(instonemap(color.r, k), instonemap(color.g, k), instonemap(color.b, k) );
       return   clampedValue(color); 
     }
     
     //"Filmic Mapping 2\n\nBy Graham Aldridge from \"Approximating Film with Tonemapping\".";
    float cutoff= 0.025;// "Transition into compressed blacks");

    vec3   GrahamAldridgeFilmicToneMapping(vec3 color, float exposure) 
       {
        color*= exposure;
        vec3 x = color + (cutoff * 2.0 - color) * clamp(cutoff * 2.0 - color, 0.0, 1.0) * (0.25 / cutoff) - cutoff;
        color = (x * (6.2 * x + 0.5)) / (x * (6.2 * x + 1.7) + 0.06);
        return   clampedValue(color); 
       }
 
       
//Exponentiation Mapping\n\nDiscussed in \"Quantization Techniques for Visualization of High Dynamic Range Pictures\" by Schlick 1994  
vec3 ExponentiationToneMapping(vec3 color, float exposure ) 
    {
        color *= exposure;  
        float L = getLuminance(color);
        float Ld = L / (exposure * Lmax);
        color = adjustColor(color, L, Ld);
        return   clampedValue(color); 
    }       
//Exponential Mapping\n\nProposed in \"A Comparison of techniques for the Transformation of Radiosity Values to Monitor Colors\" by Ferschin et al. 1994. 
 float pppParam=1.;//"Exponent numerator scale factor");
 float qqqParam=1.;// "Exponent denominator scale factor");

vec3 Exponential2ToneMapping(vec3 color, float exposure ) 
    {
      color *= exposure;    
      float L = getLuminance(color);
      float Ld = 1.0 - exp(-(L * pppParam) / (exposure * Lavg * qqqParam));
      color = adjustColor(color, L, Ld);
      return   clampedValue(color); 
    }
//"Drago Mapping\n\nPropsed in \"Adaptive Logarithmic Mapping For Displaying High Contrast Scenes\" by Drago et al. 2003.";  
 float slope = 4.5;//"Additional Gamma correction parameter:\nElevation ratio of the line passing by the origin and tangent to the curve.");
 float start=0.018;// "Additional Gamma correction parameter:\nAbscissa at the point of tangency.");
 float bbbParam=0.85;// "Bias function parameter");
 float Lwmax=2.;// "MaximumLuminance");
 
vec3 DrogoToneMapping(vec3 color, float exposure ) 
    {
       float LwaP = exposure * Lwa / pow(1.0 + bbbParam - 0.85, 5.);
        float LwmaxP = exposure * Lwmax / LwaP;
        color *= exposure/ LwaP;
        float L = getLuminance(color);
        float exponent = log(bbbParam) / log(0.5);
        float c1 = (0.01 * Ldmax) / (log(1. + LwmaxP)/log(10.0));
        float c2 = log(L + 1.) / log(2.0 + 8. * (pow(L / LwmaxP, exponent)));
        float Ld = c1 * c2;
        color = adjustColor(color, L, Ld);
          return   clampedValue(color); 
    }
 //Clamping\n\nUser defined maximum value that maps to 1.\nDiscussed in \"Quantization Techniques for Visualization of High Dynamic Range Pictures\" by Schlick 1994
float ppPar=1.0;
vec3 ClampingToneMapping(vec3 color, float exposure ) 
    {
        color *= exposure;    
        float L = getLuminance(color);
        float Ld = L / (exposure * ppPar);
        color = adjustColor(color, L, Ld);
        return   clampedValue(color); 
     }
     
vec3 JodieRoboTonemap(vec3 c, float exposure )
    {
        c *= exposure;    
        float l = dot(c, vec3(0.2126, 0.7152, 0.0722));
        vec3 tc=c/sqrt(c*c+1.);
        return mix(c/sqrt(l*l+1.),tc,tc);
    }  
vec3 JodieReinhardTonemap(vec3 c, float exposure)
    {
         c *= exposure;   
        float l = dot(c, vec3(0.2126, 0.7152, 0.0722));
        vec3 tc=c/(c+1.);
        return mix(c/(l+1.),tc,tc);
    }   
    
//minecraft
vec3 BarteropeTonemap(  vec3 color, float exposure)
    {

        color *= exposure;   
	float curve = 14.5;
	float Exposer= 0.1;

	float R	= 1.0;
	float G	= 1.0;
	float B	= 1.0;
	float sat= 1.158;
    
	float avg = (color.r + color.g + color.b);
	vec3 vary = (color - avg );
    
	vec3 V	= (color - 0.1);
	vec3 cMult= vec3(3);
    
	color = ((2.45*V+0.154)/(2.45*V+1.0));
	 
	color  = pow(color ,vec3(curve));
	color = ((vary*sat)+avg);
    
	color.r = (R*color.r);
	color.g= (G*color.g);
	color.b = (B*color.b);
    
	color = color*cMult;
        
        return color;
}
//
vec3 GiffordTonemap(  vec3 color, float exposure )
  {

    float gain=1.7;
    float bias=1.1;
    color *= exposure;   
    vec3 blend_rat = pow(clamp(color,0.0,1.0),vec3(bias));
    color = mix(color,1.0 - pow(clamp(1.0 - color,0.0,1.0), vec3(gain)),blend_rat);
    return color;
    }
   
    
 
vec3 Tonemap(vec3 color, float exposure, int toneMapType)
{
  
    #if TONEMAP==0
        return LinearToneMapping(color, exposure);
    #elif TONEMAP==1
        return SimpleReinhardToneMapping(color, exposure);
     #elif TONEMAP==2
        return LumaBasedReinhardToneMapping(color, exposure);
      #elif TONEMAP==3
        return WhitePreservingLumaBasedReinhardToneMapping(color, exposure);
     #elif TONEMAP==4
        return RomBinDaHouseToneMapping(color, exposure);
     #elif TONEMAP==5
        return ACESFilmToneMapping(color, exposure);
     #elif TONEMAP==6
        return ACESAbsoluteFilmToneMapping(color, exposure);
     #elif TONEMAP==7
        return FilmicToneMapping(color, exposure);
     #elif TONEMAP==8
        return Uncharted2ToneMapping(color, exposure);
     #elif TONEMAP==9
        return DX11DSKToneMapping(color, exposure);
     #elif TONEMAP==10
        return TimothyTonemapper(color, exposure);
     #elif TONEMAP==11
        return ExponentialToneMapping(color, exposure);
     #elif TONEMAP==12
        return UnrealToneMapping(color, exposure);
     #elif TONEMAP==13
        return AMDLottesToneMapping(color, exposure);
     #elif TONEMAP==14
        return Reinhard2ToneMapping(color, exposure);
     #elif TONEMAP==15
        return UchimuraToneMapping(color, exposure);
     #elif TONEMAP==16
        return FerwerdaToneMapping(color, exposure);
     #elif TONEMAP==17
        return HaarmPieterDuikerToneMapping(color, exposure);
     #elif TONEMAP==18
        return WardToneMapping(color, exposure);
     #elif TONEMAP==19
        return TumblinRushmeierToneMapping(color, exposure);
     #elif TONEMAP==20
        return SchlickToneMapping(color, exposure);
     #elif TONEMAP==21
        return ReinhardExtendedToneMapping(color, exposure);
     #elif TONEMAP==22
        return ReinhardDevlinToneMapping(color, exposure);
     #elif TONEMAP==23
        return MeanValueToneMapping(color, exposure);
     #elif TONEMAP==24
        return MaximumDivisionToneMapping(color, exposure);
     #elif TONEMAP==25
        return LogarithmicToneMapping(color, exposure);
     #elif TONEMAP==26
        return InsomniacToneMapping(color, exposure);
     #elif TONEMAP==27
        return GrahamAldridgeFilmicToneMapping(color, exposure);
     #elif TONEMAP==28
        return ExponentiationToneMapping(color, exposure);
     #elif TONEMAP==29
        return Exponential2ToneMapping(color, exposure);
     #elif TONEMAP==30
        return DrogoToneMapping(color, exposure);
     #elif TONEMAP==31
        return ClampingToneMapping(color, exposure);
     #elif TONEMAP==32
        return JodieRoboTonemap(color, exposure);
     #elif TONEMAP==33
        return JodieReinhardTonemap(color, exposure);
     #elif TONEMAP==34
        return BarteropeTonemap(color, exposure); 
    #elif TONEMAP==35
        return GiffordTonemap(color, exposure); 
     #else 
        return LinearToneMapping(color, exposure);
    #endif
 
/*

  


        if(toneMapType == 0)return LinearToneMapping(color, exposure);
    else if(toneMapType == 1)return SimpleReinhardToneMapping(color, exposure);
    else if(toneMapType == 2)return LumaBasedReinhardToneMapping(color, exposure);
    else if(toneMapType == 3)return WhitePreservingLumaBasedReinhardToneMapping(color, exposure);
    else if(toneMapType == 4)return RomBinDaHouseToneMapping(color, exposure);
    else if(toneMapType == 5)return ACESFilmToneMapping(color, exposure);
    else if(toneMapType == 6)return ACESAbsoluteFilmToneMapping(color, exposure);
    else if(toneMapType == 7)return FilmicToneMapping(color, exposure);
    else if(toneMapType == 8)return Uncharted2ToneMapping(color, exposure);
    else if(toneMapType == 9)return DX11DSKToneMapping(color, exposure);
    else if(toneMapType == 10)return TimothyTonemapper(color, exposure);
    else if(toneMapType == 11)return ExponentialToneMapping(color, exposure);
    else if(toneMapType == 12)return UnrealToneMapping(color, exposure);
    else if(toneMapType == 13)return AMDLottesToneMapping(color, exposure);
    else if(toneMapType == 14)return Reinhard2ToneMapping(color, exposure);
    else if(toneMapType == 15)return UchimuraToneMapping(color, exposure);
    else if(toneMapType == 16)return FerwerdaToneMapping(color, exposure);
    else if(toneMapType == 17)return HaarmPieterDuikerToneMapping(color, exposure);
    else if(toneMapType == 18)return WardToneMapping(color, exposure);
    else if(toneMapType == 19)return TumblinRushmeierToneMapping(color, exposure);
    else if(toneMapType == 20)return SchlickToneMapping(color, exposure);
    else if(toneMapType == 21)return ReinhardExtendedToneMapping(color, exposure);
    else if(toneMapType == 22)return ReinhardDevlinToneMapping(color, exposure);
    else if(toneMapType == 23)return MeanValueToneMapping(color, exposure);
    else if(toneMapType == 24)return MaximumDivisionToneMapping(color, exposure);
    else if(toneMapType == 25)return LogarithmicToneMapping(color, exposure);
    else if(toneMapType == 26)return InsomniacToneMapping(color, exposure);
    else if(toneMapType == 27)return GrahamAldridgeFilmicToneMapping(color, exposure);
    else if(toneMapType == 28)return ExponentiationToneMapping(color, exposure);
    else if(toneMapType == 29)return Exponential2ToneMapping(color, exposure);
    else if(toneMapType == 30)return DrogoToneMapping(color, exposure);
    else if(toneMapType == 31)return ClampingToneMapping(color, exposure);
    else if(toneMapType == 32)return JodieRoboTonemap(color, exposure);
    else if(toneMapType == 33)return JodieReinhardTonemap(color, exposure);
    else if(toneMapType == 34)return BarteropeTonemap(color, exposure);
    else return color;
 */

    return color;   
}


 vec3 gammaCorrection(vec3 color, float gamma)
   {
	// gamma correction 
	color = pow(color, vec3(1.0/gamma)); 
	return color;
   }

 
void main() {
    
       //
      vec2 uv = texCoord;
      
      // Original color
      vec4 c =  texture2D(m_Texture, uv ); 
      //tonemap + exposure
      vec3 n = Tonemap(c.rgb, m_Exposure,  m_ToneMapType);
      //gamma correction  
       n = gammaCorrection(n,m_Gamma);
    
       gl_FragColor = vec4(n, c.a);
 
}