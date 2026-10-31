package com.hammatov.MyUiRestDbService.controller;

import com.hammatov.MyUiRestDbService.entity.AcademicGroup;
import com.hammatov.MyUiRestDbService.repository.AcademicGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class AcademicGroupController {

    private final AcademicGroupRepository academicGroupRepository;

    @Autowired
    public AcademicGroupController(AcademicGroupRepository academicGroupRepository) {
        this.academicGroupRepository = academicGroupRepository;
    }

    // Страница со списком учебных групп: templates/list-groups.html
    @GetMapping({"/groups", "/listGroups"})
    public ModelAndView getAllGroups() {
        log.info("GET /groups: открыта страница со списком учебных групп");
        List<AcademicGroup> groups = academicGroupRepository.findAll();
        log.info("Страница list-groups: учебных групп в базе данных: {}", groups.size());

        ModelAndView mav = new ModelAndView("list-groups");
        mav.addObject("academicGroups", groups);
        return mav;
    }

    // Форма добавления: templates/add-group-form.html с пустой группой
    @GetMapping("/addGroupForm")
    public ModelAndView addGroupForm() {
        log.info("GET /addGroupForm: открыта форма добавления новой учебной группы");
        ModelAndView mav = new ModelAndView("add-group-form");
        mav.addObject("academicGroup", new AcademicGroup());
        return mav;
    }

    // Сохраняет новую группу (id пустой) или обновляет существующую (id заполнен)
    @PostMapping("/saveGroup")
    public RedirectView saveGroup(@ModelAttribute AcademicGroup academicGroup) {
        log.info("POST /saveGroup: получена форма учебной группы: {}", academicGroup);
        boolean isNew = academicGroup.getId() == null;
        AcademicGroup saved = academicGroupRepository.save(academicGroup);
        log.info("Учебная группа {}: {}", isNew ? "добавлена" : "обновлена", saved);
        log.info("Перенаправление на /groups");
        return new RedirectView("/groups", true);
    }

    // Та же форма, но с данными группы, найденной по id
    @GetMapping("/showGroupUpdateForm")
    public ModelAndView showGroupUpdateForm(@RequestParam("id") Long id) {
        Optional<AcademicGroup> optionalGroup = academicGroupRepository.findById(id);
        if (optionalGroup.isEmpty()) {
            log.warn("GET /showGroupUpdateForm?id={}: учебная группа не найдена, возврат к списку", id);
            return new ModelAndView(new RedirectView("/groups", true));
        }
        log.info("GET /showGroupUpdateForm?id={}: открыта форма редактирования учебной группы: {}", id, optionalGroup.get());

        ModelAndView mav = new ModelAndView("add-group-form");
        mav.addObject("academicGroup", optionalGroup.get());
        return mav;
    }

    @GetMapping("/deleteGroup")
    public RedirectView deleteGroup(@RequestParam("id") Long id) {
        log.info("GET /deleteGroup?id={}: запрос на удаление учебной группы", id);
        if (academicGroupRepository.existsById(id)) {
            academicGroupRepository.deleteById(id);
            log.info("Учебная группа с id={} удалена из базы данных", id);
        } else {
            log.warn("Учебная группа с id={} не найдена, удалять нечего", id);
        }
        log.info("Перенаправление на /groups");
        return new RedirectView("/groups", true);
    }

}
