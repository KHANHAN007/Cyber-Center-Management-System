package service.impl;

import dao.interfaces.IFBOrderDAO;
import dao.interfaces.IOrderDetailDAO;
import enums.OrderStatus;
import exception.InvalidDataException;
import exception.NotFoundException;
import model.FBOrder;
import model.OrderDetail;
import service.interfaces.IOrderService;

import java.util.List;

public class OrderServiceImpl implements IOrderService {
    private final IFBOrderDAO fbOrderDAO;
    private final IOrderDetailDAO orderDetailDAO;

    public OrderServiceImpl(IFBOrderDAO fbOrderDAO, IOrderDetailDAO orderDetailDAO) {
        this.fbOrderDAO = fbOrderDAO;
        this.orderDetailDAO = orderDetailDAO;
    }

    @Override
    public FBOrder createOrder(int bookingId) {
        if (bookingId <= 0) {
            throw new InvalidDataException("Invalid booking ID");
        }
        FBOrder order = new FBOrder(bookingId, OrderStatus.PENDING);
        order.setTotalPrice(0);
        fbOrderDAO.create(order);
        return order;
    }

    @Override
    public FBOrder getOrderById(int orderId) {
        FBOrder order = fbOrderDAO.findById(orderId);
        if (order == null) {
            throw new NotFoundException("Order not found with ID: " + orderId);
        }
        return order;
    }

    @Override
    public void addItemToOrder(int orderId, int itemId, int sizeId, int quantity, double price) {
        if (orderId <= 0 || itemId <= 0 || quantity <= 0 || price < 0) {
            throw new InvalidDataException("Invalid order item data");
        }

        FBOrder order = fbOrderDAO.findById(orderId);
        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        OrderDetail detail = new OrderDetail();
        detail.setOrderId(orderId);
        detail.setItemId(itemId);
        detail.setSizeId(sizeId);
        detail.setQuantity(quantity);
        detail.setPrice(price);

        orderDetailDAO.create(detail);
        updateOrderTotalPrice(orderId);
    }

    @Override
    public List<OrderDetail> getOrderDetails(int orderId) {
        FBOrder order = fbOrderDAO.findById(orderId);
        if (order == null) {
            throw new NotFoundException("Order not found");
        }
        return orderDetailDAO.findByOrderId(orderId);
    }

    @Override
    public double calculateOrderTotal(int orderId) {
        List<OrderDetail> details = orderDetailDAO.findByOrderId(orderId);
        double total = 0;
        for (OrderDetail detail : details) {
            total += (detail.getPrice() * detail.getQuantity());
        }
        return total;
    }

    @Override
    public void updateOrderStatus(int orderId, String status) {
        FBOrder order = fbOrderDAO.findById(orderId);
        if (order == null) {
            throw new NotFoundException("Order not found");
        }
        try {
            OrderStatus.fromValue(status);
            fbOrderDAO.updateStatus(orderId, status);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Invalid order status: " + status);
        }
    }

    private void updateOrderTotalPrice(int orderId) {
        double totalPrice = calculateOrderTotal(orderId);
        FBOrder order = fbOrderDAO.findById(orderId);
        if (order != null) {
            order.setTotalPrice(totalPrice);
        }
    }
}
