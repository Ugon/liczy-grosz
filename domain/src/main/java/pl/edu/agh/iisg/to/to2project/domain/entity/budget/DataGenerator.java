package pl.edu.agh.iisg.to.to2project.domain.entity.budget;

import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.joda.time.LocalDate;

import java.util.*;

/**
 * Created by root on 8/12/15.
 */
public class DataGenerator {

    private static long id = 1000;

    private Map<String, Double[]> planMap = new HashMap<>();
    private List<Category> categoryList;

    public DataGenerator() {
        planMap = new HashMap<>();
        categoryList = new ArrayList<>();
    }

    public Map<String, Double[]> getPlanMap() {
        return planMap;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public static double generateAvailableResources() {
        return 1000.0;
    }


    private Account makeupAccount(String name, BigDecimal balance) {
        Account account = new Account(name, balance);
        account.setId(id++);
        return  account;
    }

    private ExternalTransaction makeupTransaction(Account account, LocalDate date, BigDecimal delta, Category category) {
        ExternalTransaction trans = new ExternalTransaction("", account, delta, date);
        trans.setId(id++);
        trans.setCategory(category);
        return  trans;
    }

    private Category makeupCategory(String name) {
        Category cat = new Category(name);
        cat.setId(id++);
        return  cat;
    }

    public void generateOneItemBothPresent(int year, int month)
    {
        String nameA = "catA";

        Account account = makeupAccount("TestAccount", BigDecimal.ZERO);

        Category catA = makeupCategory(nameA);

        ExternalTransaction tranA = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(100), catA);
        ExternalTransaction tranB = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(30), catA);
        ExternalTransaction tranC = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-70), catA);

        categoryList.clear();
        categoryList.add(catA);

        planMap.clear();
        planMap.put(nameA, new Double[]{100.0, 40.0});
    }

    public void generateOneItemBothPast(int year, int month)
    {
        String nameA = "catA";

        Account account = makeupAccount("TestAccount", BigDecimal.ZERO);

        Category catA = makeupCategory(nameA);

        ExternalTransaction tranA = makeupTransaction(account, new LocalDate(year - 2 , month, 8), new BigDecimal(100), catA);
        ExternalTransaction tranB = makeupTransaction(account, new LocalDate(year - 3, month, 8), new BigDecimal(-30), catA);

        categoryList.clear();
        categoryList.add(catA);

        planMap.clear();
        planMap.put(nameA, new Double[]{null, null});
    }

    public void generateOneItemOnePastTrans(int year, int month)
    {
        String nameA = "catA";

        Account account = makeupAccount("TestAccount", BigDecimal.ZERO);

        Category catA = makeupCategory(nameA);

        ExternalTransaction tranA = makeupTransaction(account, new LocalDate(year - 2 , month, 8), new BigDecimal(100), catA);
        ExternalTransaction tranB = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(-90), catA);

        categoryList.clear();
        categoryList.add(catA);

        planMap.clear();
        planMap.put(nameA, new Double[]{null, null});
    }

    public void generateOneItemOnePastPlan(int year, int month)
    {
        String nameA = "catA";

        Account account = makeupAccount("TestAccount", BigDecimal.ZERO);

        Category catA = makeupCategory(nameA);

        ExternalTransaction tranA = makeupTransaction(account, new LocalDate(year - 2 , month, 8), new BigDecimal(100), catA);
        ExternalTransaction tranB = makeupTransaction(account, new LocalDate(year - 4 , month, 8), new BigDecimal(90), catA);

        categoryList.clear();
        categoryList.add(catA);

        planMap.clear();
        planMap.put(nameA, new Double[]{20.0, null});
    }

    public void generateTwoItemsEqualLevel(int year, int month)
    {
        String nameA = "catA";
        String nameB = "catB";

        Account account = makeupAccount("TestAccount", BigDecimal.ZERO);

        Category catA = makeupCategory(nameA);
        Category catB = makeupCategory(nameB);

        ExternalTransaction tranA = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(-100), catA);
        ExternalTransaction tranB = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(10), catA);
        ExternalTransaction tranC = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(-100), catA);
        ExternalTransaction tranD = makeupTransaction(account, new LocalDate(year + 1 , month, 8), new BigDecimal(-3000), catB);
        ExternalTransaction tranE = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(250), catB);
        ExternalTransaction tranF = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(1000), catB);
        ExternalTransaction tranG = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(-100), catB);

        categoryList.clear();
        categoryList.add(catA);
        categoryList.add(catB);

        planMap.clear();
        planMap.put(nameA, new Double[]{7000.0, null});
        planMap.put(nameB, new Double[]{null, 200.0});
    }

    public void generateTwoItemsParentAndChild(int year, int month)
    {
        String nameA = "catA";
        String nameB = "catB";

        Account account = makeupAccount("TestAccount", BigDecimal.ZERO);

        Category catA = makeupCategory(nameA);
        Category catB = makeupCategory(nameB);

        catA.addSubCategory(catB);

        ExternalTransaction tranA = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(-100), catA);
        ExternalTransaction tranB = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(10), catA);
        ExternalTransaction tranC = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(-100), catA);
        ExternalTransaction tranD = makeupTransaction(account, new LocalDate(year + 1 , month, 8), new BigDecimal(-3000), catB);
        ExternalTransaction tranE = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(250), catB);
        ExternalTransaction tranF = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(1000), catB);
        ExternalTransaction tranG = makeupTransaction(account, new LocalDate(year , month, 8), new BigDecimal(-100), catB);

        categoryList.clear();
        categoryList.add(catA);
        categoryList.add(catB);

        planMap.clear();
        planMap.put(nameA, new Double[]{7000.0, null});
        planMap.put(nameB, new Double[]{null, 200.0});
    }

    public void generateWholeTree(int year, int month)
    {
        String nameA = "inA";
        String nameB = "inB";
        String nameC = "inC";
        String nameD = "inD";
        String nameE = "outA";
        String nameF = "outB";
        String nameG = "outC";
        String nameH = "outD";
        String nameI = "outE";
        String nameJ = "bothA";
        String nameK = "bothB";
        String nameL = "bothC";
        String nameM = "both2A";
        String nameN = "both2B";
        String nameO = "both2C";
        String nameP = "both2D";
        String nameQ = "both2E";

        Account account = makeupAccount("TestAccount", BigDecimal.ZERO);
        categoryList.clear();

        Category catA = makeupCategory(nameA);
        Category catB = makeupCategory(nameB);
        Category catC = makeupCategory(nameC);
        Category catD = makeupCategory(nameD);

        ExternalTransaction tranA = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(100), catA);
        ExternalTransaction tranB = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(30), catA);
        ExternalTransaction tranC = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(55), catC);
        ExternalTransaction tranD = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(10), catD);

        catB.addSubCategory(catD);
        catA.addSubCategory(catB);
        catA.addSubCategory(catC);
        categoryList.add(catA);
        categoryList.add(catB);
        categoryList.add(catC);


        Category catE = makeupCategory(nameE);
        Category catF = makeupCategory(nameF);
        Category catG = makeupCategory(nameG);
        Category catH = makeupCategory(nameH);
        Category catI = makeupCategory(nameI);

        ExternalTransaction tranE = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-60), catF);
        ExternalTransaction tranF = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-20), catF);
        ExternalTransaction tranG = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-15), catF);
        ExternalTransaction tranH = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-40), catF);
        ExternalTransaction tranI = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-100), catH);
        ExternalTransaction tranJ = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-150), catH);
        ExternalTransaction tranK = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-10), catI);

        catG.addSubCategory(catH);
        catG.addSubCategory(catI);
        catE.addSubCategory(catF);
        catE.addSubCategory(catG);
        categoryList.add(catE);
        categoryList.add(catF);
        categoryList.add(catG);
        categoryList.add(catH);
        categoryList.add(catI);

        Category catJ = makeupCategory(nameJ);
        Category catK = makeupCategory(nameK);
        Category catL = makeupCategory(nameL);

        ExternalTransaction tranM = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(30), catK);
        ExternalTransaction tranN = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(80), catK);
        ExternalTransaction tranO = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-50), catL);

        catJ.addSubCategory(catK);
        catJ.addSubCategory(catL);
        categoryList.add(catJ);
        categoryList.add(catK);
        categoryList.add(catL);

        Category catM = makeupCategory(nameM);
        Category catN = makeupCategory(nameN);
        Category catO = makeupCategory(nameO);
        Category catP = makeupCategory(nameP);
        Category catQ = makeupCategory(nameQ);

        ExternalTransaction tranP = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(10), catN);
        ExternalTransaction tranQ = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-50), catO);
        ExternalTransaction tranR = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(90), catO);
        ExternalTransaction tranT = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(-100), catP);
        ExternalTransaction tranU = makeupTransaction(account, new LocalDate(year, month, 8), new BigDecimal(40), catQ);

        catP.addSubCategory(catQ);
        catM.addSubCategory(catN);
        catM.addSubCategory(catO);
        catM.addSubCategory(catP);
        categoryList.add(catM);
        categoryList.add(catN);
        categoryList.add(catO);
        categoryList.add(catP);
        categoryList.add(catQ);

        planMap.clear();
        planMap.put(nameA, new Double[]{null, null});
        planMap.put(nameB, new Double[]{null, null});
        planMap.put(nameC, new Double[]{null, null});
        planMap.put(nameD, new Double[]{null, null});
        planMap.put(nameE, new Double[]{null, null});
        planMap.put(nameF, new Double[]{null, null});
        planMap.put(nameG, new Double[]{null, null});
        planMap.put(nameH, new Double[]{null, null});
        planMap.put(nameI, new Double[]{null, null});
        planMap.put(nameJ, new Double[]{null, null});
        planMap.put(nameK, new Double[]{null, null});
        planMap.put(nameL, new Double[]{null, null});
        planMap.put(nameM, new Double[]{null, null});
        planMap.put(nameN, new Double[]{null, null});
        planMap.put(nameO, new Double[]{null, null});
        planMap.put(nameP, new Double[]{null, null});
        planMap.put(nameQ, new Double[]{null, null});
    }

    public void generateWholeTree_Past()
    {
        String nameA = "inA";
        String nameB = "inB";
        String nameC = "inC";
        String nameD = "inD";
        String nameE = "outA";
        String nameF = "outB";
        String nameG = "outC";
        String nameH = "outD";
        String nameI = "outE";
        String nameJ = "bothA";
        String nameK = "bothB";
        String nameL = "bothC";
        String nameM = "both2A";
        String nameN = "both2B";
        String nameO = "both2C";
        String nameP = "both2D";
        String nameQ = "both2E";

        Account account = makeupAccount("TestAccount", BigDecimal.ZERO);
        categoryList.clear();

        Category catA = makeupCategory(nameA);
        Category catB = makeupCategory(nameB);
        Category catC = makeupCategory(nameC);
        Category catD = makeupCategory(nameD);

        ExternalTransaction tranA = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(100), catA);
        ExternalTransaction tranB = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(30), catA);
        ExternalTransaction tranC = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(55), catC);
        ExternalTransaction tranD = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(10), catD);

        catB.addSubCategory(catD);
        catA.addSubCategory(catB);
        catA.addSubCategory(catC);
        categoryList.add(catA);
        categoryList.add(catB);
        categoryList.add(catC);


        Category catE = makeupCategory(nameE);
        Category catF = makeupCategory(nameF);
        Category catG = makeupCategory(nameG);
        Category catH = makeupCategory(nameH);
        Category catI = makeupCategory(nameI);

        ExternalTransaction tranE = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-60), catF);
        ExternalTransaction tranF = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-20), catF);
        ExternalTransaction tranG = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-15), catF);
        ExternalTransaction tranH = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-40), catF);
        ExternalTransaction tranI = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-100), catH);
        ExternalTransaction tranJ = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-150), catH);
        ExternalTransaction tranK = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-10), catI);

        catG.addSubCategory(catH);
        catG.addSubCategory(catI);
        catE.addSubCategory(catF);
        catE.addSubCategory(catG);
        categoryList.add(catE);
        categoryList.add(catF);
        categoryList.add(catG);
        categoryList.add(catH);
        categoryList.add(catI);

        Category catJ = makeupCategory(nameJ);
        Category catK = makeupCategory(nameK);
        Category catL = makeupCategory(nameL);

        ExternalTransaction tranM = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(30), catK);
        ExternalTransaction tranN = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(80), catK);
        ExternalTransaction tranO = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-50), catL);

        catJ.addSubCategory(catK);
        catJ.addSubCategory(catL);
        categoryList.add(catJ);
        categoryList.add(catK);
        categoryList.add(catL);

        Category catM = makeupCategory(nameM);
        Category catN = makeupCategory(nameN);
        Category catO = makeupCategory(nameO);
        Category catP = makeupCategory(nameP);
        Category catQ = makeupCategory(nameQ);

        ExternalTransaction tranP = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(10), catN);
        ExternalTransaction tranQ = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-50), catO);
        ExternalTransaction tranR = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(90), catO);
        ExternalTransaction tranT = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(-100), catP);
        ExternalTransaction tranU = makeupTransaction(account, new LocalDate(2016, 10, 8), new BigDecimal(40), catQ);

        catP.addSubCategory(catQ);
        catM.addSubCategory(catN);
        catM.addSubCategory(catO);
        catM.addSubCategory(catP);
        categoryList.add(catM);
        categoryList.add(catN);
        categoryList.add(catO);
        categoryList.add(catP);
        categoryList.add(catQ);

        planMap.clear();
        planMap.put(nameA, new Double[]{null, null});
        planMap.put(nameB, new Double[]{null, null});
        planMap.put(nameC, new Double[]{null, null});
        planMap.put(nameD, new Double[]{null, null});
        planMap.put(nameE, new Double[]{null, null});
        planMap.put(nameF, new Double[]{null, null});
        planMap.put(nameG, new Double[]{null, null});
        planMap.put(nameH, new Double[]{null, null});
        planMap.put(nameI, new Double[]{null, null});
        planMap.put(nameJ, new Double[]{null, null});
        planMap.put(nameK, new Double[]{null, null});
        planMap.put(nameL, new Double[]{null, null});
        planMap.put(nameM, new Double[]{null, null});
        planMap.put(nameN, new Double[]{null, null});
        planMap.put(nameO, new Double[]{null, null});
        planMap.put(nameP, new Double[]{null, null});
        planMap.put(nameQ, new Double[]{null, null});
    }

}
