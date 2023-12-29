package felix.codeQualityInsight.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * id请求
 *
 */
@Data
public class RepoUrlRequest implements Serializable {

    /**
     * 仓库url
     */
    private String repoUrl;

    private static final long serialVersionUID = 1L;
}
