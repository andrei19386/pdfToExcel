package main;

import Storage.Case;
import Storage.CaseStrorage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class CaseController {

    @GetMapping("/cases/")
    public List<Case> getAllCases(){
        return CaseStrorage.getCases();
    }

    @PostMapping("/cases/")
    public synchronized int createCase(Case case_) {
        return CaseStrorage.createCase(case_);
    }

    @DeleteMapping("/cases/")
    public void deleteAllCases(){
        CaseStrorage.deleteAll();
    }

    @PutMapping("/cases/")
    public void updateAllCases(List<Case> cases){
        CaseStrorage.updateAllCases(cases);
    }

    @GetMapping("/cases/{id}")
    public ResponseEntity getCase(@PathVariable int id){
        Case case_ = CaseStrorage.getCase(id);
        if(case_ == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(case_, HttpStatus.OK);
    }

    @PutMapping("/cases/{id}")
    public synchronized ResponseEntity updateCase(@PathVariable int id, Case case_){
        if(CaseStrorage.updateCase(id,case_)){
            return new ResponseEntity(case_, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/cases/{id}")
    public synchronized ResponseEntity deleteCase(@PathVariable int id){
        if(CaseStrorage.deleteCase(id)){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
