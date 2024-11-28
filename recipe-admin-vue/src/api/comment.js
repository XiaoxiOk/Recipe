import request from '@/utils/request'

export function getCommentsByPage(page, type, typeId) {
    return request({
        url: '/comment/getCommentsByPage',
        method: 'post',
        data: page,
        params: { "type": type, "typeId": typeId }
    });
};
export function deleteComment(CommentBO) {
    return request({
        url: '/comment/deleteComment',
        method: 'post',
        data: CommentBO
    })
};

export function getRepliesByPage(page, commentId) {
    return request({
        url: '/reply/getRepliesByPage',
        method: 'post',
        params: { "commentId": commentId },
        data: page
    });
};

export function deleteReply(replyId, commentId) {
    return request({
        url: '/reply/deleteReply',
        method: 'get',
        params: { "replyId": replyId, "commentId": commentId }
    })
};