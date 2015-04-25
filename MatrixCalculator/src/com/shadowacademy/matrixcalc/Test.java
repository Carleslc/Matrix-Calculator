package com.shadowacademy.matrixcalc;

import java.util.Scanner;

/** Prueba de funcionamiento de entrada con fracciones n/d y decimal int.decimals */

public class Test {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String s = in.next();
		try {
			System.out.println(Fraction.isFractionable(Double.parseDouble(s)));
		} catch(Exception ignore) {}
		System.out.println(new Fraction(s));
		in.close();
	}

}
