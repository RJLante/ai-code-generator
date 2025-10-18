package com.rd.aicodegenerator.innerservice;

import com.rd.aicodegenerator.exception.BusinessException;
import com.rd.aicodegenerator.exception.ErrorCode;
import com.rd.aicodegenerator.model.entity.User;
import com.rd.aicodegenerator.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static com.rd.aicodegenerator.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 内部使用的用户服务
 */
public interface InnerUserService {

    List<User> listByIds(Collection<? extends Serializable> ids);

    User getById(Serializable id);

    UserVO getUserVO(User user);

    // 静态方法，避免跨服务调用
    static User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }
}