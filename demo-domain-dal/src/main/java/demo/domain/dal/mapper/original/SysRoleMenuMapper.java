package demo.domain.dal.mapper.original;

import demo.domain.model.entity.SysRoleMenu;
import demo.domain.model.entity.SysRoleMenuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysRoleMenuMapper {
    long countByExample(SysRoleMenuExample example);

    int deleteByExample(SysRoleMenuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleMenu row);

    int insertSelective(SysRoleMenu row);

    List<SysRoleMenu> selectByExample(SysRoleMenuExample example);

    SysRoleMenu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") SysRoleMenu row, @Param("example") SysRoleMenuExample example);

    int updateByExample(@Param("row") SysRoleMenu row, @Param("example") SysRoleMenuExample example);

    int updateByPrimaryKeySelective(SysRoleMenu row);

    int updateByPrimaryKey(SysRoleMenu row);
}