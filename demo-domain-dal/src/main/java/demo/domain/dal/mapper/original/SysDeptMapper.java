package demo.domain.dal.mapper.original;

import demo.domain.model.entity.SysDept;
import demo.domain.model.entity.SysDeptExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysDeptMapper {
    long countByExample(SysDeptExample example);

    int deleteByExample(SysDeptExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysDept row);

    int insertSelective(SysDept row);

    List<SysDept> selectByExample(SysDeptExample example);

    SysDept selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") SysDept row, @Param("example") SysDeptExample example);

    int updateByExample(@Param("row") SysDept row, @Param("example") SysDeptExample example);

    int updateByPrimaryKeySelective(SysDept row);

    int updateByPrimaryKey(SysDept row);
}