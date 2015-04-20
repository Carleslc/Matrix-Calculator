package com.shadowacademy.matrixcalc;

import java.util.Arrays;
import java.util.Scanner;

public class Matrix {
	
	private int[][] matrix;
	private int rank = -1, det = -1;
	private String desc;
	
	public Matrix(int[][] matrix, String desc) {
		this.matrix = matrix;
		this.desc = desc;
	}
	
	public Matrix(Scanner in, String desc) {
		this.desc = desc;
		int NF = 0, NC = -1;
		while (NF <= 0) {
			System.out.print("Filas: ");
			NF = in.nextInt();
		}
		while (NC < 0) {
			System.out.print("Columnas: ");
			NC = in.nextInt();
		}
		System.out.println("Introduce la matriz por filas:");
		matrix = new int[NF][NC];
		for (int i = 0; i < NF; ++i) {
			for (int j = 0; j < NC; ++j)
				matrix[i][j] = in.nextInt();
		}
	}
	
	public Matrix(Matrix m) {
		this.matrix = m.matrix;
		this.rank = m.rank;
		this.det = m.det;
		this.desc = m.desc;
	}
	
	public int getRows() {
		return matrix.length;
	}
	
	public int getColumns() {
		return matrix[0].length;
	}
	
	public int[][] get() {
		return matrix;
	}
	
	public int getRank() {
		if (rank < 0) {
			int r = 0;
			//TODO
			rank = r;
		}
		return rank;
	}
	
	public Matrix inverse() {
		int[][] m = new int[getRows()][getColumns()];
		 //TODO
		return new Matrix(m, "(" + desc + ")^-1");
	}
	
	public int det() {
		if (det < 0) {
			int d = 0;
			//TODO
			det = d;
		}
		return det/*new Matrix(matrix, "det: |" + desc + "|")*/;
	}
	
	public Matrix adjugate(int i, int j) {
		int[][] m = new int[getRows() - 1][getColumns() - 1];
		 //TODO
		return new Matrix(m, "(" + desc + ")-> Adj(" + i + ", " + j + ")");
	}
	
	public Matrix add(Matrix B) {
		int[][] m = new int[getRows()][getColumns()];
		for (int i = 0; i < getRows(); ++i) {
			for (int j = 0; j < getColumns(); ++j)
				m[i][j] = matrix[i][j] + B.matrix[i][j];
		}
		return new Matrix(m, "(" + desc + ")" + " + " + "(" + B.desc + ")");
	}

	public Matrix sub(Matrix B) {
		int[][] m = new int[getRows()][getColumns()];
		for (int i = 0; i < getRows(); ++i) {
			for (int j = 0; j < getColumns(); ++j)
				m[i][j] = matrix[i][j] - B.matrix[i][j];
		}
		return new Matrix(m, "(" + desc + ")" + " - " + "(" + B.desc + ")");
	}
	
	public Matrix add_scalar(int k) {
		int[][] m = new int[getRows()][getColumns()];
		for (int i = 0; i < getRows(); ++i) {
			for (int j = 0; j < getColumns(); ++j)
				m[i][j] = matrix[i][j] + k;
		}
		return new Matrix(m, "(" + desc + ")" + " + " + "(" + k + ")");
	}
	
	/** @precondition this.getColumns() == B.getRows() */
	public Matrix prod(Matrix B) {
		int NF = getRows(), NC = B.getColumns();
		int[][] p = new int[NF][NC];
	    for (int i = 0; i < NF; ++i) {
	        for (int j = 0; j < NC; ++j) {
	            for (int k = 0; k < B.getRows(); ++k)
	                p[i][j] += matrix[i][k]*B.matrix[k][j];
	        }
	    }
		return new Matrix(p, "(" + desc + ")" + "*" + "(" + B.desc + ")");
	}
	
	/** @precondition this is a square matrix */
	public Matrix pow(int exp) {
		if (exp < 2)
			return this;
		else
			return prod(pow(exp - 1));
	}
	
	public Matrix mult_scalar(int k) {
		int NF = getRows(), NC = getColumns();
		int[][] m = new int[NF][NC];
		for (int i = 0; i < NF; ++i) {
			for (int j = 0; j < NC; ++j)
				m[i][j] = matrix[i][j] * k;
		}
		return new Matrix(m, "(" + desc + ")" + "*" + "(" + k + ")");
	}
	
	public Matrix negate() {
		Matrix neg = mult_scalar(-1);
		neg.setDescription("-(" + desc + ")");
		return neg;
	}
	
	public Matrix transposed() {
		int NF = getRows(), NC = getColumns();
		int[][] t = new int[NC][NF];
		for (int i = 0; i < NC; ++i) {
			for (int j = 0; j < NF; ++j)
				t[i][j] = matrix[j][i];
		}
		return new Matrix(t, "(" + desc + ") transposed");
	}
	
