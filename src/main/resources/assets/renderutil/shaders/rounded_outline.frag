#version 120

uniform float round;
uniform float thickness;
uniform vec2 size;
uniform vec2 smoothness;
uniform vec4 color;

float dstfn(vec2 p, vec2 b) {
	vec2 v = abs(p) - b + round;
	return min(max(v.x, v.y), 0.0) + length(max(v, .0f)) - round;
}

void main() {
    vec2 pixel = gl_TexCoord[0].st * size;
    vec2 centre = 0.5 * size;
    float sa = 1.f - smoothstep(smoothness.x, smoothness.y, abs(dstfn(centre - pixel, centre - thickness)));
    gl_FragColor = vec4(color.rgb, color.a * sa);
}
