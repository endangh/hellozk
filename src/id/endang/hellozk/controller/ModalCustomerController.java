package id.endang.hellozk.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.endang.hellozk.model.Customer;

import org.springframework.context.annotation.Scope;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

@org.springframework.stereotype.Component
@Scope("desktop")
public class ModalCustomerController extends GenericForwardComposer<Component> {

	@Wire
	private Window mdlCustomer;
	@Wire
	private Listbox tabel;

	private Customer customer;
	private Component parent;
	private EventQueue eq;

	private List<Customer> list;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		parent = comp;
		list = (List<Customer>) Executions.getCurrent().getArg().get("PARAM");
		doLoadData();
		onSelectTabel();
	}

	private void onSelectTabel() {
		tabel.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Customer customer = tabel.getSelectedItem().getValue();
				eq = EventQueues.lookup("CSTMR", EventQueues.DESKTOP, true);
				eq.publish(new Event("onTableSelected", tabel, customer));
				mdlCustomer.detach();//menghilangkan window
			}
		});
	}

	private void doLoadData() {
		int i = 1;
		tabel.getItems().clear();
		for (Customer c : list) {
			Listitem ti = tabel.appendItem(i + "", "");
			ti.appendChild(new Listcell(c.getAccNbr()));
			ti.appendChild(new Listcell(c.getAccName()));
			ti.appendChild(new Listcell(c.getBirthday()));
			ti.setValue(c);
			i++;
		}
	}
}
