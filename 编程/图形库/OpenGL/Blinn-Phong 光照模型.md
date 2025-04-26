Phong 光照模型的镜面反射会在一些情况下出现问题，特别是物体反光度很低时，会导致大片（粗糙的）高光区域，如下图镜面高光区域的边缘出现了一道很明显的断层。

![[Pasted image 20230914015649-20240513172704-m4fsgk0.png]]

>[!note] 原因：观察向量和反射向量夹角大于 90°，点积的结果为负数时镜面光分量为 $0.0$。

````col
```col-md
flexGrow=1
===
考虑漫反射光的时候，如果法线和光源夹角大于90度，光源会处于被照表面的下方，这个时候光照的漫反射分量的确是为 $0.0$。

考虑镜面高光时，我们测量的角度并不是光源与法线的夹角，而是视线与反射光线向量的夹角。
```
```col-md
flexGrow=1
===
![[Pasted image 20230914015826-20240513172725-d2qh4b4.png]]
```
````

````col
```col-md
flexGrow=1
===
Blinn-Phong模型采用半程向量(Halfway Vector)，即光线与视线夹角一半方向上的一个单位向量替换反射向量。当半程向量与法线向量越接近时，镜面光分量就越大。
```
```col-md
flexGrow=1
===
![[Pasted image 20230914015956-20240513172731-m8sguhb.png]]
```
````

```cpp
lightDir = normalize(lightPos - vFrogPos);
viewDir = normalize(viewPos - vFrogPos);
// Blinn-Phong 模型
vec3 halfwayDir = normalize(lightDir + viewDir);
float spec = pow(max(dot(normalize, halfwayDir), 0), 32);
```

![[Pasted image 20230914020228-20240513172900-7lf5qn9.png]]![[Pasted image 20230914020252-20240513172904-5deggvx.png]]

> [!warning] Blinn-Phong 的镜面光分量会比冯氏模型更锐利一些。Blinn-Phong 的反光度通常是 Phong 模型的 2-4 倍
