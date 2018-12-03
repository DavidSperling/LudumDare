#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform float seedCounter;

vec3 hash3(in vec3 seed) {
    vec3 n;
    n.x = dot(seed, vec3(7649.1, 7669.2, 7673.3));
    n.y = dot(seed, vec3(7681.4, 7687.5, 7691.6));
    n.z = dot(seed, vec3(7699.7, 7703.8, 7717.9));
    return fract(sin(n) * 7723.01) * 2.0 - 1.0;
}

void main() {
    vec3 rand3 = hash3(vec3(gl_FragCoord.xy / 8000.0f, seedCounter / 100000.0f));
    gl_FragColor = vec4(rand3.x, rand3.x, rand3.x, 1.0);
}
