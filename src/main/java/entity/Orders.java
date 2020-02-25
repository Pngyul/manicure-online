package entity;

import java.util.List;

import com.manicure.entity.TbOrder;
import com.manicure.entity.TbOrderItem;

public class Orders {

	private TbOrder order;
	
	private List<TbOrderItem> orderItemList;

	public TbOrder getOrder() {
		return order;
	}

	public void setOrder(TbOrder order) {
		this.order = order;
	}

	public List<TbOrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<TbOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

	@Override
	public String toString() {
		return "Orders [order=" + order + ", orderItemList=" + orderItemList + "]";
	}

	public Orders() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Orders(TbOrder order, List<TbOrderItem> orderItemList) {
		super();
		this.order = order;
		this.orderItemList = orderItemList;
	}
	
	
}
