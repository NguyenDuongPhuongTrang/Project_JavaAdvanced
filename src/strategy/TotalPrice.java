package strategy;

public class TotalPrice implements PricingStrategy {
    @Override
    public double calculatePrice(double hours, double pricePerHour) {
        return hours * pricePerHour;
    }
}
