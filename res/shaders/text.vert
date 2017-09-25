#version 330

layout(location = 0) in vec3 vPositoin;
layout(location = 1) in vec2 vTextureCoords;

uniform ivec2 windowSize;
uniform vec2 translation;

out vec2 fTextureCoords;

void main() {
	vec2 position = translation + vPositoin.xy;

	gl_Position = vec4(
		2.0 * position.x / windowSize.x - 1.0, 
		1.0 - 2.0 * position.y / windowSize.y, 
		0, 1);
	
	fTextureCoords = vTextureCoords;
}