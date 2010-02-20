package org.brainfarm.java.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.brainfarm.java.feat.api.params.IEvolutionParameter;

public class NeatParametersTableModel extends AbstractTableModel {
	
	/**
	 * Default generated serial id.
	 */
	private static final long serialVersionUID = -968848652196505963L;

	public List<IEvolutionParameter> data = new ArrayList<IEvolutionParameter>();
	
	public static final String columnNames[] = { "Parameter", "Value", "Description" };

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
		
		/*switch (col) {
			case 0:
				return data.get(row).getName();
			case 1:
				return data.get(row).getVal();
			case 2:
				return data.get(row).getDescription();
		}
		*/
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
			//data.get(row).setVal(String.valueOf(value));
		}
		
		fireTableCellUpdated(row, col);
	}
	
	public void setData(List<IEvolutionParameter> data) {
		this.data = data;
		
		fireTableDataChanged();
	}

}