#version 410 core

uniform float time;
uniform vec3 colourOverride;
out vec4 frag_colour;

vec3 colourA = vec3(0.149, 0.141, 0.912);
vec3 colourB = vec3(1.000, 0.833, 0.224);
vec3 undefinedColour = vec3(-1.0, -1.0, -1.0);

void main() {
	if (colourOverride != undefinedColour) {
		frag_colour = vec4(colourOverride, 1.0);
		return;
	}
	vec3 colour = vec3(0.0);
	float pct = abs(sin(time));
	colour = mix(colourA, colourB, pct);
	frag_colour = vec4(colour, 1.0);
}