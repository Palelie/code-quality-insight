package felix.codeQualityInsight.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectPageInfo {

    private PagingDTO paging;


    private List<ComponentsDTO> components;

}
