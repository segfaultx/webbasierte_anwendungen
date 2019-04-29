package web.blatt2.web2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rechner")
public class Rechner{


    @GetMapping("/add")
    @ResponseBody
    public String add(@RequestParam("x") int x, @RequestParam("y") int y){
        return  String.format("%d + %d ergibt %d", x,y,x+y);
    }
    @GetMapping("/mult")
    @ResponseBody
    public String mult(@RequestParam("x")int x, @RequestParam("y")int y){
            return String.format("%d * %d ergibt %d", x,y,x*y);
    }
}

