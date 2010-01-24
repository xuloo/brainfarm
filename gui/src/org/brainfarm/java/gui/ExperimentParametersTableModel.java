package org.brainfarm.java.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.brainfarm.java.neat.context.IExperiment;
import org.brainfarm.java.neat.params.AbstractNeatParameter;

public class ExperimentParametersTableModel extends AbstractTableModel {

	/**
	 * Auto-Generated serial id.
	 */
	private static final long serialVersionUID = 5504197431870637433L;
	
	public static final String columnNames[] = { "Parameter", "Value" };
	
	public IExperiment data;

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 8;
	}
	
	public boolean isCellEditable(int row, int col) {
		
		if (col == 1) {
			return true;
		}
		
		return false;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object value = null;
		if (data != null) {
			switch (row) {
			
				case 0:
					if (col == 0) {
						return "Data Source";
					} else if (col == 1) {
						return (data == null) ? "" : data.getDataSource().toString();
					}
					break;
					
				case 1:
					break;
					
				case 2:
					break;
				
				case 3:
					break;
				
				case 4:
					break;
				
				case 5:
					break;
				
				case 6:
					break;
				
				case 7:
					break;
					
				default:
					break;
			}
		}
		
		return value;
	}

	public void setData(IExperiment data) {
		this.data = data;
		
		fireTableDataChanged();
	}
}
