package com.miniBili.web.controller;

import com.miniBili.entity.po.UserAction;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/userAction")
@Validated
public class UserActionController extends ABaseController{

    @Autowired
    private UserActionService userActionService;

    @RequestMapping("/doAction")
    public ResponseVO doAction(@NotEmpty String videoId,
                               @NotNull Integer actionType,
                               Integer actionCount,
                               Integer commentId){
        UserAction userAction = new UserAction();
        userAction.setVideoId(videoId);
        userAction.setUserId(getTokenInfoDto().getUserId());
        userAction.setActionType(actionType);
        actionCount = actionCount==null?1:actionCount;
        commentId = commentId==null?0:commentId;
        userAction.setActionCount(actionCount);
        userAction.setCommentId(commentId);
        userActionService.saveAction(userAction);
        return getSuccessResponseVO(null);
    }
}
