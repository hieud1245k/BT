import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame {
	private static Socket socket; // khai bao đối tượng lớp socket
	private static PrintWriter gui; // khai báo đối tượng gởi đi
	private static String noidung = "", chuoi = "";
	private static JScrollPane kq;
	private static JTextArea txt;
	private static JTextField t;
	JLabel lb1, lb2;
	JButton bn;
	JPanel pn1, pn2;

	public void GUI() {
		lb1 = new JLabel("Server");
		lb2 = new JLabel("Message");

		bn = new JButton("Send");

		txt = new JTextArea(50, 80);
		kq = new JScrollPane(txt);
		t = new JTextField(20);
		kq = new JScrollPane(txt); // đưa nội dung vào thanh cuộn nếu nội dung lớn
		pn1 = new JPanel(new FlowLayout());
		pn2 = new JPanel(new BorderLayout());

		pn1.add(lb2);
		pn1.add(t);
		pn1.add(bn);
		pn2.add(lb1, BorderLayout.NORTH);
		pn2.add(kq);
		pn2.add(pn1, BorderLayout.SOUTH);
		add(pn2);

		bn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(bn)) {
					try {
						gui = new PrintWriter(socket.getOutputStream(), true); // tạo 1 đối tượng gửi đi
						noidung += lb1.getText() + ": " + t.getText() + "\n"; // lấy nội dung
						gui.println(lb1.getText() + ": " + t.getText());
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

	public Server(String st) {
		super(st);
		GUI();
	}

	public class ThreadServer extends Thread {

		Socket socket = null;

		public ThreadServer(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				DataInputStream din = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				while (true) {
					String st = din.readUTF();
					if (st.equals("exit"))
						break;
					System.out.println(st);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9090);
			Server server = new Server("Server");
			while(true) {
				Socket socket = serverSocket.accept();
				new MultiThread(socket).start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				serverSocket.close();
			} catch (IOException e) {
				System.out.println(e);
			}

		}
	}

	
	static class MultiThread extends Thread {
		Socket socket;

		public MultiThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			PrintWriter gui = null;
			try {
				BufferedReader nhan = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String chuoi = "";
				while((chuoi = nhan.readLine())!=null) {
					if(chuoi.contains("exit")) {
						System.exit(0);
						break;
					}
					noidung += chuoi + "\n";
					txt.setText(noidung);
					txt.setVisible(false);
					txt.setVisible(true);
					gui = new PrintWriter(socket.getOutputStream(),true);
					gui.println("ket qua: "+ Tinh(chuoi.trim()));
				}

			} catch (Exception e) {
				System.out.println(e);
			}
		}
		public double Tinh(String st) {
			System.out.println(st);
			try {
				String st1[] = st.split(" ");
				System.out.println(st1.length);
				// String st1 ="";
				double kq = 0, a, b;
				for (int i = 0; i < st1.length; i++) {
					switch (st1[i]) {
					case "+": {
						a = Double.parseDouble(st1[i - 1]);
						b = Double.parseDouble(st1[i + 1]);
						System.out.println(a + " " + b);
						kq = a + b;
					}
						break;
					case "-": {
						a = Double.parseDouble(st1[i - 1]);
						b = Double.parseDouble(st1[i + 1]);
						kq = a - b;
					}
						break;
					case "*": {
						a = Double.parseDouble(st1[i - 1]);
						b = Double.parseDouble(st1[i + 1]);
						kq = a * b;
					}
						break;
					case "/": {
						a = Double.parseDouble(st1[i - 1]);
						b = Double.parseDouble(st1[i + 1]);
						kq = a / b;
					}
						break;
					}

				}
				return kq;
			} catch (Exception e) {
				System.out.println(e);
				return 0;
			}
		}
	}

}
