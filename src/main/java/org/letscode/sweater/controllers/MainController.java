package org.letscode.sweater.controllers;

import org.letscode.sweater.domain.Message;
import org.letscode.sweater.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MessageRepo messageRepo;

    @GetMapping("/")
    public String greeting( Map<String, Object> model) {
        model.put("name", "user");
        return "home";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model){
        Iterable<Message> messages = this.messageRepo.findAll();
        model.put("messages",messages);
        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String text,
                      @RequestParam String tag,
                      Map<String, Object> model){
        if(text != null && !text.isEmpty() ){
            Message mes = new Message(text,tag);
            this.messageRepo.save(mes);
        }
        Iterable<Message> messages = this.messageRepo.findAll();
        model.put("messages",messages);
        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter,
                         Map<String, Object> model){
        Iterable<Message> messages;
        if(filter != null && !filter.isEmpty()) {
             messages = this.messageRepo.findByTag(filter);
        }else{
             messages = this.messageRepo.findAll();
        }
        this.messageRepo.findByTag(filter);
        model.put("messages",messages);
        return "main";

    }

}
