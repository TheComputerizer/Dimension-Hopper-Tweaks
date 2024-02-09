#version 120

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
varying vec2 texCoord;
varying vec2 oneTexel;
uniform float Prominence = 1.0;
uniform vec3 Color = vec3(1.0,1.0,1.0);

float getAdjustment(vec3 Matrix, vec3 InitialMatrix, float InitialValue) {
    float PostValue = dot(InitialMatrix, Matrix);
    float difference = PostValue-InitialValue;
    return InitialValue+(difference*Prominence);
}

void main() {
    vec4 InTexel = texture2D(DiffuseSampler, texCoord);

    float RedValue = getAdjustment(vec3(Color.r,0,0),InTexel.rgb,InTexel.r);
    float GreenValue = getAdjustment(vec3(0,Color.g,0),InTexel.rgb,InTexel.g);
    float BlueValue = getAdjustment(vec3(0,0,Color.b),InTexel.rgb,InTexel.b);
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);

    // Saturation
    float Luma = dot(OutColor, vec3(0.3, 0.59, 0.11));
    vec3 Chroma = OutColor - Luma;
    OutColor = (Chroma * (1.0-Prominence)) + Luma;

    gl_FragColor = vec4(OutColor,1.0);
}