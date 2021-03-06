package com.czeta.onlinejudge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.czeta.onlinejudge.dao.entity.Tag;
import com.czeta.onlinejudge.enums.ProblemStatus;
import com.czeta.onlinejudge.enums.RoleType;
import com.czeta.onlinejudge.model.param.PageModel;
import com.czeta.onlinejudge.model.param.ProblemConditionPageModel;
import com.czeta.onlinejudge.model.param.SubmitModel;
import com.czeta.onlinejudge.model.result.DetailProblemModel;
import com.czeta.onlinejudge.model.result.PublicSimpleProblemModel;
import com.czeta.onlinejudge.service.ProblemService;
import com.czeta.onlinejudge.service.TagService;
import com.czeta.onlinejudge.utils.response.APIResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @ClassName ProblemController
 * @Description 题目控制器
 * @Author chenlongjie
 * @Date 2020/3/17 13:08
 * @Version 1.0
 */
@Slf4j
@Api(tags = "Problem Controller")
@RestController
@RequestMapping("/api/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private TagService tagService;

    @ApiOperation(value = "分页获得公共题目列表", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageModel", value = "分页请求参数，这里的paramData置为null", dataType = "PageModel", paramType = "body", required = true)
    })
    @ApiResponses({})
    @PostMapping("/problemList")
    public APIResult<IPage<PublicSimpleProblemModel>> getPublicProblemList(@RequestBody PageModel pageModel) {
        return new APIResult<>(problemService.getPublicProblemList(pageModel));
    }

    @ApiOperation(value = "获取所有题目标签", notes = "不需要token")
    @ApiImplicitParams({})
    @ApiResponses({})
    @GetMapping("/tags")
    public APIResult<List<Tag>> getTagInfoList() {
        return new APIResult<>(tagService.getTagInfoList());
    }

    @ApiOperation(value = "根据筛选条件，分页获得公共题目列表", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemConditionPageModel", value = "分页请求参数与筛选条件model", dataType = "ProblemConditionPageModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "无分页参数")
    })
    @PostMapping("/conditionalProblemList")
    public APIResult<IPage<PublicSimpleProblemModel>> getPublicProblemList(@RequestBody ProblemConditionPageModel problemConditionPageModel) {
        return new APIResult<>(problemService.getPublicProblemListByCondition(problemConditionPageModel));
    }

    @ApiOperation(value = "根据问题ID获取问题详情", notes = "不需要token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "问题ID", dataType = "Long", paramType = "path", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "题目不存在"),
            @ApiResponse(code = 2202, message = "题目状态有误"),
            @ApiResponse(code = 2200, message = "题目异常，已下线"),
            @ApiResponse(code = 2101, message = "无权查看该题：属于比赛题")
    })
    @GetMapping("/problemInfo/{problemId}")
    public APIResult<DetailProblemModel> getDetailProblemInfo(@PathVariable Long problemId) {
        return new APIResult<>(problemService.getDetailProblemInfoById(problemId, true));
    }

    @ApiOperation(value = "用户提交题目", notes = "需要token：普通用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "submitModel", value = "提交代码model", dataType = "SubmitModel", paramType = "body", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 2001, message = "题目不存在"),
            @ApiResponse(code = 2001, message = "代码语言不合法或不支持")
    })
    @RequiresRoles(RoleType.Names.COMMON_USER)
    @PostMapping("/submit")
    public APIResult submitProblem(@RequestBody SubmitModel submitModel, @ApiIgnore @RequestAttribute Long userId) {
        problemService.submitProblem(submitModel, userId);
        return new APIResult();
    }
}
