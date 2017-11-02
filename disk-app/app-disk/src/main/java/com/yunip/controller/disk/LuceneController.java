/*
 * 描述：〈他人分享空间〉
 * 创建人：ming.zhu
 * 创建时间：2016-5-24
 */
package com.yunip.controller.disk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.controller.base.BaseController;

@Controller
@RequestMapping("/lucene")
public class LuceneController extends BaseController{

    @RequestMapping("/index")
    public String quertyGetShare(HttpServletRequest request){
        return "lucene/index";
    }
}
