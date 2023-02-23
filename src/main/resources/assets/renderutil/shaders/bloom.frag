#version 120

uniform sampler2D sampler1;
uniform sampler2D sampler2;
uniform vec2 texelSize;
uniform vec2 direction;
uniform float radius;
uniform float kernel[64];

void main(void)
{
    vec2 uv = gl_TexCoord[0].st;

    if (direction.x == 0.0 && texture2D(sampler2, uv).a > 0.0) {
    	discard;
    }

    vec4 pixel_color = texture2D(sampler1, uv);
    pixel_color.rgb *= pixel_color.a;
    pixel_color *= kernel[0];

    for (float f = 1; f <= radius; f++) {
        vec2 offset = f * texelSize * direction;
        vec4 left = texture2D(sampler1, uv - offset);
        vec4 right = texture2D(sampler1, uv + offset);
        left.rgb *= left.a;
        right.rgb *= right.a;
        pixel_color += (left + right) * kernel[int(f)];
    }

    gl_FragColor = vec4(pixel_color.rgb / pixel_color.a, pixel_color.a);
}
