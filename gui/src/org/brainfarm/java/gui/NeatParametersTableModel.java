package org.brainfarm.java.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.params.AbstractNeatParameter;

public class NeatParametersTableModel extends AbstractTableModel {
	
	private static Logger logger = Logger.getLogger(NeatParametersTableModel.class);

	public List<AbstractNeatParameter> data = new ArrayList<AbstractNeatParameter>();
	public int rows;

	public static final String columnNames[] = { " Parameter ", " Value ", " Description " };

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		
		switch (col) {
			case 0:
				return data.get(row).getName();
			case 1:
				return data.get(row).getVal();
			case 2:
				return data.get(row).getDescription();
		}
		
		return null;
	}

	public boolean isCellEditable(int row, int col) {
		if (col == 1) {
			return true;
		}
		return false;

	}
	
	public void setValueAt(Object value, int row, int col) {

		super.setValueAt(value, row, col);
		
		if (col == 1) {
			data.get(row).setVal(String.valueOf(value));
		}
		
		fireTableCellUpdated(row, col);
	}
	
	public void setData(List<AbstractNeatParameter> data) {
		this.data = data;
		
		fireTableDataChanged();
	}

}