package org.brainfarm.java.gui;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ExperimentTable extends JTable {

	/**
	 * Auto-Generated serial id.
	 */
	private static final long serialVersionUID = 1425752533428960795L;
	
	public ExperimentTable(AbstractTableModel model) {
		super(model);
		
		setRowHeight(25);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {

		// You may call getValueAt method from TableModel
		// and get the class of returned value.	
		if (column == 1 && row == 1) {
			return new MyComboBoxRenderer(new String[]{"CLASS","FILE"});
		}
		
		return super.getCellRenderer(row, column);
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		
		if (column == 1 && row == 1) {
			return new MyComboBoxEditor(new String[]{"CLASS","FILE"});
		}

		// Do the same thing as in getCellRenderer to check class type.
		return super.getCellEditor(row, column);
	}
	
	public class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
		
		/**
		 * Auto-Generated serial id.
		 */
		private static final long serialVersionUID = 574018695449769421L;

		public MyComboBoxRenderer(String[] items) { 
			super(items); 
		} 
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
			if (isSelected) { 
				setForeground(table.getSelectionForeground()); 
				super.setBackground(table.getSelectionBackground()); 
			} else { 
				setForeground(table.getForeground()); 
				setBackground(table.getBackground()); 
			} // Select the current value setSelectedItem(value); 
			
			return this; 
		}
	}
	
	public class MyComboBoxEditor extends DefaultCellEditor { 
		
		/**
		 * Auto-Generated serial id.
		 */
		private static final long serialVersionUID = 99795378241092826L;

		public MyComboBoxEditor(String[] items) { 
			super(new JComboBox(items)); 
		} 
	} 
}
