package org.launchcode.controllers;

import org.launchcode.models.User;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("users", userDao.findAll());
        model.addAttribute("title", "Registered Users");

        return "user/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayUserRegForm(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute(new User());
        return "user/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processUserRegForm(Model model, @ModelAttribute @Valid User newUser,
                                     Errors errors){

        if (errors.hasErrors()){
            model.addAttribute("title", "Register");
            return "user/add";
        }

        userDao.save(newUser);
        return "redirect:";
    }
    //TODO login/logged in feature
    //TODO add user detail view. maybe add a bio section?
    //TODO allow user to change username and password, from user detail view

}
