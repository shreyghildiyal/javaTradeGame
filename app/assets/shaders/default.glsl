#type vertex
#version 330 core

layout (location=0) in vec3 attrPos;
layout (location=1) in vec4 attrColour;

out vec4 fragmentColour;

void main()  {
    fragmentColour = attrColour;
    gl_Position = vec4(attrPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fragmentColour;

out vec4 colour;

void main() {
    colour = fragmentColour;
}