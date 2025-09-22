package demo.domain.dal.mapper.original;

import demo.domain.model.entity.SysMenuResource;
import demo.domain.model.entity.SysMenuResourceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysMenuResourceMapper {
    long countByExample(SysMenuResourceExample example);

    int deleteByExample(SysMenuResourceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysMenuResource row);

    int insertSelective(SysMenuResource row);

    List<SysMenuResource> selectByExample(SysMenuResourceExample example);

    SysMenuResource selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") SysMenuResource row, @Param("example") SysMenuResourceExample example);

    int updateByExample(@Param("row") SysMenuResource row, @Param("example") SysMenuResourceExample example);

    int updateByPrimaryKeySelective(SysMenuResource row);

    int updateByPrimaryKey(SysMenuResource row);
}