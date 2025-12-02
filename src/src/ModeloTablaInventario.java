package src;

import javax.swing.table.AbstractTableModel;

import java.util.List;

public class ModeloTablaInventario extends AbstractTableModel {

    private final String[] columnas = {"Marca", "Modelo", "Año", "Precio (€)", "Matrícula"};
    private List<Coche> coches;

    public ModeloTablaInventario(List<Coche> coches) {
        this.coches = coches;
    }

    @Override
    public int getRowCount() {
        return coches.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Coche c = coches.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> c.getMarca();
            case 1 -> c.getModelo();
            case 2 -> c.getAnio();
            case 3 -> c.getPrecio();
            case 4 -> c.getMatricula();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setCoches(List<Coche> coches) {
        this.coches = coches;
        fireTableDataChanged();
    }

    public String getMatricula(int fila) {
        String matricula = coches.get(fila).getMatricula();
        System.out.println("DEBUG - Matrícula obtenida de fila " + fila + ": '" + matricula + "'");
        return matricula;
    }
}