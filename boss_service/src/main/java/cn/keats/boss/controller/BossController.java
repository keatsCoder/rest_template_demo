package cn.keats.boss.controller;

import cn.keats.boss.entity.Emp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: keats_coder
 * @Date: 2020/1/28
 * @Version 1.0
 */
@RestController
@RequestMapping("boss")
public class BossController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 模拟无参 GET 请求，并携带响应数据
     */
    @GetMapping("employ/{id}")
    public ResponseEntity<Emp> employById(@PathVariable Long id) {
        ResponseEntity<Emp> emp = null;
        // 返回携带Http响应信息的对象
        emp = restTemplate.getForEntity("http://127.0.0.1:9001/emp/" + id, Emp.class);
        // 响应码
        HttpStatus statusCode = emp.getStatusCode();
        // 响应头
        HttpHeaders headers = emp.getHeaders();
        // 响应体
        Emp body = emp.getBody();
        return emp;
    }

    /**
     * 模拟无参 GET 请求，只返回被调用接口返回的数据
     */
    @GetMapping("employ1/{id}")
    public Emp employById1(@PathVariable Long id) {
        // 直接返回对象
        return restTemplate.getForObject("http://127.0.0.1:9001/emp/" + id, Emp.class);
    }

    /**
     * 模拟RestFul 风格带参 GET 请求
     */
    @GetMapping("employList/{sex}")
    public List employById1(@PathVariable Integer sex,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        // 直接返回对象
        List emp = null;
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("pageNum", pageNum);
        paraMap.put("pageSize", pageSize);
        paraMap.put("sex", sex);
        // 通过map的形式，spring会将url对应的参数key替换成value
//        emp = restTemplate.getForObject("http://127.0.0.1:9001/emp/list/{sex}/{pageNum}/{pageSize}", List.class, paraMap);
        // 通过可变参的形式，spring会按顺序替换url中的参数
        emp = restTemplate.getForObject("http://127.0.0.1:9001/emp/list/{sex}/{pageNum}/{pageSize}", List.class, pageNum, pageSize, sex);
        return emp;
    }

    /**
     * 模拟表单格式GET 请求
     */
    @GetMapping("employList1/{sex}")
    public List employList1(@PathVariable Integer sex,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize,
                            HttpServletRequest request) {
        // 直接返回对象
        List emp = null;
        // 请求参数
        Map<String, Object> map= new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        map.put("sex", sex);

        emp = restTemplate.getForObject("http://127.0.0.1:9001/emp/list/{sex}" + "?pageNum={pageNum}&pageSize={pageSize}", List.class, map);
        return emp;
    }

    @RequestMapping("new")
    public int addEmploy(@RequestBody Emp emp) {
        return restTemplate.postForObject("http://127.0.0.1:9001/emp/new", emp, Integer.class);
    }
}
