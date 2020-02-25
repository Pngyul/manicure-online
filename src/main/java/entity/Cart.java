package entity;

import java.io.Serializable;

import com.manicure.entity.TbOrderItem;

public class Cart implements Serializable{

	//是否被选择
	private Boolean seleted;
	
	private TbOrderItem orderItem;

	public Boolean getSeleted() {
		return seleted;
	}

	public void setSeleted(Boolean seleted) {
		this.seleted = seleted;
	}

	public TbOrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(TbOrderItem orderItem) {
		this.orderItem = orderItem;
	}

	@Override
	public String toString() {
		return "Cart [seleted=" + seleted + ", orderItem=" + orderItem + "]";
	}
	
}
