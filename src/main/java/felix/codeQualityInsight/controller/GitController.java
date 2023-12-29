package felix.codeQualityInsight.controller;

import felix.codeQualityInsight.common.BaseResponse;
import felix.codeQualityInsight.common.ErrorCode;
import felix.codeQualityInsight.common.ResultUtils;
import felix.codeQualityInsight.exception.ThrowUtils;
import felix.codeQualityInsight.model.dto.RepoNameRequest;
import felix.codeQualityInsight.model.dto.RepoUrlRequest;
import felix.codeQualityInsight.service.GitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static felix.codeQualityInsight.common.ErrorCode.PARAMS_ERROR;

/**
 * 用于本地操作，仅作为本地测试使用
 */
@RestController
@RequestMapping("/git")
public class GitController {

    @Autowired
    private GitService gitService;

    /**
     * 使用JGit与Git仓库交互（本地不存在仓库时克隆，存在时拉取）
     */
    @PostMapping("/clone")
    public BaseResponse<String> cloneRepository(@RequestBody RepoUrlRequest body) {
        //获取传入参数
        String repoUrl = body.getRepoUrl();
        //校验参数合法性
        ThrowUtils.throwIf(!StringUtils.hasText(repoUrl),PARAMS_ERROR);
        //使用JGit与Git仓库交互（本地不存在仓库时克隆，存在时拉取）
        return gitService.cloneOrUpdateRepository(repoUrl)?
                ResultUtils.success("success!") : ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    /**
     * 根据仓库名使用SonarQube对代码进行代码质量分析
     */
    @PostMapping("/analyze")
    public BaseResponse<String> repositoryAnalyze(@RequestBody RepoNameRequest body) {
        //获取传入参数
        String repoName = body.getRepoName();
        String projectKey = body.getProjectKey();
        //校验参数合法性
        ThrowUtils.throwIf(!StringUtils.hasText(repoName),PARAMS_ERROR);
        //使用SonarQube对代码进行代码质量分析
        return gitService.analyzeCode(repoName,projectKey) ?
                ResultUtils.success("success!") : ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}

