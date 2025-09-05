package com.rd.aicodegenerator.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.rd.aicodegenerator.model.dto.app.AppQueryRequest;
import com.rd.aicodegenerator.model.entity.App;
import com.rd.aicodegenerator.model.entity.User;
import com.rd.aicodegenerator.model.vo.AppVO;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author <a href="https://github.com/RJLante">RJLante</a>
 */
public interface AppService extends IService<App> {

    /**
     * 获取应用封装类
     * @param app
     * @return
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用封装类列表
     * @param appList
     * @return
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 获取查询条件
     * @param appQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

    /**
     * 通过对话生成应用代码0
     * @param appId
     * @param message
     * @param loginUser
     * @return
     */
    Flux<String> ChatToGenCode(Long appId, String message, User loginUser);


    /**
     * 部署应用
     * @param appId
     * @param loginUser
     * @return
     */
    String deployApp(Long appId, User loginUser);
}
