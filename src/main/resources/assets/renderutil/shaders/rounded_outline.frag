#version 120

uniform float round;
uniform float thickness;
uniform vec2 size;
uniform vec4 color;

float alpha(vec2 d, vec2 d1) {
	vec2 v = abs(d) - d1 + round;
	return min(max(v.x, v.y), 0.0) + length(max(v, .0f)) - round;
}

void main() {
    vec2 centre = .5f * size;
    vec2 smoothness = vec2(thickness - 1.5f, thickness);
    gl_FragColor = vec4(color.rgb, color.a * (1.f - smoothstep(smoothness.x, smoothness.y, abs(alpha(centre - (gl_TexCoord[0].st * size), centre - thickness)))));
}
