package Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CaseStrorage {

    private static Map<Integer,Case> caseStorage = new ConcurrentHashMap<>();
    private static int id = 1;

    public static List<Case> getCases(){
        List<Case> caseList = new ArrayList<>();
        for(Map.Entry<Integer,Case> entry : caseStorage.entrySet()) {
            caseList.add(entry.getValue());
        }

        return caseList;
    }

    public static Case getCase(int index){
        if( caseStorage.containsKey(index) ) {
            return caseStorage.get(index);
        }
        return null;
    }

    public static int createCase(Case case_) {
        caseStorage.put(id, case_);
        case_.setId(id);
        return id++;
    }

    public static boolean deleteCase(int index){
        if( caseStorage.containsKey(index) ) {
            caseStorage.remove(index);
            return true;
        }
        return false;
    }

    public static void deleteAll(){
       caseStorage.clear();
       id = 1;
    }

    public static boolean updateCase(int index, Case case_){
        if(caseStorage.containsKey(index)){
            caseStorage.put(index,case_);
            return true;
        }
        return false;
    }

    public static void updateAllCases(List<Case> caseList){
        if (caseStorage.size() == caseList.size()) {
            for(Case case_ : caseList){
                caseStorage.replace(case_.getId(),case_);
            }
        }
    }

}
