
# 加载文本

```cpp
cout << "  Loading string " << text << endl;
// 初始化 FreeType
FT_Library ft;
if (FT_Init_FreeType(&ft))
{
	cerr << "初始化 FreeType 失败" << endl;
	return *this;
}

FT_Face face;
// font: 字体所在目录
if (const path font = ROOT / "font" / "MicrosoftYaHei.ttf";
	FT_New_Face(ft, font.string().c_str(), 0, &face))
{
	cerr << "创建 FTFace 失败" << endl;
	return *this;
}
FT_Set_Pixel_Sizes(face, 0, 48);

// text: 要用的文字。这里以 `\0` 结尾
const char* text = "1234567890";
// 添加字形
int p = 0;
char ch;
glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
while ((ch = text[p++]))
{
	if (FT_Load_Char(face, ch, FT_LOAD_RENDER))
	{
		cerr << "创建字形 " << ch << " 失败" << endl;
		continue;
	}

	const char cc[2] = {ch, '\0'};
	// load_font(int width, int height, GLenum format, void* image)
	// 将图片载入 GL
	load_font(face->glyph->bitmap.width, face->glyph->bitmap.rows, GL_RED, face->glyph->bitmap.buffer);
	// set_font_info(unsigned int width, unsigned int height, int bearing_x, int bearing_y, GLuint advance)
	set_font_info(
	    // 字形大小
		face->glyph->bitmap.width, face->glyph->bitmap.rows,
		// 字形基于基线和起点的位置
		face->glyph->bitmap_left, face->glyph->bitmap_top,
		// 起点到下一个字形起点的距离
		static_cast<GLuint>(face->glyph->advance.x)
	);
}
glPixelStorei(GL_UNPACK_ALIGNMENT, 4);

FT_Done_Face(face);
FT_Done_FreeType(ft);
```

# 绘制文本

```cpp
char ch, name[2];
int p = 0;
// 计算绘制坐标
float x, y0;
float x0 = x;
while ((ch = string[p++]))
{
	if (!at_->chars_.contains(ch))
	{
		char ss[200];
		sprintf_s(ss, 200, "找不到纹理：[%c]", ch);
		throw exception(ss);
	}
	// set_font_info 传入的值
	const auto& [width, height, bx, by, adv] = ...;
	x0 -= ((adv >> 6) * scale) / 2;
}

name[1] = '\0';
p = 0;
while ((ch = string[p++]))
{
	name[0] = ch;
	// 纹理坐标，(u, v) 为起点，(w, h) 为宽高
	const auto& [u, v, w, h] = ...;
	// set_font_info 传入的值
	const auto& [width, height, bx, by, adv] = ...;

	x0 += bx * scale;
	y0 -= (height - by) * scale;
	const float x1 = width * scale + x0;
	const float y1 = height * scale + y0;

	const float uu = u + w;
	const float vv = v + h;

    // (x0, y0): (u, v)
    // (x0, y1): (u, vv)
    // (x1, y0): (uu, v)
  
    // (x0, y1): (u, vv)
    // (x1, y1): (uu, vv)
    // (x1, y0): (uu, v)
    draw(x0, y0, x1, y1, u, v, uu, vv);
	x0 += (adv >> 6) * scale; // adv * 64
}
```

‍
