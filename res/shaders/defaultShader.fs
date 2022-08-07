#version 140

uniform sampler2D sampler;
uniform vec4 overrideColor;
uniform float colorRotationShift;
uniform float brightnessShift;

in vec4 color;
in vec2 uvCoords;

void main()
{
    if (overrideColor.x < 0) {
        vec4 toFragColor;
        vec4 fromTexture = texture2D(sampler, uvCoords);
        if (colorRotationShift == colorRotationShift) {
        //if (colorRotationShift != 0.0 && fromTexture.w > 0.0) {
            float h;
            float s;
            float v;

            float cmax = max(fromTexture.x, max(fromTexture.y, fromTexture.z));
            float cmin = min(fromTexture.x, min(fromTexture.y, fromTexture.z));
            float diff = cmax - cmin;

            if(cmax == cmin) {
                h = 0.0;
            }
            else if (cmax == fromTexture.x) {
                h = mod((60.0 * ((fromTexture.y - fromTexture.z) / diff) + 360.0), 360.0);
            }
            else if (cmax == fromTexture.y) {
                h = mod((60.0 * ((fromTexture.z - fromTexture.x) / diff) + 120.0), 360.0);
            }
            else if (cmax == fromTexture.z) {
                h = mod((60.0 * ((fromTexture.x - fromTexture.y) / diff) + 240.0), 360.0);
            }

            if (cmax == 0.0) {
                s = 0.0;
            }
            else {
                s = (diff / cmax);
            }

            v = cmax;



            h = mod((h + (colorRotationShift * 360.0)), 360.0);

            float c = s * v;
            float x = c * (1 - abs(mod(h / 60.0, 2) - 1));
            float m = v - c;

            float r;
            float g;
            float b;

            if (h >= 0.0 && h < 60.0) {
                r = c;
                g = x;
                b = 0.0;
            }
            else if (h >= 60.0 && h < 120.0) {
                r = x;
                g = c;
                b = 0.0;
            }
            else if (h >= 120.0 && h < 180.0) {
                r = 0.0;
                g = c;
                b = x;
            }
            else if (h >= 180.0 && h < 240.0) {
                r = 0.0;
                g = x;
                b = c;
            }
            else if (h >= 240.0 && h < 300.0) {
                r = x;
                g = 0.0;
                b = c;
            }
            else {
                r = c;
                g = 0.0;
                b = x;
            }

            float red = r+m;
            float green = g+m;
            float blue = b+m;

            if (brightnessShift > 0) {
                red = red + (brightnessShift * (1.0 - red));
                green = green + (brightnessShift * (1.0 - green));
                blue = blue + (brightnessShift * (1.0 - blue));
            }
            else if (brightnessShift < 0) {
                red = red - (brightnessShift * red);
                green = green - (brightnessShift * green);
                blue = blue - (brightnessShift * blue);
            }

            toFragColor = vec4(red, green, blue, fromTexture.w);
        }
        else {
            toFragColor = fromTexture;
        }

        gl_FragColor = color * toFragColor;
    }
    else {
        gl_FragColor = vec4(overrideColor.x, overrideColor.y, overrideColor.z, overrideColor.w * texture2D(sampler, uvCoords).w);
    }
}