	public Matrix subMatrix(int i, int j, int quadrant) {
		int NF = getRows(), NC = getColumns();
		int[][] m;
		switch (quadrant) {
		case 1:
			m = new int[i + 1][j + 1];
			for (int ii = 0; ii <= i; ++ii) {
				for (int jj = 0; jj <= j; ++jj)
					m[ii][jj] = matrix[ii][jj];
			}
			break;
		case 2:
			m = new int[i + 1][NC - j];
			for (int ii = 0; ii <= i; ++ii) {
				for (int jj = j; jj < NC; ++jj)
					m[ii][jj - j] = matrix[ii][jj];
			}
			break;
		case 3:
			m = new int[NF - i][j + 1];
			for (int ii = i; ii < NF; ++ii) {
				for (int jj = 0; jj <= j; ++jj)
					m[ii - i][jj] = matrix[ii][jj];
			}
			break;
		default:
			m = new int[NF - i][NC - j];
			for (int ii = i; ii < NF; ++ii) {
				for (int jj = j; jj < NC; ++jj)
					m[ii - i][jj - j] = matrix[ii][jj];
			}
		}
		return new Matrix(m, "(" + desc + ") " + i + "~" + j + " subMatrix quadrant " + quadrant);
	}
	
	/** @return <tt>this|B</tt>
	 *  @precondition B.getRows() == this.getRows() */
	public Matrix concat(Matrix B) {
		int NF = getRows(), NC = getColumns(), NCb = B.getColumns();
		int[][] res = new int[NF][NC + NCb];
		for (int i = 0; i < NF; ++i) {
			for (int j = 0; j < NC; ++j)
				res[i][j] = matrix[i][j];
			for (int k = NC; k < res[0].length; ++k)
				res[i][k] = B.matrix[i][k - NC];
		}
		return new Matrix(res, "(" + desc + ")" + "|" + "(" + B.desc + ")");
	}
	
	/** <tt>row i <-> row j</tt> */
	public void swapRow(int i, int j) {
		for (int k = 0; k < getColumns(); ++k) {
			int aux = matrix[i][k];
			matrix[i][k] = matrix[j][k];
			matrix[j][k] = aux;
		}
		setDescription("(" + desc + ")" + " F" + i + " <-> " + "F" + j);
	}
	
	/** <tt>row <- k*row</tt> */
	public void multRow(int row, int k) {
		for (int i = 0; i < getColumns(); ++i)
			matrix[row][i] *= k;
		setDescription("(" + desc + ")" + " F" + row + " <- " + "(" + k + ")*" + "F" + row);
	}
	
	/** <tt>row1 <- row1 + k*row2</tt>*/
	public void addMultRow(int row1, int k, int row2) {
		for (int i = 0; i < getColumns(); ++i)
			matrix[row1][i] += k*matrix[row2][i];
		setDescription("(" + desc + ")" + " F" + row1 + " <- " + "F" + row1 + " + (" + k + ")*" + "F" + row2);
	}
	
	public boolean isSquare() {
		return getRows() == getColumns();
	}
	
	public static Matrix diagonal(int n, int k) {
		int[][] m = new int[n][n];
		for (int i = 0; i < n; ++i)
			m[i][i] = k;
		return new Matrix(m, "Diag(k = " + k + ") " + n + "x" + n);
	}
	
	public static Matrix identity(int n) {
		Matrix diag = diagonal(n, 1);
		diag.setDescription("I(" + n + ")");
		return diag;
	}
	
	public static Matrix zero(int n) {
		return new Matrix(new int[n][n], "O(" + n + ")");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(matrix);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(matrix, other.matrix))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return desc + "\n" + matrixToString() + "\n" + getRows() + "x" + getColumns() + (rank >= 0 ? "\n[Rank " + rank + "]" : "") + (det >= 0 ? "\n[Det " + det + "]" : "");
	}
	
	private String matrixToString() {
		String m = "";
		if (matrix.length == 0)
			m = "()";
		else if (matrix.length == 1) {
			m += "(";
			int j;
			for (j = 0; j < getColumns() - 1; ++j) {
				m += matrix[0][j] + ", ";
			}
			m += matrix[0][j] + ")";
		}
		else {
			int i;
			for (i = 0; i < getRows() - 1; ++i) {
				m += i == 0 ? "/" : "|";
				int j;
				for (j = 0; j < getColumns() - 1; ++j) {
					m += matrix[i][j] + ", ";
				}
				m += matrix[i][j] + (i == 0 ? "\\" : "|") + "\n";
			}
			m += "\\";
			int j;
			for (j = 0; j < getColumns() - 1; ++j) {
				m += matrix[i][j] + ", ";
			}
			m += matrix[i][j] + "/";
		}
		return m;
	}

	public String getDescription() {
		return desc;
	}
	
	public void setDescription(String desc) {
		this.desc = desc;
	}
}
