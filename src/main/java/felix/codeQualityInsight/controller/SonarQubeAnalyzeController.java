package felix.codeQualityInsight.controller;

import felix.codeQualityInsight.model.dto.MeasuresDTO;
import felix.codeQualityInsight.service.impl.SonarQubeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
public class SonarQubeAnalyzeController {

    @Autowired
    private SonarQubeServiceImpl sonarQubeService;

    /**
     * 接收 WebHooks 触发代码分析和计算
     */

    @PostMapping("/analyze")
    public void repositoryAnalyze(@RequestParam String repoUrl) {
        MeasuresDTO measuresDTO = sonarQubeService.triggerAnalysis(repoUrl);
        System.out.println(measuresDTO);
    }
}
