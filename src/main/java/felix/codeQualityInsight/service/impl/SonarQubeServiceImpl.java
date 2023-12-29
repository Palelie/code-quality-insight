package felix.codeQualityInsight.service.impl;

import felix.codeQualityInsight.exception.ThrowUtils;
import felix.codeQualityInsight.model.dto.MeasuresDTO;
import felix.codeQualityInsight.model.dto.ProjectInfo;
import felix.codeQualityInsight.service.GitService;
import felix.codeQualityInsight.service.SonarQubeService;
import felix.codeQualityInsight.util.SonarApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static felix.codeQualityInsight.common.ErrorCode.REPOSITORIES_ERROR;

@Service
public class SonarQubeServiceImpl implements SonarQubeService {

    @Autowired
    private GitService gitService;

    @Autowired
    private SonarApiClient sonarApiClient;
    /**
     * 接收 WebHooks 触发代码分析和计算
     */
    @Override
    public MeasuresDTO triggerAnalysis(String repoUrl) {
        //根据ssh克隆 or 拉取（若仓库已存在）代码到本地
        boolean success = gitService.cloneOrUpdateRepository(repoUrl);
        ThrowUtils.throwIf(!success,REPOSITORIES_ERROR);

        //获取仓库名称
        String repoName = gitService.extractRepoName(repoUrl);

        //从SonarQube服务器获取projectkey
        ProjectInfo projectInfo = sonarApiClient.getProjectDataByName(repoName);
        String projectKey = projectInfo.getComponents().get(0).getKey();

        //执行脚本，使用Maven构造器扫描本地仓库
        boolean success2 = gitService.analyzeCode(repoName,projectKey);

        //TODO 使用SonarQube的 WebHooks 回调获取报告接口
        //等待分析过程
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //获取分析报告
        MeasuresDTO projectAnalysisReport = sonarApiClient.getProjectAnalysisReport(projectKey);
        return projectAnalysisReport;
    }
}

