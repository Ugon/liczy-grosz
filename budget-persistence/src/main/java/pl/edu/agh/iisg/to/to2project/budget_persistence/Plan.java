package pl.edu.agh.iisg.to.to2project.budget_persistence;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by root on 19/01/16.
 */
public class Plan {
    private String categoryName;
    private BigDecimal earningPlanValue;
    private BigDecimal spendingPlanValue;
    private LocalDate date;

    public Plan(String categoryName,int year, int month, double earningPlanValue, double spendingPlanValue) {
        this.categoryName = categoryName;
        this.date = LocalDate.of(year,month,1);
        this.earningPlanValue = BigDecimal.valueOf(earningPlanValue);
        this.spendingPlanValue = BigDecimal.valueOf(spendingPlanValue);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getEarningPlanValue() {
        return earningPlanValue;
    }

    public void setEarningPlanValue(BigDecimal earningPlanValue) {
        this.earningPlanValue = earningPlanValue;
    }

    public BigDecimal getSpendingPlanValue() {
        return spendingPlanValue;
    }

    public void setSpendingPlanValue(BigDecimal spendingPlanValue) {
        this.spendingPlanValue = spendingPlanValue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}

