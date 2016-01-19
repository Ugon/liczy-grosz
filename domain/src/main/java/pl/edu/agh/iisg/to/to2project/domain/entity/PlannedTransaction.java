package pl.edu.agh.iisg.to.to2project.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Nesbite on 2015-12-08.
 */
public class PlannedTransaction {
    private LocalDate date;
    private BigDecimal value;
    private Category category;

    public PlannedTransaction(LocalDate date, BigDecimal val, Category cat){
        this.date = date;
        this.value = val;
        this.category = cat;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}