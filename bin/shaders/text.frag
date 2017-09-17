#version 330

in vec2 fTextureCoords;

uniform sampler2D mask;
uniform vec4 color;

void main() {
	gl_FragColor = color * texture(mask, fTextureCoords);
}