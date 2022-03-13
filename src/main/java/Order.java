import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order implements Comparable {

    private final String MANAGER;
    private String orderNumber;
    private Date date;
    private SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy");

    private int priority;//высокий приоритет, если ждет проверки поступления на склад, т.к. дата <= текущей

    public Order(String orderNumber, String manager, String date) {
        this.orderNumber = orderNumber;
        this.MANAGER = manager;
        try {
            this.date = parser.parse(date);
        } catch (ParseException e) {
            this.date = null;
        }

        this.checkDateForSetPriority();
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setDate(Date date) {
        this.date = date;
        checkDateForSetPriority();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getManager() {
        return MANAGER;
    }

    public String getDateStr() {
        return this.date != null ? parser.format(date) : "";
    }

    public Date getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public void checkDateForSetPriority() {
        this.priority = this.date == null || this.getDate().compareTo(new Date()) > 0 ? 0 : 1;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Order)) return false;
        Order order = (Order) object;
        return getOrderNumber().equals(order.getOrderNumber());
    }

    @Override
    public int hashCode() {
        return this.orderNumber.hashCode() + this.MANAGER.hashCode();
    }

    @Override
    public String toString() {
        return orderNumber + " " + MANAGER + " " + this.getDateStr();
    }

    @Override
    public int compareTo(Object object) {
        Order order = null;
        int comparator;
        if (object instanceof Order) order = (Order) object;
        if ((comparator = this.getManager().compareTo(order.getManager())) != 0) return comparator;
        if ((comparator = Integer.compare(this.getPriority(), order.getPriority())) != 0) return comparator;
        if ((comparator = this.getDate().compareTo(order.getDate())) != 0) {
            return comparator;
        }
        comparator = this.getOrderNumber().compareTo(order.getOrderNumber());
        return comparator;
    }
}