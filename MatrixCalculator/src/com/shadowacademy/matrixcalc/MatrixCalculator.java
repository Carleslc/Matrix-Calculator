package com.shadowacademy.matrixcalc;

import java.util.HashMap;
import java.util.Scanner;

public class MatrixCalculator {

	private static HashMap<Character, Matrix> matrices = new HashMap<Character, Matrix>();
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int opt;
		do {
			System.out.print("<-- MATRIX CALCULATOR -->\n"
				+ "[1]: Añadir matriz\n"
				+ "[2]: Calcular\n"
				+ "[3]: Lista de operaciones\n"
				+ "[4]: Lista de matrices\n"
				+ "[0]: Salir\n-> ");
			opt = in.nextInt();
			Matrix m;
			if (opt == 1) {
				char name = getMatrixName(in);
				m = new Matrix(in, String.valueOf(name));
				matrices.put(name, m);
				System.out.println("Se ha guardado la matriz " + name + ":\n" + m);
			}
			else if (opt == 2) {
				System.out.println("Escribe la operación a realizar:");
				try {
					in.nextLine();
					m = parseOperation(in.nextLine().replace(" ", "").trim());
					System.out.println(m);
					char name = getMatrixName(in);
					matrices.put(name, m);
					System.out.println("Se ha guardado la matriz " + name + " correspondiente a: " + m.getDescription());
				}
				catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
			else if (opt == 4) {
				for (char id : matrices.keySet())
					System.out.println("[" + id + "]: " + matrices.get(id) + "\n");
			}
			else {
				System.out.println("== LISTA DE OPERACIONES ==\n"
					+ "A\n" // Matriz A
					+ "A + B\n"
					+ "A - B\n"
					+ "A * B\n"
					+ "A^-1\n" // Inversa de A
					+ "A^6\n" // A elevado a 6
					+ "At\n" // Transpuesta de A
					+ "-A\n" // Negada de A
					+ "A + 7\n"
					+ "A - 7\n"
					+ "A * 7\n"
					+ "A | B\n" // A concatenada con B
					+ "rank A\n" // Rango de A
					+ "det A\n" // Determinante de A
					+ "subMatrix A #1 #2 #4\n" // Submatriz de A desde la posición (1, 2) incluida, orientado en el cuadrante 4
					+ "diag o5 #7\n" // Matriz diagonal de orden 5 y k = 7
					+ "identity 3\n" // Matriz identidad de orden 3
					+ "zero 4\n" // Matriz nula de orden 4
					+ "=========================="
				);
			}
			System.out.println();
		} while (opt != 0);
	}

	private static char getMatrixName(Scanner in) {
		System.out.println("\nIntroduce un identificador/nombre.");
		String name;
		do {
			System.out.print("Sólo una letra (case-sensitive): ");
			name = in.next();
		} while(!name.matches("[a-zA-Z]"));
		return name.charAt(0);
	}

