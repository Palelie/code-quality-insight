package felix.codeQualityInsight.util;


import com.alibaba.fastjson2.JSON;
import felix.codeQualityInsight.config.SonarProperties;
import felix.codeQualityInsight.model.dto.MeasuresDTO;
import felix.codeQualityInsight.model.dto.ProjectInfo;
import felix.codeQualityInsight.model.dto.ProjectPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * sonarqube api client
 */
@Component
public class SonarApiClient {

    @Autowired
    private SonarProperties sonarProperties;


    /**
     * 获取项目列表
     */
    public ProjectPageInfo getProjects() {
        String url = sonarProperties.getUrl() + "api/projects/search";
        Map<String, String> headers = new HashMap<>();
        //这个请求头每个api请求都要加
        headers.put("Authorization", "Basic " + sonarProperties.getToken());
        Map<String, String> params = new HashMap<>();
        params.put("p", "1");
        params.put("ps", "20");
        String response = HttpClientUtil.doGet(url, params, headers);
        return JSON.parseObject(response, ProjectPageInfo.class);
    }

    /**
     * 根据项目名获取项目数据（包括key）
     */
    public ProjectInfo getProjectDataByName(String projectName) {
        String url = sonarProperties.getUrl() + "/api/components/search";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + sonarProperties.getToken());

        Map<String, String> params = new HashMap<>();
        params.put("qualifiers", "TRK");
        params.put("q", projectName);

        // 调用 doGet 方法并返回结果
        String response = HttpClientUtil.doGet(url, params, headers);
        return JSON.parseObject(response, ProjectInfo.class);
    }

    /**
     * 根据projectKey获取项目分析报告
     */
    public MeasuresDTO getProjectAnalysisReport(String projectKey) {
        String url = sonarProperties.getUrl() + "/api/measures/component";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + sonarProperties.getToken());

        Map<String, String> params = new HashMap<>();
        params.put("component", projectKey);
        params.put("metricKeys", "new_bugs,bugs,new_coverage,coverage,new_vulnerabilities,vulnerabilities,new_duplicated_lines_density,duplicated_lines_density,ncloc,complexity,violations");

        // 调用 doGet 方法并返回结果
        String response = HttpClientUtil.doGet(url, params, headers);
        return JSON.parseObject(response, MeasuresDTO.class);
    }
}
