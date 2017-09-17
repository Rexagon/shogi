#version 330

in vec2 fTextureCoords;
in vec3 fNormal;

uniform sampler2D diffuseTexture;

void main() {
	gl_FragColor = texture(diffuseTexture, fTextureCoords);
}