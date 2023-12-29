package felix.codeQualityInsight.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * id请求
 *
 */
@Data
public class RepoNameRequest implements Serializable {

    /**
     * 仓库名
     */
    private String repoName;

    /**
     * projectKey
     */
    private String projectKey;

    private static final long serialVersionUID = 1L;
}
