#version 120

#define iterations 13
#define formuparam 0.51

#define volsteps 5
#define stepsize 0.25

#define tile 0.850

#define brightness 0.0015
#define darkmatter 0.300
#define distfading 0.730
#define saturation 0.850

uniform float u_msTime;
uniform float u_timeScale;
uniform float u_zoom;
uniform float u_offsetX;
uniform float u_offsetY;
uniform float u_offsetZ;

void main() {
    vec2 uv = gl_TexCoord[0].st;
    vec3 dir = vec3(uv*u_zoom,1f);
    float time = u_msTime * u_timeScale+0.25f;
    vec3 from = vec3(1f,0.5f,0.5f);
    from += vec3(time*2f,time,-2f);
    from += vec3(u_offsetX, u_offsetY, u_offsetZ);
    //volumetric rendering
    float s = 0.1f;
    float fade = 1f;
    vec3 v=vec3(0f);
    for(int r = 0; r < volsteps; r++) {
        vec3 p = from + s * dir * 0.5f;
        p = abs(vec3(tile) - mod(p, vec3(tile*2f))); // tiling fold
        float pa, a = pa = 0f;
        for(int i = 0; i < iterations; i++) {
            p = abs(p) / dot(p, p) - formuparam; // the magic formula
            a += abs(length(p) - pa); // absolute sum of average change
            pa = length(p);
        }
        float dm = max(0f,darkmatter-a*a*0.001f); //dark matter
        a *= a * a; // add contrast
        if (r > 6) fade *= 1f-dm ; // dark matter, don't render near
        //v+=vec3(dm,dm*.5,0.);
        v += fade;
        v += vec3(s, s*s, s*s*s*s) * a * brightness * fade; // coloring based on distance
        fade *= distfading; // distance fading
        s += stepsize;
    }
    v = mix(vec3(length(v)), v, saturation); //color adjust
    gl_FragColor = vec4(v*0.01f*vec3(0.4f,1f,1f),1f);
}