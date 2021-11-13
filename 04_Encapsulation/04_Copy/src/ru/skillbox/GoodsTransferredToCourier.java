package ru.skillbox;

public class GoodsTransferredToCourier {
    private final Dimensions dimensions;
    private final double weight;
    private final String deliveryAddress;
    private final boolean canBeFlipped;
    private final String registrationNumber;
    private final boolean isFragile;

    public GoodsTransferredToCourier setDimensions(Dimensions dimensions) {
        return new GoodsTransferredToCourier(dimensions,
                weight, deliveryAddress, canBeFlipped, registrationNumber, isFragile);
    }

    public GoodsTransferredToCourier setWeight(double weight) {
        return new GoodsTransferredToCourier(dimensions,
                weight, deliveryAddress, canBeFlipped, registrationNumber, isFragile);
    }

    public GoodsTransferredToCourier setDeliveryAddress(String deliveryAddress) {
        return new GoodsTransferredToCourier(dimensions,
                weight, deliveryAddress, canBeFlipped, registrationNumber, isFragile);
    }


    public GoodsTransferredToCourier(Dimensions dimensions,
                                     double weight,
                                     String deliveryAddress,
                                     boolean canBeFlipped,
                                     String registrationNumber,
                                     boolean isFragile) {
        this.dimensions = dimensions;
        this.weight = weight;
        this.deliveryAddress = deliveryAddress;
        this.canBeFlipped = canBeFlipped;
        this.registrationNumber = registrationNumber;
        this.isFragile = isFragile;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public double getWeight() {
        return weight;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public boolean getCanBeFlipped() {
        return canBeFlipped;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public boolean getIsFragile() {
        return isFragile;
    }
}
