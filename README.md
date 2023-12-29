# code-quality-insight

### 项目概述：

使用 `SonarQube + Jgit + Maven + Webhooks` 对远程仓库进行自动化代码质量分析

### 实现步骤：

1. 远程仓库配置Webhooks调用项目API，并传入仓库url（SSH与HTTPS皆可）
2. 项目通过`Jgit`克隆/拉取代码到本地仓库
3. 调用SonarQube API获取项目相关参数配置
4. 动态拼接项目Maven构建器脚本并执行，扫描本地仓库代码并提交到SonarQube服务器进行分析
5. 监听SonarQube服务Webhooks API获取扫描分析报告
