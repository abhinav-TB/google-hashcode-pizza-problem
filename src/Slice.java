import java.util.ArrayList;
import java.util.Iterator;

public class Slice implements Iterable<Cell> {
	
	// Cells of the matrix component the slice
	private ArrayList<Cell> cells;
	
	private int area;
	
	public Slice(int area) {
		cells = new ArrayList<Cell>(area);
		
		this.area = area;
	}
	
	public void addCell(Cell cell) {
		cells.add(cell);
	}
	
	public Cell getCell(int index) {
		return cells.get(index);
	}
	
	public int getArea() {
		return area;
	}

	@Override
	public Iterator<Cell> iterator() {
		// TODO Auto-generated method stub
		return cells.iterator();
	}
}
