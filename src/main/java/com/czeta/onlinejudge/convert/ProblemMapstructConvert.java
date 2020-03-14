package com.czeta.onlinejudge.convert;

import com.czeta.onlinejudge.dao.entity.Problem;
import com.czeta.onlinejudge.dao.entity.ProblemJudgeType;
import com.czeta.onlinejudge.model.param.MachineProblemModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @InterfaceName ProblemInfoMapstructConvert
 * @Description
 * @Author chenlongjie
 * @Date 2020/3/12 20:12
 * @Version 1.0
 */
@Mapper
public interface ProblemMapstructConvert {
    ProblemMapstructConvert INSTANCE = Mappers.getMapper(ProblemMapstructConvert.class);

    @Mapping(source = "language", target = "language", ignore = true)
    Problem machineProblemToProblem(MachineProblemModel machineProblemModel);

    ProblemJudgeType machineProblemToProblemJudgeType(MachineProblemModel machineProblemModel);
}
