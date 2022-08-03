package hello.proxy.app.v1;

public class OrderServiceImpl implements OrderServiceV1{

    private final OrderRepositoryV1 orderRepository;

    public OrderServiceImpl(OrderRepositoryV1 orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
