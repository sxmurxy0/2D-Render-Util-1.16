#version 120

uniform sampler2D sampler1;
uniform sampler2D sampler2;
uniform vec2 texelSize;
uniform vec2 direction;
uniform float radius;
uniform float kernel[64];

void main()
{
    vec2 uv = gl_TexCoord[0].st;
    uv.y = 1.0f - uv.y;

    float alpha = texture2D(sampler2, uv).a;
    if (direction.x == 0.0 && alpha == 0.0) {
        discard;
    }

    vec4 pixel_color = texture2D(sampler1, uv) * kernel[0];
    for (float f = 1; f <= radius; f++) {
        vec2 offset = f * texelSize * direction;
        pixel_color += texture2D(sampler1, uv - offset) * kernel[int(f)];
        pixel_color += texture2D(sampler1, uv + offset) * kernel[int(f)];
    }

    gl_FragColor = vec4(pixel_color.rgb, direction.x == 0.0 ? alpha : 1.0);
}
