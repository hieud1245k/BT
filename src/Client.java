import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame {
	private static final long serialVersionUID = 6521173496304218189L;
	private static Socket socket; // khai bao đối tượng lớp socket
	private static PrintWriter gui; // khai báo đối tượng gởi đi
	private static String noidung = "", chuoi = "";
	private static JScrollPane kq;
	private static JTextArea txt;
	private static JTextField t;
	JLabel lb1, lb2;
	JButton bn;
	JPanel pn1, pn2;
	JTextField field;

	public void GUI() {
//		lb1 = new JLabel("Client");
		field = new JTextField("Client");
		lb2 = new JLabel("Message");
		bn = new JButton("Send");
		txt = new JTextArea(50,80);
		kq = new JScrollPane(txt);
		t = new JTextField(20);
		kq = new JScrollPane(txt); // đưa nội dung vào thanh cuộn nếu nội dung lớn
		pn1 = new JPanel(new FlowLayout());
		pn2 = new JPanel(new BorderLayout());

		pn1.add(lb2);
		pn1.add(t);
		pn1.add(bn);
		pn2.add(field, BorderLayout.NORTH);
		pn2.add(kq);
		pn2.add(pn1, BorderLayout.SOUTH);
		add(pn2);

		bn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(bn)) {
					try {
						gui = new PrintWriter(socket.getOutputStream(), true); // tạo 1 đối tượng gửi đi
						noidung += field.getText() + ": " + t.getText() + "\n"; // lấy nội dung
						gui.println(field.getText() + ": "+ t.getText());
//						gui.println("exit");
						txt.setText(noidung);
						t.setText("");
						t.requestFocus();
						txt.setVisible(false);
						txt.setVisible(true);

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setVisible(true);
	}

	public Client(String st) {
		super(st);
		GUI();
	}

	public static void main(String[] args) {
		try {
			socket = new Socket("192.168.1.5", 9090);
			new Client("client");
			BufferedReader nhan = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// goi yeu cau di
			while ((chuoi = nhan.readLine()) != null) {
				noidung += chuoi + "\n";
				txt.setText(noidung);
				txt.setVisible(false);
				txt.setVisible(true);
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
