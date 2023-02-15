#version 120

uniform float round;
uniform vec2 size;
uniform vec4 color1;
uniform vec4 color2;
uniform vec4 color3;
uniform vec4 color4;

float dstfn(vec2 d, vec2 d1) {
    vec2 v = abs(d) - d1 + round;
    return min(max(v.x, v.y), 0.0) + length(max(v, .0f)) - round;
}

vec4 createGradient(vec2 coords, vec4 color1, vec4 color2, vec4 color3, vec4 color4){
    vec4 color = mix(mix(color1.rgba, color2.rgba, coords.y), mix(color3.rgba, color4.rgba, coords.y), coords.x);
    return color;
}

vec3 createGradientWA(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){
    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);
    return color;
}


void main() {
    vec2 d1 = gl_TexCoord[0].st * size;
    vec2 d2 = .5f * size;
    float sa = 1.f- smoothstep(0.f, 1.5f, dstfn(d2 - d1, d2 - 1.f));
    gl_FragColor = mix(vec4(createGradientWA(gl_TexCoord[0].st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), 0.0f), vec4(createGradient(gl_TexCoord[0].st, color1.rgba, color2.rgba, color3.rgba, color4.rgba)), sa);
}
