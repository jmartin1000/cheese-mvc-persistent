package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value="")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model){
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu,
                      Errors errors){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu);

        return "redirect:view/" + menu.getId();
    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId){
        model.addAttribute("title", menuDao.findOne(menuId).getName());
        model.addAttribute("menu", menuDao.findOne(menuId));
        return "menu/view";
    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId){

        Menu menu = menuDao.findOne(menuId);

        AddMenuItemForm form = new AddMenuItemForm(
                cheeseDao.findAll(), menu);

        model.addAttribute("title", "Add cheese to menu:" + menu.getName());
        model.addAttribute("form", form);

        return "menu/add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add cheese to menu:" + form.getMenu().getName());
            model.addAttribute("form", form);
            return "menu/add-item";
        }

        Cheese cheese = cheeseDao.findOne(form.getCheeseId());
        Menu menu = menuDao.findOne(form.getMenuId());
        menu.addItem(cheese);
        menuDao.save(menu);

        return "redirect:/menu/view/" + menu.getId();
    }

    @RequestMapping(value = "remove-item/{menuId}", method = RequestMethod.GET)
    public String displayRemoveItemForm(Model model, @PathVariable int menuId) {
        model.addAttribute("menu", menuDao.findOne(menuId));
        model.addAttribute("cheeses", menuDao.findOne(menuId).getCheeses());
        model.addAttribute("title", "Remove Cheese from " + menuDao.findOne(menuId).getName());
        return "menu/remove-item";
    }

    @RequestMapping(value = "remove-item/{menuId}", method = RequestMethod.POST)
    public String removeItem(@RequestParam int[] cheeseIds, @PathVariable int menuId) {

        Menu menu = menuDao.findOne(menuId);

        if (cheeseIds.length > 0) {
            for (int cheeseId : cheeseIds) {
                Cheese cheese = cheeseDao.findOne(cheeseId);
                menu.removeItem(cheese);
            }

            menuDao.save(menu);
        }

        return "redirect:/menu/view/" + menuId;
    }

    @RequestMapping(value = "remove-menu", method = RequestMethod.GET)
    public String displayRemoveMenuForm(Model model) {
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Edit Menu List");
        return "menu/remove-menu";
    }

    @RequestMapping(value = "remove-menu", method = RequestMethod.POST)
    public String removeMenu(@RequestParam int[] menuIds) {

        for (int menuId : menuIds) {
            menuDao.delete(menuId);
        }

        return "redirect:";
    }

    @RequestMapping(value = "edit/{menuId}", method = RequestMethod.GET)
    public String displayMenuEditForm(Model model, @PathVariable int menuId){
        model.addAttribute("title", menuDao.findOne(menuId).getName());
        model.addAttribute("menu", menuDao.findOne(menuId));
        return "menu/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String editMenu(Model model, @RequestParam int menuId, @ModelAttribute @Valid Menu menu, Errors errors){

        if (errors.hasErrors()){
            model.addAttribute("menuId", menuId);
            return "menu/edit";
        }

        Menu currMenu = menuDao.findOne(menuId);
        currMenu.setName(menu.getName());
        menuDao.save(currMenu);

        return "redirect:/menu/view/" + menuId;
    }
}
