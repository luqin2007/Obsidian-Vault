Assimp （开放资产导入库，Open Asset Import Library）是一个开源的模型导入库，支持导入、导出各种常见格式的模型类型。

Assimp 解析后的模型结构如下：
* `Scene`：包含场景根节点引用和所有场景、模型数据
* `Root Node`：根节点，包含所有子节点和 Mesh
* `Mesh`：多个图元组成的片段，包含渲染相关的所有数据，包含顶点、法向量、纹理、面、材质等
    * `Face`：面，渲染图元的顶点和索引
* `Material`：物体材质属性，包括颜色、纹理、光照贴图等

![[Pasted image 20230912125158.png]]

使用 NuGet 添加依赖，或从 [assimp/assimp: The official Open-Asset-Importer-Library Repository (github.com)](https://github.com/assimp/assimp) 下载，解压后将 `include` 目录加入头文件，并添加 assimp 作为依赖。

```cpp
Assimp::Importer importer;

// 加载模型
const aiScene* scene = importer.ReadFile(file, aiProcess_Triangulate);
if (scene == nullptr || (scene->mFlags & AI_SCENE_FLAGS_INCOMPLETE) == AI_SCENE_FLAGS_INCOMPLETE)
{
	const char* err = importer.GetErrorString();
	// 异常处理
}

// 加载材质
if (scene->HasMaterials())
{
	aiMaterial** materials = scene->mMaterials;
	for (int i = 0; i < scene->mNumMaterials; ++i)
	{
		aiMaterial* material = &materials[i];
		// 通过 GetTextureCount 获取每种材质的个数
		// 通过 GetTexture 获取每种材质的详细信息
	}
}

// 加载节点 DFS
std::list<const aiNode*> nodes;
nodes.push_front(scene->mRootNode);
while (!nodes.empty())
{
	const aiNode* node = nodes.back();
	nodes.pop_back();

	for (int i = 0; i < node->mNumChildren; ++i)
	{
		nodes.push_front(node->mChildren[0]);
	}

	// 加载 Mesh
	if (node->mNumMeshes)
	{
		for (int i = 0; i < node->mNumMeshes; ++i)
		{
			unsigned mesh_id = node->mMeshes[i];
			aiMesh* mesh = scene->mMeshes[mesh_id];

			// 面，顶点数组，法向量等信息，这里仅以顶点数组为例
			for (int j = 0; j < mesh->mNumVertices; ++j)
			{
				// 顶点数组
				aiVector3D v = mesh->mVertices[j];
			}
		}
	}
}

// 读取结束
importer.FreeScene();
```

`flags` 为导入时使用的参数，常用值有：
* `aiProcess_Triangulate`：将非三角形图元转换成三角形
* `aiProcess_FlipUVs`：翻转 y 轴纹理坐标
* `aiProcess_GenNormals`：若纹理中不存在法向量信息，自动生成法向量
* `aiProcess_SplitLargeMeshes`：将较大的网格分割成更小的网格，避免最大顶点数限制
* `aiProcess_OptimizeMeshes`：将较小的网格合并成更大的网格，减少绘制调用
# Assimp 类结构

* `AIScene`：模型入口，存储所有 `Materials`，`Textures`，`Meshes`，`Animations`，`Cameras`，`Lights` 等
    * `mRootNode`：`AINode` 根节点
    * `mMetaData`：元数据
* `AINode`：模型节点，保存一组模型数据
    * 一个节点可包含多个子节点
    * 一个节点可包含多个 `Mesh`
* `AIMesh`：模型片段，表示一个面，包含多个图元
    * 包含实际的顶点、法线、纹理材质等数据
* `AIFace`：`AIMesh` 中的绘制顺序，对应 `glDrawElement` 中的顶点索引

```cardlink
url: https://learnopengl-cn.github.io/03%20Model%20Loading/01%20Assimp/
title: "Assimp - LearnOpenGL CN"
description: "http://learnopengl.com 系列教程的简体中文翻译"
host: learnopengl-cn.github.io
```
