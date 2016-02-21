package pl.edu.agh.iisg.to.to2project.domain.entity.budget;

import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by root on 8/12/15.
 */
public class DataGenerator {

    private static long id = 1000;
    public static Category generateSpendings()
    {

        Category child1 = new Category("ROWER");
        child1.setId(1L);
        Category child2 = new Category("MPK");
        child2.setId(2L);
        Category child3 = new Category("TARCZE");
        child3.setId(3L);
        Category child4 = new Category("KLOCKI");
        child4.setId(4L);
        Category child5 = new Category("HAMULCE");
        child5.setId(5L);
        Category child6 = new Category("SAMOCHOD");
        child6.setId(6L);
        Category child7 = new Category("TRANSPORT");
        child7.setId(7L);
        Category root = new Category("Wydatek");
        root.setId(8L);
        child5.addSubCategory(child4);
        child5.addSubCategory(child3);
        child6.addSubCategory(child5);
        child7.addSubCategory(child6);
        child7.addSubCategory(child1);
        child7.addSubCategory(child2);
        root.addSubCategory(child7);
        return root;

    }

    public static Category generateEarnings()
    {
        Category root = new Category("Przychod");
        root.setId(9L);
        Category child1 = new Category("PENSJA");
        child1.setId(10L);
        Category child2 = new Category("PREMIA");
        child2.setId(11L);
        Category child3 = new Category("BIZNES");
        child3.setId(12L);
        root.addSubCategory(child1);
        root.addSubCategory(child2);
        root.addSubCategory(child3);
        return root;
    }

    public static double generateAvailableResources() {
        return 1000.0;
    }

    private static ExternalTransaction makeupTransaction(LocalDate date, BigDecimal delta, Category category) {
        ExternalTransaction trans = new ExternalTransaction("", null, delta, date);
        trans.setId(id++);
        trans.setCategory(category);
        return  trans;
    }

    private static Category makeupCategory(String name) {
        Category cat = new Category(name);
        cat.setId(id++);
        return  cat;
    }

    public static List<Category> generateCategoryList()
    {
        List<Category> list = new ArrayList<>();
        Category catA = makeupCategory("inA");
        Category catB = makeupCategory("inB");
        Category catC = makeupCategory("inC");
        Category catD = makeupCategory("inD");

        ExternalTransaction tranA = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(100), catA);
        ExternalTransaction tranB = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(30), catA);
        ExternalTransaction tranC = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(55), catC);
        ExternalTransaction tranD = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(10), catD);

        catB.addSubCategory(catD);
        catA.addSubCategory(catB);
        catA.addSubCategory(catC);
        list.add(catA);


        Category catE = makeupCategory("outA");
        Category catF = makeupCategory("outB");
        Category catG = makeupCategory("outC");
        Category catH = makeupCategory("outD");
        Category catI = makeupCategory("outE");

        ExternalTransaction tranE = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-60), catF);
        ExternalTransaction tranF = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-20), catF);
        ExternalTransaction tranG = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-15), catF);
        ExternalTransaction tranH = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-40), catF);
        ExternalTransaction tranI = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-100), catH);
        ExternalTransaction tranJ = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-150), catH);
        ExternalTransaction tranK = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-10), catI);

        catG.addSubCategory(catH);
        catG.addSubCategory(catI);
        catE.addSubCategory(catF);
        catE.addSubCategory(catG);
        list.add(catE);

        Category catJ = makeupCategory("bothA");
        Category catK = makeupCategory("bothB");
        Category catL = makeupCategory("bothC");

        ExternalTransaction tranM = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(30), catK);
        ExternalTransaction tranN = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(80), catK);
        ExternalTransaction tranO = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-50), catL);

        catJ.addSubCategory(catK);
        catJ.addSubCategory(catL);
        list.add(catJ);

        Category catM = makeupCategory("both2A");
        Category catN = makeupCategory("both2B");
        Category catO = makeupCategory("both2C");
        Category catP = makeupCategory("both2D");
        Category catQ = makeupCategory("both2E");

        ExternalTransaction tranP = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(10), catN);
        ExternalTransaction tranQ = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-50), catO);
        ExternalTransaction tranR = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(90), catO);
        ExternalTransaction tranT = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(-100), catP);
        ExternalTransaction tranU = makeupTransaction(new LocalDate(2016, 10, 8), new BigDecimal(40), catQ);

        catP.addSubCategory(catQ);
        catM.addSubCategory(catN);
        catM.addSubCategory(catO);
        catM.addSubCategory(catP);
        list.add(catM);

        return list;
    }


    public static Double getTransactionValueForCategory(String categoryName,int year,int month)
    {
        Random r = new Random();
        double d = r.nextDouble() * 100000.0;
        DecimalFormat df = new DecimalFormat("#.##");
        String dx=df.format(d);
        d=Double.valueOf(dx);
        return d;
    }

    public static Double getPlanValueForCategory(String categoryName,int year,int month)
    {
        Random r = new Random();
        double d = r.nextDouble() * 100000.0;
        DecimalFormat df = new DecimalFormat("#.##");
        String dx=df.format(d);
        d=Double.valueOf(dx);
        return d;
    }

}
