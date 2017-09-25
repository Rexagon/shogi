#version 330

in vec2 fTextureCoords;

uniform sampler2D diffuseTexture;
uniform vec4 color;
uniform int hasTexture;

void main() {
	vec4 diffuseColor = vec4(1);
	if (hasTexture == 1) {
		diffuseColor = texture(diffuseTexture, fTextureCoords);
	}
	gl_FragColor = color * diffuseColor;
}