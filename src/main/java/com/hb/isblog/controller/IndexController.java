package com.hb.isblog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hb.isblog.entity.Comment;
import com.hb.isblog.queryvo.DetailedBlog;
import com.hb.isblog.queryvo.FirstPageBlog;
import com.hb.isblog.queryvo.RecommendBlog;
import com.hb.isblog.service.BlogService;
import com.hb.isblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;

    /**
     * 跳转到首页
     * @param model
     * @param pageNum
     * @return
     */
    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        //1.设置分页
        PageHelper.startPage(pageNum,5);
        //2.查询首页最新博客列表信息
        List<FirstPageBlog> firstPageBlogs = blogService.getAllFirstPageBlog();
        //3.查询所有最新推荐
        List<RecommendBlog> recommendBlogs = blogService.getRecommendedBlog();
        //4.将博客list传入pageInfo对象
        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(firstPageBlogs);
        //5.将pageInfo对象以及最新推荐list传入model对象
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("recommendBlogs",recommendBlogs);
        //6.页面跳转
        return "index";
    }

    /**
     * 根据前端搜索条件查询博客并跳转到搜索结果页面
     * @param model
     * @param pageNum
     * @param query
     * @return
     */
    @PostMapping("/search")
    public String search(Model model,@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
                         @RequestParam String query){
        //1.分页设置
        PageHelper.startPage(pageNum,1000);
        //2.根据搜索条件查询所有博客
        List<FirstPageBlog> searchBlog = blogService.getSearchBlog(query);
        //3.将博客list传入pageInfo对象
        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(searchBlog);
        //4.将pageInfo对象传入model对象
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("query",query);
        //5.页面跳转
        return "search";
    }

    //博客信息统计
    @GetMapping("/footer/blogmessage")
    public String blogMessage(Model model){
        int blogTotal = blogService.getBlogTotal();
        int blogViewTotal = blogService.getBlogViewTotal();
        int blogCommentTotal = blogService.getBlogCommentTotal();
        int blogMessageTotal = blogService.getBlogMessageTotal();

        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("blogViewTotal",blogViewTotal);
        model.addAttribute("blogCommentTotal",blogCommentTotal);
        model.addAttribute("blogMessageTotal",blogMessageTotal);
        return "index :: blogMessage";
    }
    //跳转博客详情页面
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        DetailedBlog detailedBlog = blogService.getDetailedBlog(id);
        List<Comment> comments = commentService.listCommentByBlogId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("blog", detailedBlog);
        return "blog";
    }
}
