package felix.codeQualityInsight.model.dto;

import lombok.Data;


@Data
public class ComponentsDTO {
    /**
     * project_key
     */
    private String key;
    /**
     * 名字
     */
    private String name;
    /**
     * 限定符
     */
    private String qualifier;
    /**
     * 可见性
     */
    private String visibility;
    /**
     * 最后分析日期
     */
    private String lastAnalysisDate;
    /**
     * 修订
     */
    private String revision;
}
