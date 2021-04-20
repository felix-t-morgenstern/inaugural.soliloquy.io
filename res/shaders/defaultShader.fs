#version 140

uniform sampler2D sampler;
uniform vec3 overrideColor;

in vec4 color;
in vec2 uvCoords;

void main()
{
    if (overrideColor.x < 0) {
        gl_FragColor = color * texture2D(sampler, uvCoords);
    }
    else {
        gl_FragColor = vec4(overrideColor, texture2D(sampler, uvCoords).w);
    }
}