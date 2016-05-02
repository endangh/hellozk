package id.endang.hellozk.controller;

import id.endang.hellozk.model.Customer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

@org.springframework.stereotype.Component
@Scope("desktop")
public class CustomerController extends GenericForwardComposer<Component> {

	// @Autowired
	private CustomerDao service;

	@Wire
	private Listbox tblData;
	@Wire
	Textbox txtAccNbr;
	@Wire
	Textbox txtAccName;
	@Wire
	Textbox txtAddress;
	@Wire
	Textbox txtEmail;
	@Wire
	Textbox txtPhone;
	@Wire
	Datebox txtBirthday;
	@Wire
	Button btnSave;
	@Wire
	Button btnReset;
	@Wire
	Button btnDelete;
	@Wire
	Button btnReport;
	@Wire
	Button btnCari;
	private Customer customer;

	private List<Customer> listCustomer;
	private boolean delete;
	private boolean isUpdate = false;

	private EventQueue eq;
	public CustomerController() {
		// TODO Auto-generated constructor stub
		service = new CustomerDao();
	}

	private void onSelectListBox() {
		// TODO Auto-generated method stub
		tblData.addEventListener(Events.ON_SELECT, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				loadData();
			}
		});
	}

	private void loadData() {
		if (tblData.getSelectedIndex() >= 0) {
			Customer c = getRowData(tblData, tblData.getSelectedIndex());
			txtAccNbr.setText(c.getAccNbr());
			txtAccName.setText(c.getAccName());
			txtBirthday.setText(c.getBirthday());
			txtAddress.setText(c.getAddress());
			txtEmail.setText(c.getEmail());
			txtPhone.setText(c.getPhone());
			txtAccNbr.setDisabled(true);
			isUpdate = true;
			delete = true;
		}

	}

	private Customer getRowData(Listbox listbox, int index) {
		Object value = listbox.getItems().get(index).getValue();
		if (value != null) {
			if (value instanceof Customer) {
				return (Customer) value;
			}
		} else {
			throw new RuntimeException("item at index " + index
					+ " doesn't have data");
		}
		return null;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		// this.self.setAttribute("controller", this, false);
		// System.out.println("------------------" + service);
		doLoadData();
		onSelectListBox();
		onClickBtnDelete();
		onClickBtnReset();
		onClickBtnSave();
		onClickBtnReport();
		onClickBtnCari();
		onOKNama();
		
		getValueFromModal();
	}

	private void getValueFromModal() {
		eq = EventQueues.lookup("CSTMR",EventQueues.DESKTOP,true);
		eq.subscribe(new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				isUpdate = true;
				Customer c = (Customer) event.getData();
				setCustomer(c);
			}
		});
	}

	private void onOKNama() {
		// TODO Auto-generated method stub
		txtAccName.addEventListener(Events.ON_OK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Messagebox.show(txtAccName.getValue());
			}
		});

	}

	@Listen(Events.ON_OK + " = #txtAccNbr")
	private void onOKRek() {
		Messagebox.show(txtAccNbr.getValue());
	}

	private void onClickBtnCari() {
		// TODO Auto-generated method stub
		btnCari.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Window w = (Window) Executions.createComponents(
						"/content/modalCustomer.zul", self,
						Collections.singletonMap("PARAM", listCustomer));
				w.doModal();
				w.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						// TODO Auto-generated method stub
						customer = (Customer) session.getAttribute("customer");
						setCustomer(customer);
					}
				});
			
				
			}
		});
	}

	private void onClickBtnReport() {
		// TODO Auto-generated method stub
		btnReport.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Executions.createComponents("report.zul", null,
						new HashMap<String, String>());
			}

		});

	}

	@Listen(Events.ON_CLICK + " = #btnRport")
	private void onClickBtnReport2() {

		Executions.createComponents("report.zul", null,
				new HashMap<String, String>());

	}

	private void onClickBtnSave() {
		// TODO Auto-generated method stub
		btnSave.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				doSave();
			}
		});
	}

	private void doSave() {
		// TODO Auto-generated method stub
		if (doValidate()) {
			final Customer c = new Customer(txtAccNbr.getValue(),
					txtAccName.getValue(),
					new SimpleDateFormat("yyyy-MM-dd").format(txtBirthday
							.getValue()), txtAddress.getValue(),
					txtEmail.getValue(), txtPhone.getValue());
			Messagebox.show("Simpan Data?", "Confirm Dialog", Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION,
					new EventListener<Event>() {
						public void onEvent(Event evt) throws Exception {
							if (evt.getName().equals("onOK")) {
								if (!isUpdate) {
									if (service.insert(c)) {
										alert("Data Tersimpan !");
										doReset();
									}
								} else if (isUpdate) {
									if (service.update(c)) {
										alert("Data Terubah !");
										doReset();
									}
								}
							}
						}
					});
		}
	}

	private Boolean doValidate() {
		if (StringUtils.isBlank(txtAccNbr.getValue())) {
			Messagebox.show("No Rekening Harus Diisi", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}
		if (!(txtAccNbr.getValue()).matches("[0-9]*")) {
			Messagebox.show("No Rekening Hanya angka", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}
		if (StringUtils.isBlank(txtAccName.getValue())) {
			Messagebox.show("Nama Harus Diisi", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}
		if (txtBirthday.getValue() == null) {
			Messagebox.show("Tgl Lahir Harus Diisi", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}
		if (txtBirthday.getValue().after(new Date())
				|| txtBirthday.getValue().equals(new Date())) {
			Messagebox.show("Tgl Lahir salah", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}
		if (StringUtils.isBlank(txtAddress.getValue())) {
			Messagebox.show("Alamat Harus Diisi", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}
		if (StringUtils.isBlank(txtEmail.getValue())) {
			Messagebox.show("Email Harus Diisi", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}
		if (!txtEmail
				.getValue()
				.matches(
						"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			Messagebox.show("Email Salah", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}

		if (StringUtils.isBlank(txtPhone.getValue())) {
			Messagebox.show("No Telepon Harus Diisi", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}
		if (!(txtPhone.getValue()).matches("[0-9]*")) {
			Messagebox.show("No Telepon Hanya angka", "ERROR", Messagebox.OK,
					Messagebox.ERROR);
			return false;
		}

		return true;
	}

	private void onClickBtnReset() {
		// TODO Auto-generated method stub
		btnReset.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				doReset();
			}

		});
	}

	public void doReset() {
		// TODO Auto-generated method stub
		txtAccNbr.setText("");
		txtAccName.setText("");
		txtAddress.setText("");
		txtBirthday.setText(null);
		txtEmail.setText("");
		txtPhone.setText("");

		delete = false;
		isUpdate = false;
		doLoadData();
		txtAccNbr.setDisabled(false);
	}

	private void onClickBtnDelete() {
		// TODO Auto-generated method stub
		btnDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				if (delete == false) {
					Messagebox.show("Pilih Data yang akan dihapus", "ERROR",
							Messagebox.OK, Messagebox.ERROR);
				} else {
					Messagebox.show("Yakin Hapus?", "Confirm Dialog",
							Messagebox.OK | Messagebox.CANCEL,
							Messagebox.QUESTION, new EventListener<Event>() {
								public void onEvent(Event evt) throws Exception {
									if (evt.getName().equals("onOK")) {
										service.delete(txtAccNbr.getValue());
										alert("Data Terhapus !");
										doReset();
									}
								}
							});
				}

			}

		});

	}

	
	private void doLoadData() {
		listCustomer = service.getAll();//load data
		int i = 1;
		tblData.getItems().clear();
		for (Customer c : listCustomer) {
			//int bunga = c.getsaldo() * %bunga.
			//update saldo where c.getAccNbr();
			
			final Listitem ti = tblData.appendItem(i + "", "");//kolom1
			
			Listcell cs = new Listcell();
			Button btn = new Button("Detil");//create button
			//create event for button
			btn.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

				@Override
				public void onEvent(Event arg0) throws Exception {
					// TODO Auto-generated method stub
					Messagebox.show(((Customer) ti.getValue()).getAccName());
				}
			});

			cs.appendChild(btn);//insert button to cell
			
			ti.appendChild(cs);//kolom2
			ti.appendChild(new Listcell(c.getAccNbr()));//kolom3
			ti.appendChild(new Listcell(c.getAccName()));//kolom4
			ti.appendChild(new Listcell(c.getBirthday()));//kolom5
			ti.setValue(c);//set
			
			i++;
		}
	}

	private void setCustomer(Customer c) {
		txtAccNbr.setValue(c.getAccNbr());
		txtAccNbr.setDisabled(true);
		txtAccName.setValue(c.getAccName());
		try {
			txtBirthday.setValue(new SimpleDateFormat("yyyy-MM-dd").parse(c
					.getBirthday()));
		} catch (WrongValueException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txtAddress.setValue(c.getAddress());
		txtEmail.setValue(c.getEmail());
		txtPhone.setValue(c.getPhone());

	}
	
}





