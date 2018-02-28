
public class Cell {
	
	// Type of the cell
	private char type;
	// indexes of the cell with reference to the matrix
	private int row;
	private int column;
	
	// Slices
	private Slice slice;
	
	// True if the cell cannot be part of any slice
	private boolean useless;
	
	public Cell(char type, int row, int column) {
		this.type = type;
		this.row = row;
		this.column = column;
		
		// Vector to contains the slices
		this.slice = null;
		
	}
	
	public char getType() {
		return type;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void addSlice(Slice slice) {
		this.slice = slice;
	}
	
	public Slice getSlice() {
		return slice;
	}
	
	public void removeSlice() {
		this.slice = null;
	}
	
	public void setUseless() {
		useless = true;
	}
	
	public boolean isUseless() {
		return useless;
	}
	
}
