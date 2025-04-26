表现层状态化，设计原则：
# 以资源为中心的 URL

操作以资源为核心，如 `/users/{id}`

> [!note] HATEOAS 原则
> Hypermedia As The Engine Of Application State，直接访问 API 时返回的结果中包含提供的相关资源链接，以便得到后续要访问的地址

例：访问 Github 的 API 地址（`api.github.com`）时返回：

```json title:api.github.com fold
{
    "current_user_url": "https://api.github.com/user",
    "current_user_authorizations_html_url": "https://github.com/settings/connections/applications{/client_id}",
    "authorizations_url": "https://api.github.com/authorizations",
    "code_search_url": "https://api.github.com/search/code?q={query}{&page,per_page,sort,order}",
    "commit_search_url": "https://api.github.com/search/commits?q={query}{&page,per_page,sort,order}",
    "emails_url": "https://api.github.com/user/emails",
    "emojis_url": "https://api.github.com/emojis",
    "events_url": "https://api.github.com/events",
    "feeds_url": "https://api.github.com/feeds",
    "followers_url": "https://api.github.com/user/followers",
    "following_url": "https://api.github.com/user/following{/target}",
    "gists_url": "https://api.github.com/gists{/gist_id}",
    "hub_url": "https://api.github.com/hub",
    "issue_search_url": "https://api.github.com/search/issues?q={query}{&page,per_page,sort,order}",
    "issues_url": "https://api.github.com/issues",
    "keys_url": "https://api.github.com/user/keys",
    "label_search_url": "https://api.github.com/search/labels?q={query}&repository_id={repository_id}{&page,per_page}",
    "notifications_url": "https://api.github.com/notifications",
    "organization_url": "https://api.github.com/orgs/{org}",
    "organization_repositories_url": "https://api.github.com/orgs/{org}/repos{?type,page,per_page,sort}",
    "organization_teams_url": "https://api.github.com/orgs/{org}/teams",
    "public_gists_url": "https://api.github.com/gists/public",
    "rate_limit_url": "https://api.github.com/rate_limit",
    "repository_url": "https://api.github.com/repos/{owner}/{repo}",
    "repository_search_url": "https://api.github.com/search/repositories?q={query}{&page,per_page,sort,order}",
    "current_user_repositories_url": "https://api.github.com/user/repos{?type,page,per_page,sort}",
    "starred_url": "https://api.github.com/user/starred{/owner}{/repo}",
    "starred_gists_url": "https://api.github.com/gists/starred",
    "topic_search_url": "https://api.github.com/search/topics?q={query}{&page,per_page}",
    "user_url": "https://api.github.com/users/{user}",
    "user_organizations_url": "https://api.github.com/user/orgs",
    "user_repositories_url": "https://api.github.com/users/{user}/repos{?type,page,per_page,sort}",
    "user_search_url": "https://api.github.com/search/users?q={query}{&page,per_page,sort,order}"
}
```
# 正确使用 HTTP 请求方式和状态码

| 请求方式   | 说明               |
| ------ | ---------------- |
| GET    | 获取资源             |
| POST   | 创建资源             |
| DELETE | 删除资源             |
| PATCH  | 更新资源，但常被 POST 代替 |
- 2XX：成功处理
- 3XX：重定向，对应资源位置发生变化
- 4XX：客户端请求错误
- 5XX：服务器错误
# 查询及分页原则

通过路径中查询条件进行过滤、分页和排序设定
- `?<property>=<value>`
- `?limit=<count>`
- `?page=<page>`
- `?sort=<properties>(,ASC|DESC)`
# 其他指导原则

- 默认使用 `json` 作为返回类型，需要其他类型使用 `Accept` 请求头指定
- 所有 API 使用同一个专用域名，如 `api.xxx.com/...`
- 请求中带有 API 版本，如 `api.xxx.com/v1/...`
- 请求发生错误时，返回错误码和详细信息
