package com.hb.isblog.controller;

import com.hb.isblog.entity.Message;
import com.hb.isblog.entity.User;
import com.hb.isblog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Description: 留言页面控制器
 *
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Value("${message.avatar}")
    private String avatar;

    //跳转到留言板页面
    @GetMapping("/message")
    public String message() {
        System.out.println("执行了。。。。。。");
        return "message";
    }

    //查询留言
    @GetMapping("/messagecomment")
    public String messages(Model model) {
        System.out.println("执行了。。。。。。");
        List<Message> messages = messageService.listMessage();
        model.addAttribute("messages", messages);
        return "message::messageList";
    }
    //新增留言
    @PostMapping("/message")
    public String post(Message message, HttpSession session, Model model){
        //获取用户信息
        User user = (User) session.getAttribute("user");
        //如果用户已经登录则该留言头像设置为站长头像并设置站长标识，否则设置访客默认头像
        if(user != null){
            message.setAvatar(user.getAvatar());
            message.setAdminMessage(true);
        }else {
            message.setAvatar(avatar);
        }
        //如果该条留言有父级留言，则给当前留言的parentMessageId赋值
        if (message.getParentMessage().getId() != null){
            message.setParentMessageId(message.getParentMessage().getId());
        }
        //保存留言
        messageService.saveMessage(message);
        List<Message> messages = messageService.listMessage();
        //返回数据给前端
        model.addAttribute("messages",messages);
        return "message::messageList";
    }

    //删除留言
    @GetMapping("/messages/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes, Model model){
        messageService.deleteMessage(id);
        return "redirect:/message";
    }
}
