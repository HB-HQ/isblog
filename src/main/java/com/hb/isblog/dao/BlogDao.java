package com.hb.isblog.dao;

import com.hb.isblog.entity.Blog;
import com.hb.isblog.queryvo.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 博客持久层接口
 */
@Repository
public interface BlogDao {
    List<Blog> getAllBlog();

    List<BlogQuery> getAllBlogQuery();

    int saveBlog(Blog blog);

    int updateBlog(ShowBlog showBlog);

    void deleteBlog(Long id);
    //查询编辑修改的文章
    ShowBlog getBlogById(Long id);
    //搜索博客管理列表
    List<BlogQuery> searchByTitleAndType(SearchBlog searchBlog);

//-------------------------------博客首页相关方法-------------------------------
    //查询首页最新博客列表信息
    List<FirstPageBlog> getFirstPageBlog();

    //查询首页最新推荐信息
    List<RecommendBlog> getAllRecommendBlog();

    //搜索博客列表
    List<FirstPageBlog> getSearchBlog(String query);

    //统计博客总数
    Integer getBlogTotal();

    //统计访问总数
    Integer getBlogViewTotal();

    //统计评论总数
    Integer getBlogCommentTotal();

    //统计留言总数
    Integer getBlogMessageTotal();
//-----------------------博客详情-----------------------
    //查询博客详情
    DetailedBlog getDetailedBlog(Long id);

    //文章访问更新
    int updateViews(Long id);

    //根据博客id查询出评论数量
    int getCommentCountById(Long id);

    //根据TypeId查询博客列表，显示在分类页面
    List<FirstPageBlog> getByTypeId(Long typeId);
}
