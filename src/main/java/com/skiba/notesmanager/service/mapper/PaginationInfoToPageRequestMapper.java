package com.skiba.notesmanager.service.mapper;

import com.skiba.notesmanager.api.dto.PaginationInfo;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PaginationInfoToPageRequestMapper {

    public PageRequest map(PaginationInfo paginationInfo) {
        return PageRequest.of(paginationInfo.getPageOffset(),
                paginationInfo.getPageSize(),
                convertStringToSortDirection(paginationInfo.getSortDirection()),
                paginationInfo.getSortColumn());
    }

    private Sort.Direction convertStringToSortDirection(String sortDirection) {
        if (sortDirection.equals("desc")) {
            return Sort.Direction.DESC;
        } else if (sortDirection.equals("asc")) {
            return Sort.Direction.ASC;
        } else return null;
    }
}
