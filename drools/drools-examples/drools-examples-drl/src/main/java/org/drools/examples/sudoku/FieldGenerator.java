package org.drools.examples.sudoku;
/**
 * @author <a href="mailto:michael.frandsen@syngenio.de">Michael Frandsen</a>
 */
public class FieldGenerator {

	private static FieldGenerator fieldGenerator = new FieldGenerator();
	public static FieldGenerator getInstance(){
		return fieldGenerator;
	}
	public int[][] getField(){
		return new int[][]{{0, 5, 6, 8, 0, 1, 9, 4, 0},
						   {9, 0, 0, 6, 0, 5, 0, 0, 3},
						   {7, 0, 0, 4, 9, 3, 0, 0, 8},
						   {8, 9, 7, 0, 4, 0, 6, 3, 5},
						   {0, 0, 3, 9, 0, 6, 8, 0, 0},
						   {4, 6, 5, 0, 8, 0, 2, 9, 1},
						   {5, 0, 0, 2, 6, 9, 0, 0, 7},
						   {6, 0, 0, 5, 0, 4, 0, 0, 9},
						   {0, 4, 9, 7, 0, 8, 3, 5, 0}};
	}
	public int[][] getFieldMiddle(){
		return new int[][]{{8, 4, 7, 0, 0, 0, 2, 5, 6},
						   {5, 0, 0, 0, 8, 0, 0, 0, 4},
						   {2, 0, 0, 0, 7, 0, 0, 0, 8},
						   {0, 0, 0, 3, 0, 8, 0, 0, 0},
						   {0, 5, 1, 0, 0, 0, 8, 7, 2},
						   {0, 0, 0, 5, 0, 7, 0, 0, 0},
						   {4, 0, 0, 0, 5, 0, 0, 0, 7},
						   {6, 0, 0, 0, 3, 0, 0, 0, 9},
						   {1, 3, 2, 0, 0, 0, 4, 8, 5}};
	}
	public int[][] getFieldHard(){
		return new int[][]{{0, 0, 0, 0, 5, 1, 0, 8, 0},
						   {0, 8, 0, 0, 4, 0, 0, 0, 5},
						   {0, 0, 3, 0, 0, 0, 2, 0, 0},
						   {0, 0, 0, 0, 6, 0, 0, 0, 9},
						   {6, 7, 0, 4, 0, 9, 0, 1, 3},
						   {8, 0, 0, 0, 3, 0, 0, 0, 0},
						   {0, 0, 2, 0, 0, 0, 4, 0, 0},
						   {5, 0, 0, 0, 9, 0, 0, 2, 0},
						   {0, 9, 0, 7, 1, 0, 0, 0, 0}};
	}
	
	public int[][] getFieldHard2(){
		return new int[][]{{0,0,0,6,0,0,1,0,0},
						   {0,0,0,0,0,5,0,0,6},
						   {5,0,7,0,0,0,2,3,0},
						   {0,8,0,9,0,7,0,0,0},
						   {9,3,0,0,0,0,0,6,7},
						   {0,0,0,4,0,6,0,1,0},
						   {0,7,4,0,0,0,9,0,1},
						   {8,0,0,7,0,0,0,0,0},
						   {0,0,3,0,0,8,0,0,0}};
	}
	
	public int[][] getFieldHard3(){
		return new int[][]{{0,8,0,0,0,6,0,0,5},
						   {2,0,0,0,0,0,4,8,0},
						   {0,0,9,0,0,8,0,1,0},
						   {0,0,0,0,8,0,1,0,2},
						   {0,0,0,3,0,1,0,0,0},
						   {6,0,1,0,9,0,0,0,0},
						   {0,9,0,4,0,0,8,0,0},
						   {0,7,6,0,0,0,0,0,3},
						   {1,0,0,7,0,0,0,5,0}};
	}
	public int[][] getFieldHard4(){
		return new int[][]{{0,0,0,0,0,4,0,9,5},
						   {6,7,0,5,0,0,0,1,0},
						   {0,0,0,6,0,9,0,0,0},
						   {0,2,0,0,0,0,4,0,0},
						   {8,1,0,0,0,0,0,7,2},
						   {0,0,7,0,0,0,0,8,0},
						   {0,0,0,3,0,5,0,0,0},
						   {0,6,0,0,0,1,0,5,8},
						   {7,3,0,9,0,0,0,0,0}};
	}
}
