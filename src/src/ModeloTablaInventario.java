package src;

import javax.swing.table.AbstractTableModel;

public class ModeloTablaInventario extends AbstractTableModel {

    private String[] columnas = {"Marca", "Modelo", "Año", "Precio (€)", "Matrícula"};
    private Object[][] datos;

    public ModeloTablaInventario(Object[][] datos) {
        this.datos = datos;
    }

    @Override
    public int getRowCount() {
        return datos.length;
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return datos[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;  // No editable
    }

    public Object[][] getDatos() {
        return datos;
    }

    public String getMatricula(int fila) {
        return datos[fila][4].toString();
    }
}
