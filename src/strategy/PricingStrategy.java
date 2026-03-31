package strategy;

public interface PricingStrategy {
    double calculatePrice(double hours, double pricePerHour);
}
