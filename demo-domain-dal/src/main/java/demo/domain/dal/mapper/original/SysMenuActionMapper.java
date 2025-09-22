package demo.domain.dal.mapper.original;

import demo.domain.model.entity.SysMenuAction;
import demo.domain.model.entity.SysMenuActionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysMenuActionMapper {
    long countByExample(SysMenuActionExample example);

    int deleteByExample(SysMenuActionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysMenuAction row);

    int insertSelective(SysMenuAction row);

    List<SysMenuAction> selectByExample(SysMenuActionExample example);

    SysMenuAction selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") SysMenuAction row, @Param("example") SysMenuActionExample example);

    int updateByExample(@Param("row") SysMenuAction row, @Param("example") SysMenuActionExample example);

    int updateByPrimaryKeySelective(SysMenuAction row);

    int updateByPrimaryKey(SysMenuAction row);
}