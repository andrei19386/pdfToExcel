public class Main {
    public static void main(String[] args) {
        Client physicalPerson = new PhysicalPerson();
        Client legalPerson = new LegalPerson();
        Client individualBusinessMan = new IndividualBusinessman();

        System.out.println();

        physicalPerson.put(-1000);
        legalPerson.put(-1000);
        individualBusinessMan.put(-1000);

        System.out.println();

        physicalPerson.put(1000);
        legalPerson.put(1000);
        individualBusinessMan.put(1000);

        System.out.println();

        physicalPerson.take(200);
        legalPerson.take(200);
        individualBusinessMan.take(200);

        System.out.println();

        physicalPerson.take(800);
        legalPerson.take(800);
        individualBusinessMan.take(800);

        System.out.println();

        physicalPerson.take(100);
        legalPerson.take(100);
        individualBusinessMan.take(100);

        System.out.println();

        physicalPerson.put(500);
        legalPerson.put(500);
        individualBusinessMan.put(500);

    }
}
