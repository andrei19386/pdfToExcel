package main;

import main.model.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.model.Case;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public synchronized int createCase(String caseString) {
        Case case_ = new Case(caseString);
        int id = caseRepository.save(case_).getId();
        return id;
    }

//    @DeleteMapping("/cases/")
//    public void deleteAllCases(){
//        caseRepository.deleteAll();
//    }
//
//    @PutMapping("/cases/")
//    public void updateAllCases(List<Case> cases){
//        for(Case case_ : cases) {
//           if(caseRepository.findById(case_.getId()).isPresent()){
//               caseRepository.save(case_);
//           }
//        }
//    }

//    @GetMapping("/cases/{id}")
//    public ResponseEntity getCase(@PathVariable int id){
//        Optional<Case> caseOptional = caseRepository.findById(id);
//        if(caseOptional.isPresent()){
//            Case case_ = caseOptional.get();
//            return new ResponseEntity(case_, HttpStatus.OK);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }

    @PutMapping("/case/{id}")
    public synchronized ResponseEntity updateCase(@PathVariable int id, String caseString){
        if(caseRepository.findById(id).isPresent()){
            Case case_ = caseRepository.findById(id).get();
            case_.setName(caseString);
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
