#version 330

layout(location = 0) in vec3 vPositoin;
layout(location = 1) in vec2 vTextureCoords;

uniform ivec2 windowSize;
uniform vec2 translation;

out vec2 fTextureCoords;

void main() {
	vec2 tranlsatedPosition = translation + vPositoin.xy;

	gl_Position = vec4(
		(tranlsatedPosition.x / windowSize.x) * 2 - 1, 
		(1 - tranlsatedPosition.y / windowSize.y) * 2 - 1, 
		0, 1);
	
	fTextureCoords = vTextureCoords;
}