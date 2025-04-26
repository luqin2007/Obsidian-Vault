扫描代码中过时的 API，包括 jar、类及目录、源文件等

```shell
jdeprscan <路径>
```

- `--class-path <PATH>`：依赖类路径
- `--release <version>`：Java API 版本，不小于 6
- `--for-removal`：仅扫描 `forRemove=true`
- `--verbose`：额外信息
