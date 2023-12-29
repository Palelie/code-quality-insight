package felix.codeQualityInsight.service;

import felix.codeQualityInsight.model.dto.MeasuresDTO;
import org.eclipse.jgit.api.errors.GitAPIException;

public interface SonarQubeService {

    /**
     * 接收 WebHooks 触发代码分析和计算
     */
    MeasuresDTO triggerAnalysis(String repoName);
}
