package demo.domain.dal.mapper.original;

import demo.domain.model.entity.SysMenu;
import demo.domain.model.entity.SysMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysMenuMapper {
    long countByExample(SysMenuExample example);

    int deleteByExample(SysMenuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysMenu row);

    int insertSelective(SysMenu row);

    List<SysMenu> selectByExample(SysMenuExample example);

    SysMenu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") SysMenu row, @Param("example") SysMenuExample example);

    int updateByExample(@Param("row") SysMenu row, @Param("example") SysMenuExample example);

    int updateByPrimaryKeySelective(SysMenu row);

    int updateByPrimaryKey(SysMenu row);
}