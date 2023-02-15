#version 120

uniform float round;
uniform sampler2D texture;
uniform vec2 size;

float dstfn(vec2 p, vec2 b) {
    return length(max(abs(p) - b, 0.0)) - round;
}

void main() {
    vec2 tex = gl_TexCoord[0].st;
    vec4 smpl = texture2D(texture, tex);
    vec2 pixel = gl_TexCoord[0].st * size;
    vec2 centre = 0.5 * size;
    float sa = 1.f - smoothstep(0.0, 1, dstfn(centre - pixel, centre - round - 1));
    gl_FragColor = vec4(smpl.rgb, smpl.a * sa);
}
