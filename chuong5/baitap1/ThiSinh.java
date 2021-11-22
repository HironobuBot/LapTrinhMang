package chuong5.baitap1;

import java.io.Serializable;

public class ThiSinh implements Serializable{
	private static final long serialVersionUID = 1L;
	String fullname;
	String sbd;
	double[] diem = new double[6];
	public ThiSinh() {
		
	}
	public ThiSinh(String fullname, String sbd) {
		this.fullname = fullname;
		this.sbd = sbd;
	}
	public String getFullname() {
		return this.fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getSbd() {
		return this.sbd;
	}
	public void setSbd(String sbd) {
		this.sbd = sbd;
	}
	public double[] getDiem() {
		return this.diem;
	}
	public void setDiem(double[] diem) {
		this.diem = diem;
	}
	public String getMsg() {
		return "Ho Ten: " 		+ this.fullname + "\n"
			 + "So Bao Danh: " 	+ this.sbd;
	}
	@Override
	public String toString() {
		return "Ho Ten: " 		+ this.fullname + "\n"
			 + "So Bao Danh: " 	+ this.sbd 		+ "\n"
			 + "Diem: " 						+ "\n"
			 + "\tToan: " 		+ this.diem[0] 	+ "\n"
			 + "\tVan: " 		+ this.diem[1] 	+ "\n"
			 + "\tAnh: " 		+ this.diem[2] 	+ "\n"
			 + "\tLy: " 		+ this.diem[3] 	+ "\n"
			 + "\tHoa: " 		+ this.diem[4] 	+ "\n"
			 + "\tSinh: "	 	+ this.diem[5];
	}
}
