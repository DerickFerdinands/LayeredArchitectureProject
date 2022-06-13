package lk.ijse.pos.view.tm;

public class MonthlyIncomeTM {
    private String year;
    private String month;
    private double income;

    public MonthlyIncomeTM() {
    }

    public MonthlyIncomeTM(String year, String month, double income) {
        this.year = year;
        this.month = month;
        this.income = income;
    }

    @Override
    public String toString() {
        return "MonthlyIncomeTM{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", income=" + income +
                '}';
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
