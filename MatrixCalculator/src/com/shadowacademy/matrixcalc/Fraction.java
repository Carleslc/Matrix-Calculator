package com.shadowacademy.matrixcalc;

import java.math.BigInteger;

/** Reduced fraction n/d, n and d integers, with d > 0 */

public class Fraction {

	private int n, d;
	private double value;
	
	public Fraction(int n, int d) {
		if (d == 0)
			throw new ArithmeticException("Denominator is 0!");
		this.n = n;
		this.d = d;
		if (d < 0) {
			this.n *= -1;
			this.d *= -1;
		}
		this.value = ((double)n)/d;
		reduce();
	}
	
	public Fraction(double value) {
		this.value = value;
		String[] parts = String.valueOf(value).split("\\."); // [0]: integer, [1]: fractional
		this.n = Integer.parseInt(parts[0] + parts[1]);
		this.d = (int) Math.pow(10, parts[1].length());
		reduce();
	}
	
	// Parse a fraction with format n/d or with decimal format (dot as comma)
	public Fraction(String f) {
		String[] terms = f.split("/");
		if (terms.length == 2) {
			if (terms[1].equals("0"))
				throw new ArithmeticException("Denominator is 0!");
			this.n = Integer.parseInt(terms[0]);
			this.d = Integer.parseInt(terms[1]);
			if (d < 0) {
				this.n *= -1;
				this.d *= -1;
			}
			this.value = ((double)n)/d;
			reduce();
		}
		else {
			try {
				this.value = Double.parseDouble(f);
				Fraction copy = new Fraction(value);
				this.d = copy.d;
				this.n = copy.n;
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException(f + " is not a fraction with correct format \"n/d\" or a dot-as-comma decimal format, or is not fractionable");
			}
		}
	}
	
	public Fraction(Fraction other) {
		this.value = other.value;
		this.n = other.n;
		this.d = other.d;
	}
	
    public Fraction add(Fraction other) {
        return new Fraction(n*other.d + d*other.n, d*other.d);
    }
    
    public Fraction sub(Fraction other) {
        return new Fraction(n*other.d - d*other.n, d*other.d);
    }
    
	public Fraction mul(Fraction other) {
        return new Fraction(n*other.n, d*other.d);
    }
	
	public Fraction div(Fraction other) {
        return new Fraction(n*other.d, d*other.n);
    }
	
	public double eval() {
		return value;
	}
	
	public String eval(int decimals) {
		return String.format("%." + decimals + "d", value);
	}
	
	public Fraction inverse() {
		return new Fraction(d, n);
	}
	
	private void reduce() {
		int gcd = gcd(n, d);
		
		if (gcd < 0)
			gcd = -gcd;

		n = n / gcd;
		d = d / gcd;
	}
	
	// Greatest common divisor
	private int gcd(int a, int b) {
		if (a%b == 0) return b;
		return gcd(b, a%b);
	}
	
	/** @return if a double value is fractionable (if not, this is because the numerator or denominator without reduction is greater than Integer.MAX_VALUE = 2^31 - 1) */
	public static boolean isFractionable(double value) {
		return new BigInteger(String.valueOf(value).replace(".", "")).compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) <= 0;
	}
	
	@Override
	/** @return a fraction with format "n/d" if denominator is not 1 or numerator value otherwise */
    public String toString() {
		if (d == 1)
			return String.valueOf(n);
		else
			return n+"/"+d;
    }
	
}
