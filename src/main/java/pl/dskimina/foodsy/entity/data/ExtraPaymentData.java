package pl.dskimina.foodsy.entity.data;

public class ExtraPaymentData {

    private String extraPaymentId;
    private String extraPaymentProduct;
    private double price;
    private OrderData order;

    public String getExtraPaymentId() {
        return extraPaymentId;
    }

    public void setExtraPaymentId(String extraPaymentId) {
        this.extraPaymentId = extraPaymentId;
    }

    public String getExtraPaymentProduct() {
        return extraPaymentProduct;
    }

    public void setExtraPaymentProduct(String extraPaymentProduct) {
        this.extraPaymentProduct = extraPaymentProduct;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOder(OrderData order){
        this.order = order;
    }
    public OrderData getOrder(){
        return order;
    }


}
