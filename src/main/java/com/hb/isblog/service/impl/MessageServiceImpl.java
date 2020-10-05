package com.hb.isblog.service.impl;

import com.hb.isblog.dao.MessageDao;
import com.hb.isblog.entity.Message;
import com.hb.isblog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;
    //存放迭代找出的所有子代的集合
    private List<Message> tempReplys = new ArrayList<>();

    @Override
    public List<Message> listMessage() {
        //查询所有父级留言，即数据表中parent_message_id为“-1”
        List<Message> messages = messageDao.findByParentIdNull(Long.parseLong("-1"));
        //循环所有父级留言
        for (Message message : messages) {
            //获取父留言的主键id
            Long id = message.getId();
            //获取父留言的昵称
            String parentNickname1 = message.getNickname();
            //根据父留言的id查询该父留言下的一级回复留言
            List<Message> childrenMessage = messageDao.findByParentIdNotNull(id);
            //查询子留言
            combineChildren(childrenMessage,parentNickname1);
            //将回复评论存入message的属性replyMessages中
            message.setReplyMessages(tempReplys);
            //清空链表，给下一个元素使用
            tempReplys = new ArrayList<>();
        }
        return messages;
    }

    /**
     * @Description: 查询出子留言
     * @Date: 17:31 2020/4/14
     * @Param: childMessages：所有子留言
     * @Param: parentNickname1：父留言的姓名
     * @Return:
     */
    private void combineChildren(List<Message> childMessages, String parentNickname1) {
        //判断是否有一级子回复
        if(childMessages.size() > 0){
            //循环找出一级子回复的id
            for(Message childMessage : childMessages){
                //获取一级子回复的昵称
                String parentNickname = childMessage.getNickname();
                //给一级子回复的父昵称填充对应父留言的数据
                childMessage.setParentNickname(parentNickname1);
                //将一级子回复存入临时留言链表
                tempReplys.add(childMessage);
                //获取一级子回复的主键id
                Long childId = childMessage.getId();
                //查询二级以及所有子集回复
                recursively(childId, parentNickname);
            }
        }
    }

    /**
     * @Description: 循环迭代找出子集回复
     * @Date: 17:33 2020/4/14
     * @Param: childId：子留言的id
     * @Param: parentNickname1：子留言的姓名
     * @Return:
     */
    private void recursively(Long childId, String parentNickname1) {
        //根据子一级回复的id找到子二级回复
        List<Message> replayMessages = messageDao.findByReplayId(childId);

        if(replayMessages.size() > 0){
            //遍历所有子二级回复
            for(Message replayMessage : replayMessages){
                //将子二级回复的昵称设置为子三级回复的父留言昵称
                String parentNickname = replayMessage.getNickname();
                replayMessage.setParentNickname(parentNickname1);
                //将其主键id设置为下一级回复的parent_message_id
                Long replayId = replayMessage.getId();
                //添加进临时链表中
                tempReplys.add(replayMessage);
                //循环迭代找出所有子集回复
                recursively(replayId,parentNickname);
            }
        }
    }
    @Override
    //存储留言信息
    public int saveMessage(Message message) {
        message.setCreateTime(new Date());
        return messageDao.saveMessage(message);
    }

    //    删除留言
    @Override
    public void deleteMessage(Long id) {
        messageDao.deleteMessage(id);
    }
}
