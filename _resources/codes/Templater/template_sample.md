这里是使用 `templater` 创建的一个简单模板

#template-tag1 #template-tag2 #template-tag3 <% '#' + 'template-tag4' %>
 
# config

该笔记通过 <% tp.config.run_mode %> 的方式使用了 <% tp.config.template_file.basename %> 模板文件，并将其应用到了 <% `${tp.config.target_file.basename}.${tp.config.target_file.extension}` %> 文件中。

在调用该模板时，Obsidian 打开的活动文件是：<% tp.config.active_file == null ? "未打开文件": tp.config.active_file.basename %>

# date

今天是 <% tp.date.now() %>
明天是 <% tp.date.tomorrow("YYYY-MM-DD") %>
昨天是 <% tp.date.yesterday() %>

# file

文件内容：`<% tp.file.content %>`
文件标题：<% tp.file.title %>
文件 `tag`：<% tp.file.tags %>
创建日期：<% tp.file.creation_date() %>
修改日期：<% tp.file.last_modified_date("YY-MM-DD HH:mm:ss") %>
文件路径：<% tp.file.path(true) %>
文件所在目录：<% tp.file.folder(true) %>
文件选中内容：<% tp.file.selection() %>

# system

剪贴板内容：<% tp.system.clipboard() %>
输入框：<% await tp.system.prompt('请输入内容：') %>
选择框：<% await tp.system.suggester(s => s, ["item1", "item2", "item3"], "请输入内容：") %>

# web
`quote`：
<% await tp.web.daily_quote() %>
`picture`：
<% await tp.web.random_picture("1", "anything", false) %>