	private static Matrix parseOperation(String op) throws IllegalArgumentException {
		if (op.length() < 2) {
			if (op.length() == 1) {
				if (matrices.containsKey(op.charAt(0)))
					return matrices.get(op.charAt(0));
				else
					throw new IllegalArgumentException("La matriz " + op.charAt(0) + " no existe.");
			}
			else
				throw new IllegalArgumentException("No se corresponde con una instrucción correcta.");
		}
		else if (op.length() == 5 && op.startsWith("rank")) {
			char at = op.charAt(4);
			if (matrices.containsKey(at)) {
				Matrix get = matrices.get(at);
				get.getRank();
				return get;
			}
			else
				throw new IllegalArgumentException("La matriz " + at + " no existe.");
		}
		else if (op.length() == 4 && op.startsWith("det")) {
			char at = op.charAt(3);
			if (matrices.containsKey(at)) {
				Matrix get = matrices.get(at);
				get.det();
				return get;
			}
			else
				throw new IllegalArgumentException("La matriz " + at + " no existe.");
		}
		else if (op.length() >= 16 && op.startsWith("subMatrix")) {
			char at = op.charAt(9);
			if (matrices.containsKey(at)) {
				Matrix m = matrices.get(at);
				String[] ij = op.split("#");
				try {
					int i = Integer.parseInt(ij[1]);
					int j = Integer.parseInt(ij[2]);
					int q = Integer.parseInt(ij[3]);
					if (i < 0 || j < 0 || q < 1 || q > 4 || i >= m.getRows() || j >= m.getColumns())
						throw new IllegalArgumentException("Los parámetros introducidos son incorrectos (posición fuera de la matriz o cuadrante incorrecto).");
					else
						return matrices.get(at).subMatrix(i, j, q);
				}
				catch (NumberFormatException e) {
					throw new IllegalArgumentException("Instrucción incorrecta. Después de los '#' sólo pueden ir números.");
				}
				catch (IndexOutOfBoundsException e) {
					throw new IllegalArgumentException("Instrucción incorrecta. No te olvides los '#' para indicar la posición y el cuadrante (1, 2, 3 o 4).");
				}
			}
			else
				throw new IllegalArgumentException("La matriz " + at + " no existe.");
		}
		else if (op.length() >= 8 && op.startsWith("diag")) {
			String[] args = op.split("o|#");
			try {
				return Matrix.diagonal(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException("Instrucción incorrecta. Después de 'o' y '#' sólo pueden ir números.");
			}
			catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentException("Instrucción incorrecta. No te olvides indicar el orden con 'o' y el número de la diagonal con '#'.");
			}
		}
		else if (op.length() >= 5 && op.startsWith("zero")) {
			try {
				return Matrix.zero(Integer.parseInt(op.substring(4)));
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException("Instrucción incorrecta. El orden sólo puede ser un número.");
			}
		}
		else if (op.length() >= 9 && op.startsWith("identity")) {
			try {
				return Matrix.identity(Integer.parseInt(op.substring(8)));
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException("Instrucción incorrecta. El orden sólo puede ser un número.");
			}
		}
		else if (op.substring(0, 1).matches("[a-zA-Z]|-")) {
			char at = op.charAt(1), A = op.charAt(0);
			if (A == '-') {
				if (matrices.containsKey(at))
					return matrices.get(at).negate();
				else
					throw new IllegalArgumentException("La matriz " + at + " no existe.");
			}
			else if (at == 't') {
				if (matrices.containsKey(A))
					return matrices.get(A).transposed();
				else
					throw new IllegalArgumentException("La matriz " + A + " no existe.");
			}
			else if (op.length() >= 3) {
				char B = op.charAt(2);
				switch (at) {
					case '+':
						if (matrices.containsKey(A)) {
							if (matrices.containsKey(B))
								return matrices.get(A).add(matrices.get(B));
							else {
								String n = op.substring(2);
								try {
									return matrices.get(A).add_scalar(Integer.parseInt(n));
								}
								catch (NumberFormatException e) {
									throw new IllegalArgumentException(n + " no es un número válido.");
								}
							}
						}
						else
							throw new IllegalArgumentException("La matriz " + A + " no existe.");
					case '-':
						if (matrices.containsKey(A)) {
							if (matrices.containsKey(B))
								return matrices.get(A).sub(matrices.get(B));
							else {
								String n = op.substring(2);
								try {
									return matrices.get(A).add_scalar(-Integer.parseInt(n));
								}
								catch (NumberFormatException e) {
									throw new IllegalArgumentException(n + " no es un número válido.");
								}
							}
						}
						else
							throw new IllegalArgumentException("La matriz " + A + " no existe.");
					case '*':
						if (matrices.containsKey(A)) {
							if (matrices.containsKey(B)) {
								Matrix A_Matrix = matrices.get(A), B_Matrix = matrices.get(B);
								if (A_Matrix.getColumns() == B_Matrix.getRows())
									return A_Matrix.prod(B_Matrix);
								else
									throw new IllegalArgumentException("El número de columnas de " + A + " es diferente al número de filas de " + B + ".");
							}
							else {
								String n = op.substring(2);
								try {
									return matrices.get(A).mult_scalar(Integer.parseInt(n));
								}
								catch (NumberFormatException e) {
									throw new IllegalArgumentException(n + " no es un número válido.");
								}
							}
						}
						else
							throw new IllegalArgumentException("La matriz " + A + " no existe.");
					case '^':
						if (matrices.containsKey(A)) {
							if (B == '-')
								return matrices.get(A).inverse();
							else {
								String n = op.substring(2);
								try {
									Matrix A_Matrix = matrices.get(A);
									if (A_Matrix.isSquare()) {
										Matrix Apow = A_Matrix.pow(Integer.parseInt(n));
										Apow.setDescription("(" + A_Matrix.getDescription() + ")^" + n);
										return Apow;
									}
									else
										throw new IllegalArgumentException(A + " no es una matriz cuadrada.");
								}
								catch (NumberFormatException e) {
									throw new IllegalArgumentException(n + " no es un número válido.");
								}
							}
						}
						else
							throw new IllegalArgumentException("La matriz " + A + " no existe.");
					case '|':
						if (matrices.containsKey(A) && matrices.containsKey(B)) {
							Matrix A_Matrix = matrices.get(A), B_Matrix = matrices.get(B);
							if (A_Matrix.getRows() == B_Matrix.getRows())
								return A_Matrix.concat(B_Matrix);
							else
								throw new IllegalArgumentException("El número de filas de " + A + " es diferente al número de filas de " + B + ".");
						}
						else
							throw new IllegalArgumentException("La matriz " + A + " o " + B + " no existe.");
					default:
						throw new IllegalArgumentException("No se corresponde con una instrucción correcta.");
				}
			}
			else
				throw new IllegalArgumentException("No se corresponde con una instrucción correcta.");
		}
		else
			throw new IllegalArgumentException("La matriz " + op.charAt(0) + " no existe.");
	}
}
