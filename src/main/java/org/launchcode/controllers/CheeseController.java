package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private MenuDao menuDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/add";
        }

        // these lines were used when we passed in categoryId from the form
        // i changed the select tab to a field of the object
        // Category cat = categoryDao.findOne(categoryId);
        //newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "edit/{cheeseId}", method = RequestMethod.GET)
    public String displayCheeseEditForm(Model model, @PathVariable int cheeseId){
        model.addAttribute("title", "Edit "+ cheeseDao.findOne(cheeseId).getName());
        model.addAttribute("cheese", cheeseDao.findOne(cheeseId));
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editCheese(Model model, @RequestParam int cheeseId, @ModelAttribute @Valid Cheese cheese, Errors errors){

        if (errors.hasErrors()){
            model.addAttribute("categories", categoryDao.findAll());
            model.addAttribute("cheeseId", cheeseId);
            return "cheese/edit";
        }

        Cheese currCheese = cheeseDao.findOne(cheeseId);
        currCheese.setName(cheese.getName());
        currCheese.setDescription(cheese.getDescription());
        currCheese.setCategory(cheese.getCategory());
        cheeseDao.save(currCheese);

        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {


        if (cheeseIds != null) {
            for (int cheeseId : cheeseIds) {
                List<Menu> currMenus = cheeseDao.findOne(cheeseId).getMenus();
                if (currMenus.size() > 0){
                    for (Menu menu : currMenus) {
                        menu.removeItem(cheeseDao.findOne(cheeseId));
                    }
                }
                cheeseDao.delete(cheeseId);
            }
        }

        return "redirect:";
    }

    @RequestMapping(value = "category/{id}", method = RequestMethod.GET)
    public String category(Model model, @PathVariable int id) {
        Category cat = categoryDao.findOne(id);
        List<Cheese> cheeses = cat.getCheeses();
        model.addAttribute("cheeses", cheeses);
        model.addAttribute("title", "Cheeses in Category: " + cat.getName());
        model.addAttribute("categoryId", cat.getId());
        return "cheese/index";
    }

    //TODO add userid to the add cheese handlers
    //TODO add userid checks to the edit and remove handlers and views (hide edit and remove options owner not logged in)
    // TODO add user name to the views, link to user detail

}
