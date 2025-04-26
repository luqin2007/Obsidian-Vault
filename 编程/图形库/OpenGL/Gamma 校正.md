# Gamma 校正

> [!note] Gamma：灰度系数，$设备输出亮度=电压^{Gamma}$

> [!note] Gamma 校正：对每个像素取 $\frac{1}{2.2}$，约为 0.45 次幂

> [!note] sRGB 颜色空间：基于 Gamma 2.2 的颜色空间

对于CRT，Gamma通常为2.2。人类所感知的亮度恰好和CRT所显示出来相似的指数关系非常匹配，而与物理亮度的梯度不同

![[Pasted image 20230914020740-20240513173107-jqo6crn.png]]

* 自动校正：`glEnable(GL_FRAMEBUFFER_SRGB)`
* 手动校正：片元着色器中 `fColor.rgb = pow(fColor.rgb, vec3(1/gamma))`
* Gamma 纹理：internal format 参数为 `GL_SRGB`

‍
