package com.sellinghouses.salescontrol.common.result;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResult<T> {

    private final List<T> records;

    private final Integer pageNo;

    private final Integer pageSize;

    private final Long total;
}
