package cn.keats.controller;

import cn.keats.entity.Emp;
import cn.keats.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: keats_coder
 * @Date: 2020/1/28
 * @Version 1.0
 */
@RestController
@RequestMapping("emp")
public class EmpController {
    @Autowired
    private EmpService empService;


    @GetMapping("/{id}")
    public Emp getById(@PathVariable Long id) {
        return empService.getById(id);
    }

    @GetMapping("/list/{sex}/{pageNum}/{pageSize}")
    public List<Emp> getByParam(@PathVariable Integer sex,
                                @PathVariable Integer pageNum,
                                @PathVariable Integer pageSize) {
        return empService.getListByPara(pageNum, pageSize, sex);
    }

    @GetMapping("/list/{sex}")
    public List<Emp> getByParam1(@PathVariable Integer sex,
                                 @RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 HttpServletRequest request) {
        System.out.println(request);

        return empService.getListByPara(pageNum, pageSize, sex);
    }

    @PostMapping("/new")
    public int add(@RequestBody Emp emp) {
        return empService.add(emp);
    }
}
