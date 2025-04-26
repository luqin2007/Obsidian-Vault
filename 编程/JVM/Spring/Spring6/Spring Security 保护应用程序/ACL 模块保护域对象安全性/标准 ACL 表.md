# 标准 ACL 表

# ACL_CLASS

应用程序中被 ACL 保护的域对象类型

|列名|类型|说明|
| ------| ------| ------------------|
|`id`|`int`|自增主键|
|`class`|`string`|被保护类的全类名|

# ACL_SID

系统中的主体和权限

|列名|类型|说明|
| ------| ------| ------------------------------------------------------|
|`id`|`int`|自增主键|
|`principal`|`bool`|类型，`true` 表示为用户名（主体），`false` 表示为角色（权限）|
|`sid`|`string`|用户名或角色名|

# ACL_OBJECT_IDENTITY

要保护的域对象，通过 `object_id_class`，`object_id_identity` 和 `parent_object` 可以唯一确定域对象

|列名|类型|说明|
| ------| ------| -----------------------------------------|
|`id`|`int`|自增主键|
|`object_id_class`|`string`|域对象全类名|
|`object_id_identity`|`int`|域对象 id|
|`parent_object`|`int?`|若域对象具有父对象，则这里是父对象的 id|
|`owner_sid`|`string`|拥有域对象实例的用户或角色|
|`entries_inheriting`|`bool`|该 ACL 条目是否继承自其他 ACL|

# ACT_ENTRY

分配给域对象的权限

|列名|类型|说明|
| ------| ------| -----------------------------------------------------|
|`id`|`int`|自增主键|
|`acl_object_identity`|`int`|指向 ((20240506234812-d5xihhn "ACL_OBJECT_IDENTITY")) 表 id 列的外键|
|`ace_order`|`int`|访问控制条目顺序，优先级|
|`sid`|`int`|指向 ((20240506234416-9s9qmfg "ACL_SID")) 表 id 列的外键|
|`mask`|`int`|权限掩码：<br />1 - 读<br />2 - 写<br />8 - 删除<br />16 - 管理许可|
|`granting`|`bool`|`mask` 掩码对应的权限是否允许|
|`audit_success`|`bool`|审核成功的权限标记|
|`audit_failure`|`bool`|审核失败的权限标记|
