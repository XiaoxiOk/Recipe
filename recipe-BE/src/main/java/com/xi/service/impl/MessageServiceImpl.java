package com.xi.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.MessageTypeEnum;
import com.xi.mapper.MessageMapper;
import com.xi.model.pojo.Message;
import com.xi.model.vo.MessageVO;
import com.xi.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;


    /**
     * 新增消息信息
     * @param message 消息对象
     * @return 消息对象/null
     */
    @Override
    public Message insertMessage(Message message){
        message.setIsRead(0);//新增默认为0表示未读过
        int count = messageMapper.insert(message);
        if(count == 1){
            return message; //插入成功后用户id会自动填充!
        }else{
            return null;
        }
    }

    /**
     * 获取用户关注消息
     * @param userId 获取该消息列表的用户ID
     * @return 消息列表，含是否关注信息isMutual
     */
    @Override
    public IPage<Map<String, Object>> getFollowMessage(Page<?> page, int userId){
        return messageMapper.getFollowMsgList(page, MessageTypeEnum.FOLLOW_YOU.type, userId);
    }

    /**
     * 获取用户点赞消息
     * @param page 分页对象
     * @param userId 接收消息用户ID
     * @return 消息列表
     */
    @Override
    public IPage<Map<String, Object>> getLikeMessage(Page<?> page, int userId){
       // 四种点赞情况查询还分页：
        // 设置数据库content为json类型，Java用String来接收
        // 在添加消息的地方自定义content内容

        List<Integer> msgLikeTypeList = new ArrayList<>();
        msgLikeTypeList.add(MessageTypeEnum.LIKE_RECIPE.type);
        msgLikeTypeList.add(MessageTypeEnum.LIKE_USER_SHARE.type);
        msgLikeTypeList.add(MessageTypeEnum.LIKE_COMMENT.type);
        msgLikeTypeList.add(MessageTypeEnum.LIKE_REPLY.type);
        return messageMapper.getLikeOrCommentMsgList(page, msgLikeTypeList, userId);

    }

    /**
     * 获取用户评论/回复消息
     * @param page 分页对象
     * @param userId 接收消息用户ID
     * @return 消息列表
     */
    @Override
    public IPage<Map<String, Object>> getCommentMessage(Page<?> page, int userId){

        List<Integer> msgTypeList = new ArrayList<>();
        msgTypeList.add(MessageTypeEnum.COMMENT_RECIPE.type);
        msgTypeList.add(MessageTypeEnum.COMMENT_USER_SHARE.type);
        msgTypeList.add(MessageTypeEnum.REPLY_YOU.type);
        return messageMapper.getLikeOrCommentMsgList(page, msgTypeList, userId);

    }

    /**
     * 根据ID删除消息
     * @param msgId 消息ID
     * @return 影响条数
     */
    @Override
    public int deleteMsgById(int msgId){
        return messageMapper.deleteById(msgId);
    }

}
