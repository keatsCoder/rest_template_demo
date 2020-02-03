package cn.keats.service;

import cn.keats.entity.Emp;

import java.util.List;

/**
 * @Author: keats_coder
 * @Date: 2020/1/28
 * @Version 1.0
 */
public interface EmpService {
    Emp getById(Long id);

    List<Emp> getListByPara(int pageNum, int pageSize, int sex);

    int add(Emp emp);

    int update(Emp emp);

    int delById(Long id);
}
