#version 330

layout(location=0) in vec3 position;
layout(location=1) in vec3 color;
layout(location=2) in vec3 normal;

out vec3 passColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 lightPosition;

void main() {
    passColor = color;
	gl_Position = projection * view * model * vec4(position, 1.0);
}