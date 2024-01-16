#version 400 core

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;

in vec2 fragTextureCoord;
in vec3 fragNormal;
in vec3 fragPos;

out vec4 fragColor;

struct Material {
	vec4 ambient; 
	vec4 diffuse;
	vec4 specular;
	int hasTexture;
	float reflectance;
};

struct DirectionalLight {
	vec3 color;
	vec3 direction;
	float intensity;
};

struct PointLight {
	vec3 color;
	vec3 position;
	float intensity;
	float constant;
	float linear;
	float exponent;
};

struct SpotLight {
	PointLight pl;
	vec3 conedir;
	float cutoff;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform DirectionalLight directionalLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColors(Material material, vec2 texCoord) {
	if(material.hasTexture == 1) {
		ambientC = texture(textureSampler, fragTextureCoord);
		diffuseC = ambientC;
		specularC = ambientC;
	}else {
		ambientC = material.ambient;
		diffuseC = material.diffuse;
		specularC = material.specular;
	}
}

vec4 calcLightColor(vec3 light_color, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal) {
	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specColor = vec4(0, 0, 0, 0);
	
	float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
	diffuseColor = diffuseC * vec4(light_color, 1.0) * light_intensity * diffuseFactor;
	
	vec3 camera_direction = normalize(-position);
	vec3 from_light_dir = -to_light_dir;
	vec3 reflectedLight = normalize(reflect(from_light_dir, normal));
	float specularFactor = pow(max(dot(camera_direction, reflectedLight), 0.0), specularPower);
	specColor = specularC * light_intensity * specularFactor * material.reflectance * vec4(light_color, 1.0);
	
	return (diffuseColor + specColor);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal){
	vec3 light_dir = light.position - position;
	vec3 to_light_dir = normalize(light_dir);
	vec4 light_color = calcLightColor(light.color, light.intensity, position, to_light_dir, normal);
	
	
	float distance = length(light_dir);
	float attenuationInv = light.constant + light.linear * distance + light.exponent * distance * distance;
	return light_color / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal) {
	vec3 light_dir = light.pl.position - position;
	vec3 to_light_dir = normalize(light_dir);
	vec3 from_light_dir = -to_light_dir;
	float spot_alpha = dot(from_light_dir, normalize(light.conedir));
	
	vec4 color = vec4(0, 0, 0, 0);
	
	if(spot_alpha > light.cutoff) {
		color = calcPointLight(light.pl, position, normal);
		color *= (1.0 - (1.0 - spot_alpha) / (1.0 - light.cutoff));
	}
	return color;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
	return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main(){

	setupColors(material, fragTextureCoord);
	
	vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, fragPos, fragNormal);
	
	for(int i = 0; i < MAX_POINT_LIGHTS; i++) {
		if(pointLights[i].intensity > 0) {
			diffuseSpecularComp += calcPointLight(pointLights[i], fragPos, fragNormal);
		}
	}
	
	for(int i = 0; i < MAX_SPOT_LIGHTS; i++) {
		if(spotLights[i].pl.intensity > 0) {
			diffuseSpecularComp += calcSpotLight(spotLights[i], fragPos, fragNormal);
		}
	}

	fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}



