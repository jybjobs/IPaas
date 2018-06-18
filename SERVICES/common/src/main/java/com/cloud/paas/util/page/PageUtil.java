package com.cloud.paas.util.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageUtil {

   public static PageInfo getPageInfo(Page page, List list){
       PageInfo pageInfo=new PageInfo(list);
       pageInfo.setTotal(page.getTotal());
       pageInfo.setPageNum(page.getPageNum());
       pageInfo.setPageSize(page.getPageSize());
       pageInfo.setStartRow(page.getStartRow());
       pageInfo.setEndRow(page.getEndRow());
       pageInfo.setPages(page.getPages());
       return  pageInfo;
    }
}
