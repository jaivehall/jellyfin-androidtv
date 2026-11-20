# 更新日志

[English](CHANGELOG.md) | **简体中文**

本文件记录本项目的所有重要变更。

格式遵循 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)。

## [1.0.2] - 2026-11-20

### 新增

- **Finplayer TV 品牌化**：应用显示名改为 `Finplayer` / `飞影视界` / `飛影視界`，随系统语言自适应；`applicationId` 改为 `com.finplayer.androidtv`。
- **全新品牌视觉**：Mint `#00E0A4` + Ink `#0C1417` 配色、鳍形播放符号；桌面图标、横版 banner、应用内 logo、Splash、设置菜单图标、单色图标全套替换，并提供简 / 繁 / 英多语言矢量字标。
- **水中倒影风格 logo**：桌面 launcher、设置菜单项、登录页标题、Splash 统一改为「上半实体 + 下半渐隐倒影 + 水面高光」；VectorDrawable 不支持渐变填充，改用 20 段递减 alpha 水平切片模拟。
- **默认服务器地址构建期注入**：地址不再硬编码在源码，改为构建期由 `local.properties` / 环境变量 / Gradle 属性注入 `BuildConfig`；新增不含真实地址的 `local.properties.example` 模板。未配置时为空串并回退原生「添加服务器」流程，干净 clone 仍可编译。
- **首次启动直达登录页**：未登录时自动登记默认服务器并直接进入用户名密码登录页，跳过 QuickConnect；登记过程不依赖网络发现评分，网络只在真正认证时才需要。
- **播放偏好默认值**：ASS / PGS 字幕默认直接播放；AC3 / EAC3 / DTS / TrueHD 默认开启比特流透传。

### 修复

- 暗色背景下 logo 字标不可见：应用为固定深色主题，字标统一改为白色，不跟随系统明暗切换。
- 中文系统残留英文：欢迎语、登录页副标题、设置页「管理服务器」、关于页版本标题与 caption 全部本地化；已存的旧英文服务器记录做显示层兜底，无需清除 App 数据。
- 中文系统 logo 字标仍为英文：新增 `drawable-zh-rCN` / `drawable-zh-rTW` 中文矢量字标，横版 logo、banner、Splash 均按 locale 自动切换。
- 设置页播放相关图标被 adaptive-icon 圆形遮罩切角：新增普通 vector `app_icon_list` 替代 `R.mipmap.app_icon` 并去掉二次 clip；外部播放器仍显示真实应用图标。
- 桌面 banner / 横版 logo 字标未垂直居中：按字标 bbox 中心重算 `translateY`，并补齐中英文密度限定符资源。
- release 全量编译报 `Expecting a top level declaration`：删除 `ServerConstants.kt` 末尾残留字符。

### 变更

- 版本号统一由 `gradle.properties` 的 `jellyfin.version` 管理（`1.0.2`）。
- 50+ 语言 `strings.xml` 批量替换品牌文案，清理 Jellyfin 字样。
- 关于页 / 设置页图标引用改为自有 `ic_finplayer`，caption 改为 `finplayer-androidtv`。
- `.gitignore` 增加本地工作数据与构建产物规则，避免将含私有信息的文件提交到公开仓库。
