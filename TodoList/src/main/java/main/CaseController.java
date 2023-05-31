package main;

import main.model.Case;
import main.model.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CaseController {

    @Autowired
    private CaseRepository caseRepository;

    @GetMapping("/case/")
    public List<Case> getAllCases(){
        Iterable<Case> caseIterable = caseRepository.findAll();
        List<Case> caseList = new ArrayList<>();
        for(Case case_ : caseIterable) {
            caseList.add(case_);
        }
        return caseList;
    }

    @PostMapping("/case/")
    public synchronized int createCase(Case case_) {
        int id = caseRepository.save(case_).getId();
        return id;
    }

    @PutMapping("/case/{id}")
    public synchronized ResponseEntity updateCase(@PathVariable int id, Case newCase_){
        if(caseRepository.findById(id).isPresent()){
            Case case_ = caseRepository.findById(id).get();
            case_.setName(newCase_.getName());
            caseRepository.save(case_);
            return new ResponseEntity(case_, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("/case/completed/{id}")
    public synchronized ResponseEntity updateCompletedCase(@PathVariable int id){
        if(caseRepository.findById(id).isPresent()){
            Case case_ = caseRepository.findById(id).get();
            case_.inverse();
            caseRepository.save(case_);
            return new ResponseEntity(case_, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/case/{id}")
    public synchronized ResponseEntity deleteCase(@PathVariable int id){
        if(caseRepository.findById(id).isPresent()){
            caseRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
