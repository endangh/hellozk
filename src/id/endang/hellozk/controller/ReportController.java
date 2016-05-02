package id.endang.hellozk.controller;

import id.endang.hellozk.model.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

@org.springframework.stereotype.Component
@Scope("Excecution")
public class ReportController extends GenericForwardComposer<Component> {

	private CustomerDao service;

	@Wire
	Button btnCari;
	@Wire
	Textbox txtCari;
	@Wire
	Iframe frame;

	public ReportController() {
		// TODO Auto-generated constructor stub
		service = new CustomerDao();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		onClickBtnCari();
	}

	private void onClickBtnCari() {
		// TODO Auto-generated method stub
		btnCari.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				loadReport();
			}
		});
	}

	protected void loadReport() {
		// TODO Auto-generated method stub
		frame.setContent(null);
		if (StringUtils.isNotBlank(txtCari.getValue())) {
			List<Customer> list = service.getAllByName(txtCari.getValue());
			if (list.isEmpty()) {
				Messagebox.show("Tidak ada data yang ditemukan", "ERROR",
						Messagebox.OK, Messagebox.ERROR);
			} else {
				try {
					String design = "/id/endang/hellozk/jasper/ReportCustomer.jasper";
					HashMap param = new HashMap();
					JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(
							list);
					showReport(design, ds, param);
				} catch (JRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			Messagebox.show("Masukkan Nama yang dicari", "ERROR",
					Messagebox.OK, Messagebox.ERROR);
		}

	}

	public void showReport(String templatePath, JRDataSource ds,
			Map<String, Object> param) throws JRException {
		JasperPrint jasperPrint = JasperFillManager.fillReport(getClass()
				.getResourceAsStream(templatePath), param, ds);
		byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
		AMedia pdfFile = new AMedia("Report", "pdf", "application/pdf", bytes);
		frame.setContent(pdfFile);
	}

}
