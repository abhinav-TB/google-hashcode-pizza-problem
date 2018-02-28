import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	public static String in = "/Users/alessandro/Documents/workspace/Google_Hashcode2018_Pizza_Problem/dataset/big.in";
	public static String out = "/Users/alessandro/Documents/workspace/Google_Hashcode2018_Pizza_Problem/output/big.out";

	public static void main(String[] args) throws Exception {
		// Read dataset
		File file = new File(in);
		Scanner scanner = new Scanner(file);
		
		//Read information of the problem instance from the dataset
		int R = scanner.nextInt();
		int C = scanner.nextInt();
		int L = scanner.nextInt();
		int H = scanner.nextInt();
		
		// Skip line
		scanner.nextLine();
		
		// Create the matrix from the dataset
		Cell[][] matrix = new Cell[R][C];
		
		// Counting 'T' and 'M' in the dataset
		int T = 0;
		int M = 0;
		
		for (int i = 0; i < R; i++) {
			String line = scanner.nextLine();
			for (int j = 0; j < C; j++) {
				matrix[i][j] = new Cell(line.charAt(j), i, j);
				
				if (matrix[i][j].getType() == 'T') {
					T++;
				} else {
					M++;
				}
			}
		}
		
		// Vector of the slices
		System.out.println("MAX SLICES: " + (Math.min(T, M) / L));
		ArrayList<Slice> slices = new ArrayList<Slice>(Math.min(T, M) / L);
		
		resolveMatrix(matrix, slices, R, C, L, H, 10000, 0);
		
		if (checkSlicesCorrectness(slices, R, C, L, H)) {
			computePoints(slices);
			createOutputFile(slices, out, R, C);
		} else {
			System.out.println("The slices are wrong!");
		}
		
		
		//printMatrix(matrix);
		
	}
	
	public static void resolveMatrix(Cell[][] matrix, ArrayList<Slice> slices, int R, int C, int L, int H, long time, int minCells) {
		long startTime = System.currentTimeMillis();
		int freeCells = R*C;
		int minFreeCells = R*C;
		// First round only free cells
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				// If the cell is free (it has no slice)
				if (matrix[i][j].getSlice() == null) {
					for(int d = H; d >= 2*L; d--) {
						boolean found = false;
						Forms forms = new Forms(d);
						for (Form form : forms) {								
							if ((i + form.getRows() < R) && (i + form.getRows() >= 0) && (j + form.getColumns() < C) && (j + form.getColumns() >= 0)) {
								if (checkSliceRequirements(matrix, L, i, i + form.getRows(), j, j + form.getColumns(), true)) {										
									createSlice(matrix, slices, d, i, i + form.getRows(), j, j + form.getColumns());
									found = true;
									freeCells -= d;
									break;
								}
							}
						}
						if (found) {
							break;
						}
					}
				}
			}
		}
		
		// Initializing minFreeCells
		minFreeCells = freeCells;
		
		System.out.println("...........");
		System.out.println("CELL REMAINING: " + freeCells);
		System.out.println("CELL REMAINING MIN: " + minFreeCells);
		System.out.println("SLICES: " + slices.size());
		System.out.println("...........");
		System.out.println();
		
		while(freeCells > 0 && (time == 0 || (startTime + time) >= System.currentTimeMillis()) && (minCells == 0 || minFreeCells < minCells)) {
			
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					// If the cell is free (it has no slice)
					if (matrix[i][j].getSlice() == null /*&& !matrix[i][j].isUseless()*/) {
						for(int d = H; d >= 2*L; d--) {
							boolean found = false;
							Forms forms = new Forms(d);
							for (Form form : forms) {								
								if ((i + form.getRows() < R) && (i + form.getRows() >= 0) && (j + form.getColumns() < C) && (j + form.getColumns() >= 0)) {
									if (checkSliceRequirements(matrix, L, i, i + form.getRows(), j, j + form.getColumns(), true)) {										
										createSlice(matrix, slices, d, i, i + form.getRows(), j, j + form.getColumns());
										found = true;
										freeCells -= d;
										break;
									}
								}
							}
							if (found) {
								break;
							}
						}
					}
					
					if (freeCells < minFreeCells) {
						minFreeCells = freeCells;
					}
					
					// Check if it can be constructed a slice destroying other slices
					if (matrix[i][j].getSlice() == null /*&& !matrix[i][j].isUseless()*/) {
//						System.out.println("CELL: " + i + " " + j);
						for(int d = H; d >= 2*L; d--) {
							boolean found = false;
							Forms forms = new Forms(d);
							for (Form form : forms) {
//								System.out.println("FORM: " + form.getColumns() + " " + form.getRows());
								if ((i + form.getRows() < R) && (i + form.getRows() >= 0) && (j + form.getColumns() < C) && (j + form.getColumns() >= 0)) {
									if (checkSliceRequirements(matrix, L, i, i + form.getRows(), j, j + form.getColumns(), false)) {										
										int freedCells = deleteSlices(matrix, slices, d, i, i + form.getRows(), j, j + form.getColumns());
										createSlice(matrix, slices, d, i, i + form.getRows(), j, j + form.getColumns());
										freeCells += freedCells - d;
										found = true;
										break;
									}
								}
							}
							
							if (found) {
								break;
							} else if (d == 2*L) {
								//System.out.println("USELESS");
								//freeCells--;
								//matrix[i][j].setUseless();
							}
						}
						
					}
				}
			}
			
			if (freeCells < minFreeCells) {
				minFreeCells = freeCells;
			}
			
			System.out.println("CELL REMAINING: " + freeCells);
			System.out.println("CELL REMAINING MIN: " + minFreeCells);
			System.out.println("SLICES: " + slices.size());
			System.out.println();
		}
		
		if (freeCells != minFreeCells) {
			// Reset M+matrix
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[i].length; j++) {
					matrix[i][j].removeSlice();
				}
			}
			
			// Reset slices
			slices.clear();
			
			// Recall this method with minCells updated
			resolveMatrix(matrix, slices, R, C, L, H, time, minFreeCells);
		}
	}
	
	public static boolean checkSliceRequirements(Cell[][] matrix, int L, int rowStart, int rowEnd, int columnStart, int columnEnd, boolean mustBeFree) {		
		int T = 0;
		int M = 0;
		
		if (rowStart > rowEnd) {
			int swap = rowStart;
			rowStart = rowEnd;
			rowEnd = swap;
		}
		if (columnStart > columnEnd) {
			int swap = columnStart;
			columnStart = columnEnd;
			columnEnd = swap;
		}
		
//		System.out.println("CHECK SLICE " + columnStart + " " + columnEnd + "     " + rowStart + " " + rowEnd);
//		System.out.println();
		
		for (int i = rowStart; i <= rowEnd; i++) {
			for (int j = columnStart; j <= columnEnd; j++) {
				if (mustBeFree && matrix[i][j].getSlice() != null) {
					return false;
				}
				if (matrix[i][j].getType() == 'T') {
					T++;
				} else {
					M++;
				}
			}
		}
		
		return (T >= L && M >= L);
	}
	
	public static void createSlice(Cell[][] matrix, ArrayList<Slice> slices, int area, int rowStart, int rowEnd, int columnStart, int columnEnd) {		
		if (rowStart > rowEnd) {
			int swap = rowStart;
			rowStart = rowEnd;
			rowEnd = swap;
		}
		if (columnStart > columnEnd) {
			int swap = columnStart;
			columnStart = columnEnd;
			columnEnd = swap;
		}
		
//		System.out.println("--CREATE SLICE " + columnStart + " " + columnEnd + "     " + rowStart + " " + rowEnd);
//		System.out.println();
		
		Slice slice = new Slice(area);
		slices.add(slice);
		
		for (int i = rowStart; i <= rowEnd; i++) {
			for (int j = columnStart; j <= columnEnd; j++) {
				matrix[i][j].addSlice(slice);
				slice.addCell(matrix[i][j]);
			}
		}
	}
	
	public static int deleteSlices(Cell[][] matrix, ArrayList<Slice> slices, int area, int rowStart, int rowEnd, int columnStart, int columnEnd) {
		int freedCells = 0;	
		
		if (rowStart > rowEnd) {
			int swap = rowStart;
			rowStart = rowEnd;
			rowEnd = swap;
		}
		if (columnStart > columnEnd) {
			int swap = columnStart;
			columnStart = columnEnd;
			columnEnd = swap;
		}
		
//		System.out.println("--DELETE SLICE " + columnStart + " " + columnEnd + "     " + rowStart + " " + rowEnd);
		
		for (int i = rowStart; i <= rowEnd; i++) {
			for (int j = columnStart; j <= columnEnd; j++) {
				Slice slice = matrix[i][j].getSlice();
				if (slice != null) {
					// Delete slice from all the cells
					for (Cell cell : slice) {
						cell.removeSlice();
					}
					// Delete slice from the slices array
					slices.remove(slice);
					
					freedCells += slice.getArea();
				}
			}
		}
		
//		System.out.println("FREED: " + freedCells);
//		System.out.println();
		
		return freedCells;
	}
	
	public static boolean checkSlicesCorrectness(ArrayList<Slice> slices, int R, int C, int L, int H) {
		boolean[][] matrix = new boolean[R][C];
		
		// Reset matrix
		for (int i = 0; i < R; i++) {
			for (int j = 0; j < C; j++) {
				matrix[i][j] = false;
			}
		}
		
		for (Slice slice : slices) {
			// If the area is too big
			if (slice.getArea() > H) {
				System.out.println("AREA TOO BIG!");
				return false;
			}
			for (Cell cell : slice) {
				if (matrix[cell.getRow()][cell.getColumn()]) {
					System.out.println("CELL ALREADY USED!");
					return false;
				}
				matrix[cell.getRow()][cell.getColumn()] = true;
			}
		}
		
		return true;
	}
	
	public static void computePoints(ArrayList<Slice> slices) {
		int points = 0;
		for (Slice slice : slices) {
			points += slice.getArea();
		}
		
		System.out.println("Total points: " + points); 
	}
	
	public static void createOutputFile(ArrayList<Slice> slices, String filename, int R, int C) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		
		//Write the number of slices
		writer.println(slices.size());
		
		// Write slices coordinates
		for (Slice slice : slices) {
			Cell firstCell = new Cell(' ', R, C);
			Cell lastCell = new Cell(' ', 0, 0);
			
			for (Cell cell : slice) {
				if (cell.getRow() < firstCell.getRow()) {
					firstCell = cell;
				} else if (cell.getRow() == firstCell.getRow() && cell.getColumn() <= firstCell.getColumn()) {
					firstCell = cell;
				}
				
				if (cell.getRow() > lastCell.getRow()) {
					lastCell = cell;
				} else if (cell.getRow() == lastCell.getRow() && cell.getColumn() >= lastCell.getColumn()) {
					lastCell = cell;
				}
			}
			
			writer.println(firstCell.getRow() + " " + firstCell.getColumn() + " " + lastCell.getRow() + " " + lastCell.getColumn());
		}
		
		writer.close();
	}
	
	public static void printMatrix(Cell[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j].getType());
			}
			System.out.println();
		}
	}
}
