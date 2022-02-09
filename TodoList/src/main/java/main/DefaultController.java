package main;

import main.model.Case;
import main.model.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

    @Autowired
    CaseRepository caseRepository;

    @RequestMapping("/")
    public String index(Model model){
        Iterable<Case> caseIterable = caseRepository.findAll();
        List<Case> caseList = new ArrayList<>();
        for(Case case_: caseIterable) {
            caseList.add(case_);
        }
        model.addAttribute("cases", caseList);
        model.addAttribute("casesCount",caseList.size());

        return "index";
    }

}
