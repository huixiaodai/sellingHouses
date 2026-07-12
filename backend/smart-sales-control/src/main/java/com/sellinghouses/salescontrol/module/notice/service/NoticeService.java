package com.sellinghouses.salescontrol.module.notice.service;

import com.sellinghouses.salescontrol.common.dto.IdDTO;
import com.sellinghouses.salescontrol.common.result.PageResult;
import com.sellinghouses.salescontrol.module.notice.dto.NoticeCreateDTO;
import com.sellinghouses.salescontrol.module.notice.dto.NoticePageQueryDTO;
import com.sellinghouses.salescontrol.module.notice.vo.NoticeVO;

public interface NoticeService {

    /**
     * 发布公告。
     *
     * @param createDTO 公告发布请求
     * @return 公告ID
     */
    Long create(NoticeCreateDTO createDTO);

    /**
     * 删除公告。
     *
     * @param idDTO 公告ID请求
     */
    void delete(IdDTO idDTO);

    /**
     * 查询当前用户可见公告详情。
     *
     * @param idDTO 公告ID请求
     * @return 公告详情
     */
    NoticeVO detail(IdDTO idDTO);

    /**
     * 分页查询当前用户可见公告。
     *
     * @param queryDTO 分页查询请求
     * @return 公告分页
     */
    PageResult<NoticeVO> page(NoticePageQueryDTO queryDTO);
}
