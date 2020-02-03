package cn.keats.service.impl;

import com.github.pagehelper.PageHelper;
import cn.keats.entity.Emp;
import cn.keats.mapper.EmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.keats.service.EmpService;

import java.util.List;

/**
 * @Author: keats_coder
 * @Date: 2020/1/28
 * @Version 1.0
 */
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public Emp getById(Long id) {
        return empMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Emp> getListByPara(int pageNum, int pageSize, int sex) {
        PageHelper.startPage(pageNum, pageSize);
        List<Emp> resultList = empMapper.getListByPara(sex);
        return resultList;
    }

    @Override
    public int add(Emp emp) {
        return empMapper.insert(emp);
    }

    @Override
    public int update(Emp emp) {
        return empMapper.updateByPrimaryKeySelective(emp);
    }

    @Override
    public int delById(Long id) {
        return empMapper.deleteByPrimaryKey(id);
    }
}
