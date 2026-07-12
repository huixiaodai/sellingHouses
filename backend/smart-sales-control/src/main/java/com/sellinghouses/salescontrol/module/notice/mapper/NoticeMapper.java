package com.sellinghouses.salescontrol.module.notice.mapper;

import com.sellinghouses.salescontrol.module.notice.entity.Notice;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NoticeMapper {

    int insert(Notice notice);

    int deleteById(@Param("id") Long id);

    Notice selectById(@Param("id") Long id);

    Notice selectVisibleById(@Param("id") Long id, @Param("roleCode") String roleCode);

    List<Notice> selectVisiblePage(@Param("roleCode") String roleCode);
}
