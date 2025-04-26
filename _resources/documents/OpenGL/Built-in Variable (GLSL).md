# Built-in Variable (GLSL)

The [OpenGL Shading Language](https://www.khronos.org/opengl/wiki/OpenGL_Shading_Language "OpenGL Shading Language") defines a number of special variables for the various shader stages. These **built-in variables** (or built-in variables) have special properties. They are usually for communicating with certain fixed-functionality. By convention, all predefined variables start with "gl_"; no user-defined variables may start with this.

**Note:**  This page only describes the core OpenGL shading language pre-defined variables. Any variables that are from the compatibility profiles are not listed here.

## Vertex shader inputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=1 "Edit section: Vertex shader inputs")]

[V](https://www.khronos.org/opengl/wiki/Vertex_Shader/Defined_Inputs "Vertex Shader/Defined Inputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Vertex_Shader/Defined_Inputs&action=edit)

[Vertex Shaders](https://www.khronos.org/opengl/wiki/Vertex_Shader "Vertex Shader") have the following built-in input variables.

```
in int gl_VertexID;
in int gl_InstanceID;
in int gl_DrawID; // Requires GLSL 4.60 or ARB_shader_draw_parameters
in int gl_BaseVertex; // Requires GLSL 4.60 or ARB_shader_draw_parameters
in int gl_BaseInstance; // Requires GLSL 4.60 or ARB_shader_draw_parameters
```

gl_VertexIDthe index of the vertex currently being processed. When using non-indexed rendering, it is the effective index of the current vertex (the number of vertices processed + the **first** value). For indexed rendering, it is the index used to fetch this vertex from the buffer.**Note:**  **gl_VertexID** will have the [baseVertex parameter](https://www.khronos.org/opengl/wiki/Draw_Base_Index "Draw Base Index") added to the index, if there was such a parameter in the rendering command.

gl_InstanceIDthe index of the current instance when doing some form of [instanced rendering](https://www.khronos.org/opengl/wiki/Instancing "Instancing"). The instance count *always* starts at 0, even when using base instance calls. When not using instanced rendering, this value will be 0.**Warning:**  This value ***does not*** follow the [baseInstance provided by some instanced rendering functions](https://www.khronos.org/opengl/wiki/Instancing "Instancing"). **gl_InstanceID** always falls on the half-open range [0, **instancecount**). If you have GLSL 4.60, you may use **gl_BaseInstance** to compute the proper instance index.

gl_DrawIDthe index of the drawing command within [multi-draw rendering](https://www.khronos.org/opengl/wiki/Multidraw_Rendering "Multidraw Rendering") commands (including indirect multi-draw commands). The first draw command has an ID of 0, increasing by one as the renderer passes through drawing commands.This value will always be a [Dynamically Uniform Expression](https://www.khronos.org/opengl/wiki/Dynamically_Uniform_Expression "Dynamically Uniform Expression").**gl_BaseVertex**the value of the **baseVertex** parameter of the rendering command. If the rendering command did not include that parameter, the value of this input will be 0.**gl_BaseInstance**the value of the **baseInstance** parameter of the [instanced rendering command](https://www.khronos.org/opengl/wiki/Instancing "Instancing"). If the rendering command did not include this parameter, the value of this input will be 0.## Vertex shader outputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=2 "Edit section: Vertex shader outputs")]

[V](https://www.khronos.org/opengl/wiki/Vertex_Shader/Defined_Outputs "Vertex Shader/Defined Outputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Vertex_Shader/Defined_Outputs&action=edit)

[Vertex Shaders](https://www.khronos.org/opengl/wiki/Vertex_Shader "Vertex Shader") have the following predefined outputs.

```
out gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
};
```

**gl_PerVertex** defines an [interface block](https://www.khronos.org/opengl/wiki/Interface_Block_(GLSL)) "Interface Block (GLSL)") for outputs. The block is defined without an instance name, so that prefixing the names is not required.

These variables only take on the meanings below if this shader is the last active [Vertex Processing](https://www.khronos.org/opengl/wiki/Vertex_Processing "Vertex Processing") stage, and if rasterization is still active (ie: **GL_RASTERIZER_DISCARD** is not enabled). The text below explains how the [Vertex Post-Processing](https://www.khronos.org/opengl/wiki/Vertex_Post-Processing "Vertex Post-Processing") system uses the variables. These variables may not be redeclared with interpolation qualifiers.

gl_Positionthe clip-space output position of the current vertex.**gl_PointSize**the pixel width/height of the point being rasterized. It only has a meaning when rendering [point primitives](https://www.khronos.org/opengl/wiki/Point_Primitive "Point Primitive"). It will be clamped to the **GL_POINT_SIZE_RANGE**.**gl_ClipDistance**allows the shader to set the distance from the vertex to each [user-defined clipping half-space](https://www.khronos.org/opengl/wiki/User-Defined_Clip_Plane "User-Defined Clip Plane"). A non-negative distance means that the vertex is inside/behind the clip plane, and a negative distance means it is outside/in front of the clip plane. Each element in the array is one clip plane. In order to use this variable, the user must manually redeclare it with an explicit size. With GLSL 4.10 or [ARB_separate_shader_objects](http://www.opengl.org/registry/specs/ARB/separate_shader_objects.txt), the whole **gl_PerVertex** block needs to be redeclared. Otherwise just the **gl_ClipDistance** built-in needs to be redeclared.## Tessellation control shader inputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=3 "Edit section: Tessellation control shader inputs")]

[V](https://www.khronos.org/opengl/wiki/Tessellation_Control_Shader/Defined_Inputs "Tessellation Control Shader/Defined Inputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Tessellation_Control_Shader/Defined_Inputs&action=edit)

[Tessellation Control Shaders](https://www.khronos.org/opengl/wiki/Tessellation_Control_Shader "Tessellation Control Shader") provide the following built-in input variables:

```
 in int gl_PatchVerticesIn;
 in int gl_PrimitiveID;
 in int gl_InvocationID;
```

gl_PatchVerticesInthe number of vertices in the input patch.**gl_PrimitiveID**the index of the current patch within this rendering command.**gl_InvocationID**the index of the TCS invocation within this patch. A TCS invocation writes to per-vertex output variables by using this to index them.The TCS also takes the built-in variables output by the [vertex shader](https://www.khronos.org/opengl/wiki/Vertex_Shader#Outputs "Vertex Shader"):

```
in gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
} gl_in[gl_MaxPatchVertices];
```

Note that just because **gl_in** is defined to have **gl_MaxPatchVertices** entries does not mean that you can access beyond **gl_PatchVerticesIn** and get reasonable values. These variables have only the meaning the vertex shader that passed them gave them.

## Tessellation control shader outputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=4 "Edit section: Tessellation control shader outputs")]

[V](https://www.khronos.org/opengl/wiki/Tessellation_Control_Shader/Defined_Outputs "Tessellation Control Shader/Defined Outputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Tessellation_Control_Shader/Defined_Outputs&action=edit)

[Tessellation Control Shaders](https://www.khronos.org/opengl/wiki/Tessellation_Control_Shader "Tessellation Control Shader") have the following built-in patch output variables:

```
patch out float gl_TessLevelOuter[4];
patch out float gl_TessLevelInner[2];
```

These define the outer and inner tessellation levels used by the [tessellation primitive generator](https://www.khronos.org/opengl/wiki/Tessellation#Tessellation_primitive_generation "Tessellation"). They define how much tessellation to apply to the patch. Their exact meaning depends on the type of patch (and other settings) defined in the [Tessellation Evaluation Shader](https://www.khronos.org/opengl/wiki/Tessellation_Evaluation_Shader "Tessellation Evaluation Shader").

**Note:**  If any of the outer levels used by the abstract patch type is 0 or negative (or NaN), then the patch will be discarded by the generator, and no TES invocations for this patch will result.

As with any other **patch** variable, multiple TCS invocations for the same patch can write to the same tessellation level variable, so long as they are all computing and writing the exact same value.

TCS's also provide the following optional per-vertex output variables:

```
out gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
} gl_out[];
```

The use of any of these in a TCS is completely optional. Indeed, their semantics will generally be of no practical value to the TCS. They have the same general meaning as for [vertex shaders](https://www.khronos.org/opengl/wiki/Vertex_Shader/Defined_Outputs "Vertex Shader/Defined Outputs"), but since a TCS must always be followed by an evaluation shader, the TCS never has to write to any of them.

## Tessellation evaluation shader inputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=5 "Edit section: Tessellation evaluation shader inputs")]

[V](https://www.khronos.org/opengl/wiki/Tessellation_Evaluation_Shader/Defined_Inputs "Tessellation Evaluation Shader/Defined Inputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Tessellation_Evaluation_Shader/Defined_Inputs&action=edit)

[Tessellation Evaluation Shaders](https://www.khronos.org/opengl/wiki/Tessellation_Evaluation_Shader "Tessellation Evaluation Shader") have the following built-in inputs.

```
in vec3 gl_TessCoord;
in int gl_PatchVerticesIn;
in int gl_PrimitiveID;
```

gl_TessCoordthe location within the [tessellated abstract patch](https://www.khronos.org/opengl/wiki/Abstract_Patch "Abstract Patch") for this particular vertex. Every input parameter other than this one will be identical for all TES invocations within a patch.Which components of this **vec3** that have valid values depends on the abstract patch type. For **isolines** and **quads**, only the XY components have valid values. For **triangles**, all three components have valid values. All valid values are normalized floats (on the range [0, 1]).**gl_PatchVerticesIn**the vertex count for the patch being processed. This is either the output vertex count specified by the TCS, or the patch vertex size specified by [glPatchParameter](https://www.khronos.org/opengl/wiki/GLAPI/glPatchParameter "GLAPI/glPatchParameter") if no TCS is active. Attempts to index per-vertex inputs by a value greater than or equal to **gl_PatchVerticesIn** results in undefined behavior.**gl_PrimitiveID**the index of the current patch in the series of patches being processed for this draw call. [Primitive restart](https://www.khronos.org/opengl/wiki/Primitive_restart "Primitive restart"), if used, has no effect on the primitive ID.**Note:**  The [tessellation primitive generator](https://www.khronos.org/opengl/wiki/Tessellation#Tessellation_primitive_generator "Tessellation") will cull patches that have a zero for one of the active outer tessellation levels. [The intent of the specification seems to be](https://www.khronos.org/bugzilla/show_bug.cgi?id=754) that **gl_PrimitiveID** will still be incremented for culled patches. So the primitive ID for the TES is equivalent to the ID for the TCS invocations that generated that patch. But this is not entirely clear from the spec itself.

The TES also has access to the tessellation levels provided for the patch by the TCS or by OpenGL:

```
patch in float gl_TessLevelOuter[4];
patch in float gl_TessLevelInner[2];
```

Only the outer and inner levels actually used by the abstract patch are valid. For example, if this TES uses **isolines**, only **gl_TessLevelOuter[0]**  and **gl_TessLevelOuter[1]**  will have valid values.

The TES also takes the built-in per-vertex variables output by the [TCS](https://www.khronos.org/opengl/wiki/TCS_Output "TCS Output"):

```
in gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
} gl_in[gl_MaxPatchVertices];
```

Note that just because **gl_in** is defined to have **gl_MaxPatchVertices** entries does not mean that you can access beyond **gl_PatchVerticesIn** and get reasonable values.

## Tessellation evaluation shader outputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=6 "Edit section: Tessellation evaluation shader outputs")]

[V](https://www.khronos.org/opengl/wiki/Tessellation_Evaluation_Shader/Defined_Outputs "Tessellation Evaluation Shader/Defined Outputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Tessellation_Evaluation_Shader/Defined_Outputs&action=edit)

[Tessellation Evaluation Shaders](https://www.khronos.org/opengl/wiki/Tessellation_Evaluation_Shader "Tessellation Evaluation Shader") have the following built-in outputs.

```
out gl_PerVertex {
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
};
```

**gl_PerVertex** defines an [interface block](https://www.khronos.org/opengl/wiki/Interface_Block_(GLSL)) "Interface Block (GLSL)") for outputs. The block is defined without an instance name, so that prefixing the names is not required.

These variables only take on the meanings below if this shader is the last active [Vertex Processing](https://www.khronos.org/opengl/wiki/Vertex_Processing "Vertex Processing") stage, and if rasterization is still active (ie: **GL_RASTERIZER_DISCARD** is not enabled). The text below explains how the [Vertex Post-Processing](https://www.khronos.org/opengl/wiki/Vertex_Post-Processing "Vertex Post-Processing") system uses the variables. These variables may not be redeclared with interpolation qualifiers.

gl_Positionthe clip-space output position of the current vertex.**gl_PointSize**the [pixel width/height of the point](https://www.khronos.org/opengl/wiki/Point_Sprite "Point Sprite") being rasterized. It only has a meaning when rendering point primitives, which in a TES requires using the **point_mode** input layout qualifier.**gl_ClipDistance**allows the shader to set the distance from the vertex to each [User-Defined Clip Plane](https://www.khronos.org/opengl/wiki/User-Defined_Clip_Plane "User-Defined Clip Plane"). A positive distance means that the vertex is inside/behind the clip plane, and a negative distance means it is outside/in front of the clip plane. Each element in the array is one clip plane. In order to use this variable, the user must manually redeclare it with an explicit size.## Geometry shader inputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=7 "Edit section: Geometry shader inputs")]

[V](https://www.khronos.org/opengl/wiki/Geometry_Shader/Defined_Inputs "Geometry Shader/Defined Inputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Geometry_Shader/Defined_Inputs&action=edit)

[Geometry Shaders](https://www.khronos.org/opengl/wiki/Geometry_Shader "Geometry Shader") provide the following built-in input variables:

```
in gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
} gl_in[];
```

These variables have only the meaning the prior shader stage(s) that passed them gave them.

There are some GS input values that are based on primitives, not vertices. These are not aggregated into arrays. These are:

```
in int gl_PrimitiveIDIn;
in int gl_InvocationID; // Requires GLSL 4.0 or ARB_gpu_shader5
```

gl_PrimitiveIDInthe current input primitive's ID, based on the number of primitives processed by the GS since the current drawing command started.**gl_InvocationID**the current instance, as defined when [instancing geometry shaders](https://www.khronos.org/opengl/wiki/Geometry_Shader_Instancing "Geometry Shader Instancing").## Geometry shader outputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=8 "Edit section: Geometry shader outputs")]

[V](https://www.khronos.org/opengl/wiki/Geometry_Shader/Defined_Outputs "Geometry Shader/Defined Outputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Geometry_Shader/Defined_Outputs&action=edit)

[Geometry Shaders](https://www.khronos.org/opengl/wiki/Geometry_Shader "Geometry Shader") have the following built-in outputs.

```
out gl_PerVertex
{
  vec4 gl_Position;
  float gl_PointSize;
  float gl_ClipDistance[];
};
```

**gl_PerVertex** defines an [interface block](https://www.khronos.org/opengl/wiki/Interface_Block_(GLSL)) "Interface Block (GLSL)") for outputs. The block is defined without an instance name, so that prefixing the names is not required.

The GS is the final [Vertex Processing](https://www.khronos.org/opengl/wiki/Vertex_Processing "Vertex Processing") stage. Therefore, unless rasterization is being turned off, you must write to some of these values. These outputs are always associated with stream 0. So if you're emitting vertices to a different stream, you don't have to write to them.

gl_Positionthe clip-space output position of the current vertex. This value must be written if you are emitting a vertex to stream 0, unless rasterization is off.**gl_PointSize**the pixel width/height of the point being rasterized. It is only necessary to write to this when outputting [point primitives](https://www.khronos.org/opengl/wiki/Point_Primitive "Point Primitive").**gl_ClipDistance**allows the shader to set the distance from the vertex to each [User-Defined Clip Plane](https://www.khronos.org/opengl/wiki/User-Defined_Clip_Plane "User-Defined Clip Plane"). A positive distance means that the vertex is inside/behind the clip plane, and a negative distance means it is outside/in front of the clip plane. In order to use this variable, the user must manually redeclare it (and therefore the interface block) with an explicit size.Certain predefined outputs have special meaning and semantics.

```
out int gl_PrimitiveID;
```

The primitive ID will be passed to the fragment shader. The primitive ID for a particular line/triangle will be taken from the [provoking vertex](https://www.khronos.org/opengl/wiki/Provoking_Vertex "Provoking Vertex") of that line/triangle, so make sure that you are writing the correct value for the right provoking vertex.

The meaning for this value is whatever you want it to be. However, if you want to match the standard OpenGL meaning (ie: what the [Fragment Shader](https://www.khronos.org/opengl/wiki/Fragment_Shader "Fragment Shader") would get if no GS were used), you must do this for each vertex before emitting it:

```
gl_PrimitiveID = gl_PrimitiveIDIn;
```

This naturally assumes that the number of primitives output by the GS equals the number of primitives received by the GS.

[V](https://www.khronos.org/opengl/wiki/Geometry_Shader/Defined_Outputs_Layered "Geometry Shader/Defined Outputs Layered")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Geometry_Shader/Defined_Outputs_Layered&action=edit)

Layered rendering in the [GS](https://www.khronos.org/opengl/wiki/Geometry_Shader "Geometry Shader") works via two special output variables:

```
out int gl_Layer;
out int gl_ViewportIndex; // Requires GL 4.1 or ARB_viewport_array.
```

The **gl_Layer** output defines which layer in the layered image the primitive goes to. Each vertex in the primitive must get the same layer index. Note that when rendering to cubemap arrays, the **gl_Layer** value represents layer-faces (the faces within a layer), not the layers of cubemaps.

**gl_ViewportIndex**, which requires GL 4.1 or [ARB_viewport_array](http://www.opengl.org/registry/specs/ARB/viewport_array.txt), specifies which viewport index to use with this primitive.

**Note:**  [ARB_viewport_array](http://www.opengl.org/registry/specs/ARB/viewport_array.txt), while technically a 4.1 feature, is widely available on 3.3 hardware, from both NVIDIA and AMD.

## Fragment shader inputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=9 "Edit section: Fragment shader inputs")]

[V](https://www.khronos.org/opengl/wiki/Fragment_Shader/Defined_Inputs "Fragment Shader/Defined Inputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Fragment_Shader/Defined_Inputs&action=edit)

[Fragment Shaders](https://www.khronos.org/opengl/wiki/Fragment_Shader "Fragment Shader") have the following built-in input variables.

```
in vec4 gl_FragCoord;
in bool gl_FrontFacing;
in vec2 gl_PointCoord;
```

gl_FragCoordThe location of the fragment in window space. The X, Y and Z components are the window-space position of the fragment. The Z value will be written to the depth buffer if **gl_FragDepth** is not written to by this shader stage. The W component of **gl_FragCoord** is 1/W~clip~, where W~clip~ is the interpolated W component of the clip-space vertex position output to **gl_Position** from the last [Vertex Processing](https://www.khronos.org/opengl/wiki/Vertex_Processing "Vertex Processing") stage.The space of **gl_FragCoord** can be modified by redeclaring **gl_FragCoord** with special input layout qualifiers:

```
layout(origin_upper_left) in vec4 gl_FragCoord;
```

This means that the origin for **gl_FragCoord**'s window-space will be the upper-left of the screen, rather than the usual lower-left.

```
layout(pixel_center_integer) in vec4 gl_FragCoord;
```

OpenGL window space is defined such that pixel centers are on half-integer boundaries. So the center of the lower-left pixel is (0.5, 0.5). Using **pixel_center_integer** adjust **gl_FragCoord** such that whole integer values represent pixel centers.Both of these exist to be compatible with D3D's window space. Unless you need your shaders to have this compatibility, you are advised not to use these features.**gl_FrontFacing**This is false if the fragment was generated by the [back-face of the primitive](https://www.khronos.org/opengl/wiki/Winding_Order "Winding Order"); it is true in all other cases (including [Primitives](https://www.khronos.org/opengl/wiki/Primitive "Primitive") that have no back face).**gl_PointCoord**The location within a [point primitive](https://www.khronos.org/opengl/wiki/Primitive#Point_Primitives "Primitive") that defines the position of the fragment relative to the side of the point. Points are effectively rasterized as window-space squares of a certain pixel size. Since points are defined by a single vertex, the only way to tell where in that square a particular fragment is is with **gl_PointCoord**.The values of **gl_PointCoord**'s coordinates range from [0, 1]. OpenGL uses a upper-left origin for point-coordinates by default, so (0, 0) is the upper-left. However, the origin can be switched to a bottom-left origin by calling [glPointParameteri(GL_POINT_SPRITE_COORD_ORIGIN, GL_LOWER_LEFT);](https://www.khronos.org/opengl/wiki/GLAPI/glPointParameter "GLAPI/glPointParameter")OpenGL 4.0 and above define additional system-generated input values:

```
in int gl_SampleID;
in vec2 gl_SamplePosition;
in int gl_SampleMaskIn[];
```

gl_SampleIDThis is an integer identifier for the current sample that this fragment is rasterized for.**Warning:**  Any use of this variable *at all* will force this shader to be evaluated per-sample. Since much of the point of [multisampling](https://www.khronos.org/opengl/wiki/Multisample "Multisample") is to avoid that, you should use it only when you must.

gl_SamplePositionThis is the location of the current sample for the fragment within the pixel's area, with values on the range [0, 1]. The origin is the bottom-left of the pixel area.**Warning:**  Any use of this variable *at all* will force this shader to be evaluated per-sample. Since much of the point of [multisampling](https://www.khronos.org/opengl/wiki/Multisample "Multisample") is to avoid that, you should use it only when you must.

gl_SampleMaskInWhen using [multisampling](https://www.khronos.org/opengl/wiki/Multisample "Multisample"), this variable contains a bitfield for the sample mask of the fragment being generated. The array is as long as needed to fill in the number of samples supported by the GL implementation.Some Fragment shader built-in inputs will take values specified by OpenGL, but these values can be overridden by user control.

```
in float gl_ClipDistance[];
in int gl_PrimitiveID;
```

gl_ClipDistanceThis array contains the interpolated clipping plane half-spaces, as output for vertices from the last [Vertex Processing](https://www.khronos.org/opengl/wiki/Vertex_Processing "Vertex Processing") stage.**gl_PrimitiveID**This value is the index of the current primitive being rendered by this [drawing command](https://www.khronos.org/opengl/wiki/Vertex_Rendering "Vertex Rendering"). This includes any [Tessellation](https://www.khronos.org/opengl/wiki/Tessellation "Tessellation") applied to the mesh, so each individual primitive will have a unique index.However, if a [Geometry Shader](https://www.khronos.org/opengl/wiki/Geometry_Shader "Geometry Shader") is active, then the **gl_PrimitiveID** is exactly and only what the GS provided as output. Normally, **gl_PrimitiveID** is guaranteed to be unique, so if two FS invocations have the same primitive ID, they come from the same primitive. But if a GS is active and outputs non-unique values, then different fragment shader invocations for different primitives will get the same value. If the GS did not output a value for **gl_PrimitiveID**, then the fragment shader gets an undefined value.

**Warning:**  The above discussion of **gl_PrimitiveID** is based on a particular reading of the OpenGL 4.6 specification. However, the specification itself is somewhat inconsistent with this view, suggesting that the primitive ID may only get incremented based on data fed to the system, not data generated by, for example, the tessellator. And the Vulkan specification seems to concur with this interpretation, and [at least one implementation is known to agree with that as well](https://forums.khronos.org/showthread.php/83726-gl_PrimitiveID-in-fragment-shader-with-tessellation-enabled). Until there is some [clarification on the issue](https://github.com/KhronosGroup/OpenGL-API/issues/47), you should consider the above to be questionable.

GL 4.3 provides the following additional inputs:

```
in int gl_Layer;
in int gl_ViewportIndex;
```

gl_LayerThis is either 0 or the [layer number for this primitive output by the Geometry Shader](https://www.khronos.org/opengl/wiki/Layered_Rendering "Layered Rendering").**gl_ViewportIndex**This is either 0 or the [viewport index for this primitive output by the Geometry Shader](https://www.khronos.org/opengl/wiki/Layered_Rendering "Layered Rendering").## Fragment shader outputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=10 "Edit section: Fragment shader outputs")]

[V](https://www.khronos.org/opengl/wiki/Fragment_Shader/Defined_Outputs "Fragment Shader/Defined Outputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Fragment_Shader/Defined_Outputs&action=edit)

[Fragment Shaders](https://www.khronos.org/opengl/wiki/Fragment_Shader "Fragment Shader") have the following built-in output variables.

```
out float gl_FragDepth;
```

gl_FragDepthThis output is the fragment's depth. If the shader does not statically write this value, then it will take the value of **gl_FragCoord.z**.To "statically write" to a variable means that you write to it *anywhere* in the program. Even if the writing code is technically unreachable for some reason, if there is a **gl_FragDepth =**   **...**  expression *anywhere* in the shader, then it is statically written.**Warning:**  If the fragment shader statically writes to **gl_FragDepth**, then it is the responsibility of the shader to statically write to the value in *all circumstances*. No matter what branches may or may not be taken, the shader must ensure that the value is written. So, if you conditionally write to it in one place, you should at least make sure that there is a single non-conditional write sometime before that.

GLSL 4.20 or [ARB_conservative_depth](http://www.opengl.org/registry/specs/ARB/conservative_depth.txt) allows the user to specify that modifications to **gl_FragDepth** (relative to the **gl_FragCoord.z** value it would have otherwise had) will happen in certain ways. This allows the implementation the freedom to not turn off [Early Depth Tests](https://www.khronos.org/opengl/wiki/Early_Depth_Test "Early Depth Test") in certain situations.

This is done by re-declaring **gl_FragDepth** with a special layout qualifier:

```
layout (depth_<condition>) out float gl_FragDepth;
```

The **condition** can be one of the following:

anyThe default. You may freely change the depth, but you lose the most potential performance.**greater**You will only make the depth *larger*, compared to **gl_FragCoord.z**.**less**You will only make the depth *smaller*, compared to **gl_FragCoord.z**.**unchanged**If you write to **gl_FragDepth**, you will write exactly **gl_FragCoord.z**.Violating the **condition** yields undefined behavior.

GLSL 4.00 or [ARB_sample_shading](http://www.opengl.org/registry/specs/ARB/sample_shading.txt) brings us:

```
out int gl_SampleMask[];
```

gl_SampleMaskThis defines the sample mask for the fragment when performing [mutlisampled rendering](https://www.khronos.org/opengl/wiki/Multisample "Multisample"). If a shader does not statically write to it, then it will be filled in by **gl_SampleMaskIn**. The sample mask output here will be logically AND'd with the sample mask computed by the rasterizer.**Warning:**  Just as with **gl_FragDepth**, if a fragment shader writes to **gl_SampleMask** at all, it must make sure to write to the value for all execution paths. But it must *also* make sure to write to each element in the array. The array has the same size as **gl_SampleMaskIn**.

## Compute shader inputs **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=11 "Edit section: Compute shader inputs")]

[V](https://www.khronos.org/opengl/wiki/Compute_Shader/Defined_Inputs "Compute Shader/Defined Inputs")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Compute_Shader/Defined_Inputs&action=edit)

[Compute Shaders](https://www.khronos.org/opengl/wiki/Compute_Shader "Compute Shader") have the following built-in input variables.

```
in uvec3 gl_NumWorkGroups;
in uvec3 gl_WorkGroupID;
in uvec3 gl_LocalInvocationID;
in uvec3 gl_GlobalInvocationID;
in uint  gl_LocalInvocationIndex;
```

gl_NumWorkGroupsThis variable contains the number of work groups passed to the dispatch function.**gl_WorkGroupID**This is the current work group for this shader invocation. Each of the XYZ components will be on the half-open range [0, gl_NumWorkGroups.XYZ).**gl_LocalInvocationID**This is the current invocation of the shader *within* the work group. Each of the XYZ components will be on the half-open range [0, **gl_WorkGroupSize.XYZ**).**gl_GlobalInvocationID**This value uniquely identifies this particular invocation of the compute shader among *all* invocations of this compute dispatch call. It's a short-hand for the math computation:

```
gl_WorkGroupID * gl_WorkGroupSize + gl_LocalInvocationID;
```

gl_LocalInvocationIndexThis is a 1D version of **gl_LocalInvocationID**. It identifies this invocation's index *within* the work group. It is short-hand for this math computation:

```
  gl_LocalInvocationIndex =
          gl_LocalInvocationID.z * gl_WorkGroupSize.x * gl_WorkGroupSize.y +
          gl_LocalInvocationID.y * gl_WorkGroupSize.x + 
          gl_LocalInvocationID.x;
```

## Compute shader other variables **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=12 "Edit section: Compute shader other variables")]

```
const uvec3 gl_WorkGroupSize;   // GLSL ≥ 4.30
```

The *gl_WorkGroupSize* variable is a constant that contains the local work-group size of the shader, in 3 dimensions. It is defined by the [layout qualifiers](https://www.khronos.org/opengl/wiki/Layout_Qualifier_(GLSL)) "Layout Qualifier (GLSL)") *local_size_x/y/z*. This is a compile-time constant.

## Shader uniforms **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=13 "Edit section: Shader uniforms")]

[V](https://www.khronos.org/opengl/wiki/Shader/Defined_Uniforms "Shader/Defined Uniforms")  **·**  [E](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Shader/Defined_Uniforms&action=edit)

[Shaders](https://www.khronos.org/opengl/wiki/Shader "Shader") define the following uniforms.

```
struct gl_DepthRangeParameters
{
    float near;
    float far;
    float diff;
};
uniform gl_DepthRangeParameters gl_DepthRange;

uniform int gl_NumSamples; // GLSL 4.20
```

This struct provides access to the [glDepthRange](https://www.khronos.org/opengl/wiki/GLAPI/glDepthRange "GLAPI/glDepthRange") near and far values. The **diff** value is the **far** value minus the **near** value. Do recall that OpenGL makes no requirement that **far** is greater than **near**. With regard to multiple [Viewports](https://www.khronos.org/opengl/wiki/Viewport "Viewport"), **gl_DepthRange** only stores the range for viewport 0.

**gl_NumSamples** is the number of samples in the current [Framebuffer](https://www.khronos.org/opengl/wiki/Framebuffer "Framebuffer"). If the framebuffer is not multisampled, then this value will be 1.

## Constants **[**[edit](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Built-in_Variable_(GLSL)&action=edit&section=14 "Edit section: Constants")]

There are many [implementation-defined shader stage limits](https://www.khronos.org/opengl/wiki/Shader_Resource_Limit "Shader Resource Limit") who's values would be useful to a particular shader. GLSL provides a number of constant integer variables that give these values to shaders. All of these values are available to *all* shader stages.

All of these variables are declared as **const**, so they are considered [constant expressions](https://www.khronos.org/opengl/wiki/Constant_Expression "Constant Expression"). These constants are named based on the OpenGL enumerators used to specify those limitations. The transformation is quite simple: take the GLSL name, put an underscore before every capital letter (unless there's one there already), and then make all the letters capital.

The variables are as follows:

|||Minimum Required Value|
| ---------------------| ------| ------------------------|
|Type|Name|GL 3.3|
|**int**|**gl_MaxVertexAttribs**|16|
|**int**|**gl_MaxVertexOutputComponents**|64|
|**int**|**gl_MaxVertexUniformComponents**|1024|
|**int**|**gl_MaxVertexTextureImageUnits**|16|
|**int**|**gl_MaxGeometryInputComponents**|64|
|**int**|**gl_MaxGeometryOutputComponents**|128|
|**int**|**gl_MaxGeometryUniformComponents**|1024|
|**int**|**gl_MaxGeometryTextureImageUnits**|16|
|**int**|**gl_MaxGeometryOutputVertices**|256|
|**int**|**gl_MaxGeometryTotalOutputComponents**|1024|
|**int**|**gl_MaxGeometryVaryingComponents**|64|
|**int**|**gl_MaxFragmentInputComponents**|128|
|**int**|**gl_MaxDrawBuffers**|8|
|**int**|**gl_MaxFragmentUniformComponents**|1024|
|**int**|**gl_MaxTextureImageUnits**^1^|16|
|**int**|**gl_MaxClipDistances**|8|
|**int**|**gl_MaxCombinedTextureImageUnits**|48|
|Requires OpenGL 4.0|||
|**int**|**gl_MaxTessControlInputComponents**|N/A|
|**int**|**gl_MaxTessControlOutputComponents**|N/A|
|**int**|**gl_MaxTessControlUniformComponents**|N/A|
|**int**|**gl_MaxTessControlTextureImageUnits**|N/A|
|**int**|**gl_MaxTessControlTotalOutputComponents**|N/A|
|**int**|**gl_MaxTessEvaluationInputComponents**|N/A|
|**int**|**gl_MaxTessEvaluationOutputComponents**|N/A|
|**int**|**gl_MaxTessEvaluationUniformComponents**|N/A|
|**int**|**gl_MaxTessEvaluationTextureImageUnits**|N/A|
|**int**|**gl_MaxTessPatchComponents**|N/A|
|**int**|**gl_MaxPatchVertices**|N/A|
|**int**|**gl_MaxTessGenLevel**|N/A|
|Requires OpenGL 4.1|||
|**int**|**gl_MaxViewports**|N/A|
|**int**|**gl_MaxVertexUniformVectors**|N/A|
|**int**|**gl_MaxFragmentUniformVectors**|N/A|
|**int**|**gl_MaxVaryingVectors**|N/A|
|Requires OpenGL 4.2|||
|**int**|**gl_MaxVertexImageUniforms**|N/A|
|**int**|**gl_MaxVertexAtomicCounters**|N/A|
|**int**|**gl_MaxVertexAtomicCounterBuffers**|N/A|
|**int**|**gl_MaxTessControlImageUniforms**|N/A|
|**int**|**gl_MaxTessControlAtomicCounters**|N/A|
|**int**|**gl_MaxTessControlAtomicCounterBuffers**|N/A|
|**int**|**gl_MaxTessEvaluationImageUniforms**|N/A|
|**int**|**gl_MaxTessEvaluationAtomicCounters**|N/A|
|**int**|**gl_MaxTessEvaluationAtomicCounterBuffers**|N/A|
|**int**|**gl_MaxGeometryImageUniforms**|N/A|
|**int**|**gl_MaxGeometryAtomicCounters**|N/A|
|**int**|**gl_MaxGeometryAtomicCounterBuffers**|N/A|
|**int**|**gl_MaxFragmentImageUniforms**|N/A|
|**int**|**gl_MaxFragmentAtomicCounters**|N/A|
|**int**|**gl_MaxFragmentAtomicCounterBuffers**|N/A|
|**int**|**gl_MaxCombinedImageUniforms**|N/A|
|**int**|**gl_MaxCombinedAtomicCounters**|N/A|
|**int**|**gl_MaxCombinedAtomicCounterBuffers**|N/A|
|**int**|**gl_MaxImageUnits**|N/A|
|**int**|**gl_MaxCombinedImageUnitsAndFragmentOutputs**|N/A|
|**int**|**gl_MaxImageSamples**|N/A|
|**int**|**gl_MaxAtomicCounterBindings**|N/A|
|**int**|**gl_MaxAtomicCounterBufferSize**|N/A|
|**int**|**gl_MinProgramTexelOffset**|N/A|
|**int**|**gl_MaxProgramTexelOffset**|N/A|
|Requires OpenGL 4.3|||
|**ivec3**|**gl_MaxComputeWorkGroupCount**|N/A|
|**ivec3**|**gl_MaxComputeWorkGroupSize**|N/A|
|**int**|**gl_MaxComputeUniformComponents**|N/A|
|**int**|**gl_MaxComputeTextureImageUnits**|N/A|
|**int**|**gl_MaxComputeImageUniforms**|N/A|
|**int**|**gl_MaxComputeAtomicCounters**|N/A|
|**int**|**gl_MaxComputeAtomicCounterBuffers**|N/A|
|Requires OpenGL 4.4|||
|**int**|**gl_MaxTransformFeedbackBuffers**|N/A|
|**int**|**gl_MaxTransformFeedbackInterleavedComponents**|N/A|

^1^: This is the number of fragment shader texture image units.

[Categories](https://www.khronos.org/opengl/wiki/Special:Categories "Special:Categories"): * [Pages using deprecated source tags](https://www.khronos.org/opengl/wiki_opengl/index.php?title=Category:Pages_using_deprecated_source_tags&action=edit&redlink=1 "Category:Pages using deprecated source tags (page does not exist)")

* [OpenGL Shading Language](https://www.khronos.org/opengl/wiki/Category:OpenGL_Shading_Language "Category:OpenGL Shading Language")

```cardlink
url: https://www.khronos.org/opengl/wiki/Built-in_Variable_(GLSL)
title: "Built-in Variable (GLSL) - OpenGL Wiki"
host: www.khronos.org
favicon: https://www.khronos.org/favicon.ico
```
