#version 140 
out vec4 color; 


in vec2 vs_out;

uniform sampler2D tex;

uniform vec3 color_filter;void main()
{
	color = texture(tex, vs_out);
	color.x = color_filter.x * color.x;
	color.y = color_filter.y * color.y;
	color.z = color_filter.z * color.z;}