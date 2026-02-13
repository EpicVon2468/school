#version 410 core

uniform float time;
out vec4 frag_colour;

vec3 colourA = vec3(0.149, 0.141, 0.912);
vec3 colourB = vec3(1.000, 0.833, 0.224);

void main() {
	vec3 colour = vec3(0.0);
	float pct = abs(sin(time));
	colour = mix(colourA, colourB, pct);
	frag_colour = vec4(colour, 1.0);
}