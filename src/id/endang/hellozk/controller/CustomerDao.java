package id.endang.hellozk.controller;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;

import id.endang.hellozk.model.Customer;

@Controller
public class CustomerDao {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Koneksi koneksi;

	public CustomerDao() {
		// TODO Auto-generated constructor stub
		koneksi = new Koneksi();
	}
	
	public static CustomerDao getInstance(){
		return new CustomerDao();
	}

	public boolean insert(Customer c) {
		boolean stat = false;
		String query = "insert into customer(accNbr,accName,birthday,address,email,phone) values(?,?,?,?,?,?)";
		try {
			PreparedStatement ps = koneksi.getConnect()
					.prepareStatement(query);
			ps.setString(1, c.getAccNbr());
			ps.setString(2, c.getAccName());
			ps.setString(3, c.getBirthday());
			ps.setString(4, c.getAddress());
			ps.setString(5, c.getEmail());
			ps.setString(6, c.getPhone());
			ps.executeUpdate();
			stat = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stat;
	}
	
	public boolean update(Customer c){
		String query = "update customer set accName=?,birthday=?,address=?,email=?,phone=? where accNbr=?";
		try {
			PreparedStatement ps = koneksi.getConnect().prepareStatement(query);
			ps.setString(1, c.getAccName());
			ps.setString(2, c.getBirthday());
			ps.setString(3, c.getAddress());
			ps.setString(4, c.getEmail());
			ps.setString(5, c.getPhone());
			ps.setString(6, c.getAccNbr());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(String accNbr) {
		String query = "delete from customer where accNbr = '" + accNbr.trim()
				+ "'";
		try {
			Koneksi.getInstance().getConnect().createStatement()
					.executeUpdate(query);
			System.out.println(query);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Customer> getAll(){
		List<Customer> list = new ArrayList<Customer>();
		String query = "select * from customer";
		try {
			ResultSet rs = koneksi.getConnect().createStatement().executeQuery(query);
			while (rs.next()) {
				list.add(new Customer(rs.getString("accNbr"), rs.getString("accName"), sdf.format(rs.getDate("birthday")), rs.getString("address"), rs.getString("email"), rs.getString("phone")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Customer> getAllByName(String name){
		List<Customer> list = new ArrayList<Customer>();
		String query = "select * from customer where accName LIKE '"+name+"%'";
		try {
			System.out.println(query);
			ResultSet rs = koneksi.getConnect().createStatement().executeQuery(query);
			
			while (rs.next()) {
				list.add(new Customer(rs.getString("accNbr"), rs.getString("accName"), sdf.format(rs.getDate("birthday")), rs.getString("address"), rs.getString("email"), rs.getString("phone")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
