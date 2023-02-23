#version 120

uniform float round;
uniform vec2 size;
uniform vec4 color1;
uniform vec4 color2;
uniform vec4 color3;
uniform vec4 color4;

float alpha(vec2 d, vec2 d1) {
    vec2 v = abs(d) - d1 + round;
    return min(max(v.x, v.y), 0.0) + length(max(v, .0f)) - round;
}

void main() {
	 vec2 coords = gl_TexCoord[0].st;
    vec2 centre = .5f * size;
    vec4 color = mix(mix(color1, color2, coords.y), mix(color3, color4, coords.y), coords.x);
    gl_FragColor = vec4(color.rgb, color.a * (1.f- smoothstep(0.f, 1.5f, alpha(centre - (gl_TexCoord[0].st * size), centre - 1.f))));
}
