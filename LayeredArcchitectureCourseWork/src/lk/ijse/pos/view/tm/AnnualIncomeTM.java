package lk.ijse.pos.view.tm;

public class AnnualIncomeTM {
    private String year;
    private double income;

    public AnnualIncomeTM() {
    }

    public AnnualIncomeTM(String year, double income) {
        this.year = year;
        this.income = income;
    }

    @Override
    public String toString() {
        return "AnnualIncomeTM{" +
                "year='" + year + '\'' +
                ", income=" + income +
                '}';
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
