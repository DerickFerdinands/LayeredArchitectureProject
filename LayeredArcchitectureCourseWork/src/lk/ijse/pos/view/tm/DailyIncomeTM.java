package lk.ijse.pos.view.tm;

import java.time.LocalDate;

public class DailyIncomeTM {
    private LocalDate date;
    private double income;

    @Override
    public String toString() {
        return "DailyIncomeTM{" +
                "date=" + date +
                ", income=" + income +
                '}';
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public DailyIncomeTM() {
    }

    public DailyIncomeTM(LocalDate date, double income) {
        this.date = date;
        this.income = income;
    }
}
