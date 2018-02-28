public class Form {
		
		private int rows;
		private int columns;
		
		public Form(int rows, int columns) {
			
			if (rows > 0) {
				this.rows = rows -1;
			} else {
				this.rows = rows +1;
			}
			
			if (columns > 0) {
				this.columns = columns -1;
			} else {
				this.columns = columns +1;
			}
			
		}
		
		public int getRows() {
			return rows;
		}
		
		public int getColumns() {
			return columns;
		}
		
		public String toString() {
			return "[" + columns + ", " + rows + "]";
			
		}
		
		public boolean equals(Object o) {
			Form that = (Form) o;
			return that.rows == this.rows && that.columns == this.columns;
			
		}
		
		public int compareTo(Object o) {
			Form that = (Form) o;
			
			if (this.columns > that.columns) {
				return 1;
			} else if (this.columns < that.columns) {
				return -1;
			} else {
				return 0;
			}
		}
		
	}