package colin.julien.test.model;

public enum taxRate {
    GOODS(1.1),
    OTHER(1.0);

    public final Double taxRate;

    taxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getValue(){
        return taxRate;
    }
}
