package in.gov.irms.train.dto;

import java.util.List;

public record PagedResponseDTO<T>(
        long pageNumber,
        long pageSize,
        long totalPages,
        long totalRecords,
        List<T> data
) {}
