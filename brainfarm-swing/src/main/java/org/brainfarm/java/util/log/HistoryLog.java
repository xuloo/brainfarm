package org.brainfarm.java.util.log;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * Insert the type's description here. Creation date: (18/04/2002 15.48.30)
 * 
 * @author: Administrator
 */
public class HistoryLog extends JPanel {

	JFrame f1;
	JTextArea textArea1;
	JScrollPane paneScroll1;
	JLabel curr_status = new JLabel("OK");

	protected static String getTimestamp() {
		return (new SimpleDateFormat("HH:mm:ss:")).format(new Date());
	}

	public void statusMessage(String message) {
		curr_status.setText(message);
	}

	public static void main(String[] args) {

		try {

			final JFrame jf = new JFrame("Log Panel");
			jf.getContentPane().setLayout(new BorderLayout());
			HistoryLog lp = new HistoryLog();
			jf.getContentPane().add(lp, BorderLayout.CENTER);

			jf.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					jf.dispose();
					System.exit(0);
				}
			});
			jf.pack();
			jf.setVisible(true);
			lp.sendToLog("test log panel - history part");
			lp.sendToStatus("Test status part");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}

	}

	public void sendToStatus(String _msg) {
		curr_status.setText(_msg);
	}

	public HistoryLog() {
		Font Fontlab;
		Fontlab = new Font("Verdana Bold ", Font.BOLD, 12);

		Font Fontlog;
		Fontlog = new Font("Verdana ", Font.PLAIN, 12);

		curr_status.setFont(Fontlab);
		curr_status.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		textArea1 = new JTextArea(5, 80);

		textArea1.setLineWrap(true);
		textArea1.setEditable(false);
		textArea1.setOpaque(true);
		textArea1.setWrapStyleWord(true);
		// textArea1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		textArea1.setVisible(true);

		paneScroll1 = new JScrollPane(textArea1);
		paneScroll1
				.setVerticalScrollBarPolicy(paneScroll1.VERTICAL_SCROLLBAR_ALWAYS);
		// paneScroll1.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		paneScroll1.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(1, 1, 1, 1), BorderFactory
				.createEtchedBorder()));

		JPanel p1 = new JPanel();
		p1.setBorder(BorderFactory.createTitledBorder("system logger "));
		p1.setLayout(new BorderLayout());
		textArea1.setFont(Fontlog);
		textArea1.setBackground(new Color(255, 252, 242));
		// textArea1.setBackground(new Color(255, 242, 232));

		setLayout(new BorderLayout());
		p1.add(paneScroll1, BorderLayout.CENTER);
		add(p1, BorderLayout.CENTER);
		add(curr_status, BorderLayout.SOUTH);

	}

	public void sendToLog(String message) {
		String tmp = null;
		String tmp1 = null;
		String pref = null;
		int ml;
		int pos;
		try {

			textArea1.insert(getTimestamp() + " " + message + "\n", textArea1
					.getText().length());
			ml = textArea1.getText().length();

			/*if (ml > EnvConstant.MAX_BUFFER_LOGGER) {
				tmp = textArea1.getText(0, 128);
				pos = tmp.indexOf("\n", 0);
				tmp1 = textArea1.getText(pos + 1, ml - pos - 1);
				textArea1.setText(tmp1);
			}*/
			paneScroll1.repaint();
			textArea1.setCaretPosition(textArea1.getText().length());

		} catch (Exception ec) {
			System.out
					.print("\n warning : error in historical log in sendToLog :"
							+ ec);
		}

	}
}