package felix.codeQualityInsight.service.impl;

import felix.codeQualityInsight.config.SonarProperties;
import felix.codeQualityInsight.config.SshSessionFactory;
import felix.codeQualityInsight.exception.BusinessException;
import felix.codeQualityInsight.service.GitService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static felix.codeQualityInsight.common.Constant.BASE_DIRECTORY;
import static felix.codeQualityInsight.common.Constant.REPOSITORY_PATH;
import static felix.codeQualityInsight.common.ErrorCode.*;

@Service
public class GitServiceImpl implements GitService {

    @Autowired
    private SonarProperties sonarProperties;

    /**
     * 使用JGit与Git仓库交互（本地不存在仓库时克隆，存在时拉取）
     * @param repoUrl 仓库url
     * @throws GitAPIException
     */
    @Override
    public boolean cloneOrUpdateRepository(String repoUrl){
        //设置JGit使用自定义的SshSessionFactory
        SshSessionFactory.setInstance(new SshSessionFactory());
        //截取仓库名
        String repoName = extractRepoName(repoUrl);
        try {
            //设置仓库
            File repoDir = new File(BASE_DIRECTORY, repoName);
            //克隆或更新仓库
            if (repoDir.exists() && new File(repoDir, ".git").exists()) {
                // 如果目录存在并且已经是一个Git仓库，则执行更新操作
                Git git = Git.open(repoDir);
                git.pull().call();
            } else {
                // 如果目录不存在或不是Git仓库，则执行克隆操作
                Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(repoDir)
                        .call();
            }
            return true;
        } catch (GitAPIException e) {   //无法连接到远程仓库
            e.printStackTrace();
            throw new BusinessException(REPOSITORIES_CONNECT_ERROR);
        } catch (IOException e) {      //无法打开本地仓库
            e.printStackTrace();
            throw new BusinessException(LOCAL_REPO_OPEN_ERROR);
        }
    }

    /**
     * //从URL中提取仓库名称
     * @param repoUrl 仓库url
     * @return 仓库名
     */
    @Override
    public String extractRepoName(String repoUrl) {
        //从URL中提取仓库名称
        String[] parts = repoUrl.split("/");
        String lastPart = parts[parts.length - 1];
        return lastPart.replace(".git", "");
    }

    /**
     * 根据本地仓库名使用SonarQube Maven构造器进行代码质量分析
     * @param repositoryName
     */
    @Override
    public boolean analyzeCode(String repositoryName,String projectKey) {
        //本地仓库地址
        String repoBasePath = BASE_DIRECTORY + repositoryName;
        //判断仓库是否存在以及仓库路径是否合法
        if (new File(repoBasePath).exists()){
            try {
                //执行脚本
                executeMavenScript(projectKey,
                        sonarProperties.getUrl(),
                        sonarProperties.getGlobalToken(),
                        REPOSITORY_PATH + repositoryName + "\\");
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        throw new BusinessException(LOCAL_REPO_ERROR);
    }

    /**
     * 到指定目录执行脚本
     */
    private void executeMavenScript(String projectKey, String sonarUrl, String sonarToken, String workingDir) throws IOException, InterruptedException {
        //拼接命令
        String cmd = String.format("mvn clean verify sonar:sonar " +
                        "-Dsonar.projectKey=%s " +
                        "-Dsonar.host.url=%s " +
                        "-Dsonar.login=%s",
                projectKey, sonarUrl, sonarToken);

        ProcessBuilder builder = new ProcessBuilder();
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            //windows环境
            builder.command("cmd.exe", "/c", cmd);
        } else {
            //linux环境
            builder.command("bash", "-c", cmd);
        }
        // 设置执行路径
        builder.directory(new File(workingDir));
        Process process = builder.start();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            // 处理错误情况
            throw new BusinessException(SCRIPT_ERROR);
        }
    }

}
