package id.endang.hellozk.controller;


import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class Koneksi {

	private Connection koneksi;

	public Koneksi() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			koneksi = (Connection) DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/hellozk", "root", "");
			System.out.println("konek");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static Koneksi getInstance() {
        return KoneksiHolder.INSTANCE;
    }

    private static class KoneksiHolder {

        private static final Koneksi INSTANCE = new Koneksi();
    }

    public Connection getConnect() {
        return koneksi;
    }
    
    public static void main(String[] args) {
		new Koneksi();
	}


}
