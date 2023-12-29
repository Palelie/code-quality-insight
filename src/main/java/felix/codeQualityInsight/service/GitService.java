package felix.codeQualityInsight.service;

import org.eclipse.jgit.api.errors.GitAPIException;

public interface GitService {
    /**
     * 使用JGit与Git仓库交互（本地不存在仓库时克隆，存在时拉取）
     * @param repoUrl 仓库url
     * @throws GitAPIException
     */
    boolean cloneOrUpdateRepository(String repoUrl);

    /**
     * //从URL中提取仓库名称
     * @param repoUrl 仓库url
     * @return 仓库名
     */
    String extractRepoName(String repoUrl);

    /**
     * 根据仓库名使用SonarQube进行代码质量分析
     * @param repositoryName
     */
    public boolean analyzeCode(String repositoryName,String projectKey);
}
