#version 140
out vec4 color;


in vec2 vs_out;

uniform sampler2D tex;

void main()
{
	color = texture(tex, vs_out);
}