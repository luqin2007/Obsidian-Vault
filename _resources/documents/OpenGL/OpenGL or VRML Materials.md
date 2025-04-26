# OpenGL/VRML Materials

These numbers come from the OpenGL teapots.c demo, ï¿½ Silicon Graphics, Inc., ï¿½ 1994, Mark J. Kilgard. See also [[1]](https://www.opengl.org/archives/resources/code/samples/sig99/advanced99/notes/node153.html), [[2]](http://web.archive.org/web/20100725103839/http://www.cs.utk.edu/~kuck/materials_ogl.htm), and [[3]](http://www.it.hiof.no/~borres/j3d/explain/light/p-materials.html).

## The numbers

|Name|Ambient|Diffuse|Specular|Shininess|
| ----------------| ----------| ----------| ----------| -----------|
|emerald|0.0215|0.1745|0.0215|0.07568|
|jade|0.135|0.2225|0.1575|0.54|
|obsidian|0.05375|0.05|0.06625|0.18275|
|pearl|0.25|0.20725|0.20725|1|
|ruby|0.1745|0.01175|0.01175|0.61424|
|turquoise|0.1|0.18725|0.1745|0.396|
|brass|0.329412|0.223529|0.027451|0.780392|
|bronze|0.2125|0.1275|0.054|0.714|
|chrome|0.25|0.25|0.25|0.4|
|copper|0.19125|0.0735|0.0225|0.7038|
|gold|0.24725|0.1995|0.0745|0.75164|
|silver|0.19225|0.19225|0.19225|0.50754|
|black plastic|0.0|0.0|0.0|0.01|
|cyan plastic|0.0|0.1|0.06|0.0|
|green plastic|0.0|0.0|0.0|0.1|
|red plastic|0.0|0.0|0.0|0.5|
|white plastic|0.0|0.0|0.0|0.55|
|yellow plastic|0.0|0.0|0.0|0.5|
|black rubber|0.02|0.02|0.02|0.01|
|cyan rubber|0.0|0.05|0.05|0.4|
|green rubber|0.0|0.05|0.0|0.4|
|red rubber|0.05|0.0|0.0|0.5|
|white rubber|0.05|0.05|0.05|0.5|
|yellow rubber|0.05|0.05|0.0|0.5|

## How to use it

### OpenGL

Multiply the shininess by 128!

```c++
mat[0] = ambr;
mat[1] = ambg;
mat[2] = ambb;
mat[3] = 1.0;
glMaterialfv(GL_FRONT, GL_AMBIENT, mat);
mat[0] = difr;
mat[1] = difg;
mat[2] = difb;
glMaterialfv(GL_FRONT, GL_DIFFUSE, mat);
mat[0] = specr;
mat[1] = specg;
mat[2] = specb;
glMaterialfv(GL_FRONT, GL_SPECULAR, mat);
glMaterialf(GL_FRONT, GL_SHININESS, shine * 128.0);
```

### VRML97

Compute ambientIntensity as (0.212671*ambr + 0.715160*ambg + 0.072169*ambb)/(0.212671*difr + 0.715160*difg + 0.072169*difb)

```c++
Material {
ambientIntensity  amb
diffuseColor      difr digg difb
specularColor     specr specg specb
shininess         shine
}
```
# 参考

```cardlink
url: http://devernay.free.fr/cours/opengl/materials.html
title: "OpenGL/VRML Materials"
host: devernay.free.fr
```
