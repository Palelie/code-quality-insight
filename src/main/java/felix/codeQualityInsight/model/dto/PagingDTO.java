package felix.codeQualityInsight.model.dto;

import lombok.Data;


@Data
public class PagingDTO {
    /**
     * 页面索引
     */
    private Integer pageIndex;
    /**
     * 页面大小
     */
    private Integer pageSize;
    /**
     * 总
     */
    private Integer total;
}
