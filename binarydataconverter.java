import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Scanner;

public class humeyraceydapolat {

	static int control = 0;

	public static void main(String[] args) throws IOException {

		File file = new File("input.txt");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
		int c;
		int x = 0;
		char[] chars = new char[200];

		while ((c = reader.read()) != -1) {
			char character = (char) c;
			if ((character >= 48 && character <= 57) || (character >= 97 && character <= 102)) {
				chars[x] = character;
				x++;
			}
		}
		PrintWriter writer = new PrintWriter("Output.txt", "UTF-8");

		Scanner input = new Scanner(System.in);

		System.out.println("Byte ordering type(little endian/big endian):");
		String orderingType = input.nextLine();

		System.out.println("Data type to be converted(signed, unsigned or floating point):");
		String dataType = input.nextLine();

		System.out.println("Size of the given data type:");
		int dataSize = input.nextInt();

		int y = 0, z = 0;

		while (z < x) {
			int k, bias, exponent;
			double mantissa;
			String s = "", binary = "", expoPart = "", fracPart = "", floatingPoint = "";
			if (dataSize == 1) {
				if (dataType.equals("signed")) {
					k = 0;
					while (k < 2) {
						s = new StringBuilder("").append(chars[y]).toString();
						if (orderingType.equalsIgnoreCase("little endian")
								|| orderingType.equalsIgnoreCase("big endian")) {
							binary += hexToBinary(s);
						}
						y++;
						k++;
					}
					z++;
					int decimal = signedConvert(binary);
					// System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");
				} else if (dataType.equals("unsigned")) {
					k = 0;
					while (k < 2) {
						s = new StringBuilder("").append(chars[y]).toString();
						if (orderingType.equalsIgnoreCase("little endian")
								|| orderingType.equalsIgnoreCase("big endian")) {
							binary += hexToBinary(s);
						}
						y++;
						k++;
					}
					z++;
					int decimal = unsignedConvert(binary);
					// System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");
				} else if (dataType.equals("floating point")) {
					bias = 7;
					k = 0;
					while (k < 2) {
						s = new StringBuilder("").append(chars[y]).toString();
						if (orderingType.equalsIgnoreCase("little endian")
								|| orderingType.equalsIgnoreCase("big endian")) {
							binary += hexToBinary(s);
						}
						y++;
						k++;
					}
					z++;
					if (binary.charAt(0) == '1')
						floatingPoint = "-";

					expoPart = binary.substring(1, 5); // binary representation of exponent part
					int binExp = stringToInteger(expoPart);// convert int to string exponent
					exponent = getDecimal(binExp);// get the decimal value of exponent
					// System.out.println("exp: " + exponent);
					fracPart = binary.substring(5);// binary representation of fraction part

					if (expoPart.equals("0000") && fracPart.equals("000")) {// if exponent part is denormalized
						writer.println("±0" + "	");
					} else if (expoPart.equals("0000") && !fracPart.equals("000")) {
						fracPart = "0." + fracPart;// mantissa of fraction
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, 1 - bias));// calculate
																											// the
																											// floating
																											// point
																											// number
						// System.out.println(floatingPoint);
						writer.println(floatingPoint + "	");
					} else if (expoPart.equals("1111") && fracPart.equals("000")) {// if exponent part is special
																					// values
						writer.println("infinity" + "	");
					} else if (expoPart.equals("1111") && !fracPart.equals("000")) {
						writer.println("NaN" + "	");
					} else {// if exponent is normalized
						fracPart = "1." + fracPart;// mantissa of fraction
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, exponent - bias));
						writer.println(floatingPoint + "	");
					}

				}
			}
			if (dataSize == 2) {
				bias = 31;
				if (dataType.equals("signed")) {
					k = 0;
					while (k < 4) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittleSigned2(y, binary);

						} else if (orderingType.equalsIgnoreCase("big endian")) {
						}
						y++;
						k++;
					}
					z++;
					int decimal = signedConvert(binary);
					System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");
				} else if (dataType.equals("unsigned")) {
					k = 0;
					while (k < 4) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle(y, binary);

						} else if (orderingType.equalsIgnoreCase("big endian")) {

						}
						y++;
						k++;
					}
					z++;

					int decimal = unsignedConvert(binary);
					// System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");

				} else if (dataType.equals("floating point")) {
					k = 0;
					while (k < 4) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle(y, binary);

						} else if (orderingType.equalsIgnoreCase("big endian")) {
						}
						y++;
						k++;
					}
					if (binary.charAt(0) == '1')
						floatingPoint = "-";

					expoPart = binary.substring(1, 7); // binary representation of exponent part
					int binExp = stringToInteger(expoPart);// convert int to string exponent
					exponent = getDecimal(binExp);// get the decimal value of exponent
					// System.out.println("exp: " + exponent);
					fracPart = binary.substring(7);// binary representation of fraction part

					if (expoPart.equals("000000") && fracPart.equals("000000000")) {// if exponent part is
																					// denormalized
						writer.println("±0" + "	");
					} else if (expoPart.equals("000000") && !fracPart.equals("000000000")) {
						fracPart = "0." + fracPart;// mantissa of fraction
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, 1 - bias));// calculate
																											// the
																											// floating
																											// point
																											// number
						System.out.println("Floating point : " + floatingPoint);
						writer.println(floatingPoint + "	");
					} else if (expoPart.equals("111111") && fracPart.equals("000000000")) {// if exponent part is
																							// special values
						writer.println("infinity" + "	");
					} else if (expoPart.equals("111111") && !fracPart.equals("000000000")) {
						writer.println("NaN" + "	");
					} else {// if exponent is normalized
						fracPart = "1." + fracPart;// mantissa of fraction
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, exponent - bias));
						System.out.println(floatingPoint);
						writer.println(floatingPoint + "	");
					}
				}
			}
			if (dataSize == 3) {
				bias = 127;
				if (dataType.equals("signed")) {
					k = 0;
					while (k < 6) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle3(y, binary);
						} else if (orderingType.equalsIgnoreCase("big endian")) {
						}
						y++;
						k++;
					}
					z++;
					int decimal = signedConvert(binary);
					System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");
				} else if (dataType.equals("unsigned")) {
					k = 0;
					while (k < 6) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle3(y, binary);

						} else if (orderingType.equalsIgnoreCase("big endian")) {

						}
						y++;
						k++;
					}
					z++;

					int decimal = unsignedConvert(binary);
					System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");

				} else if (dataType.equals("floating point")) {
					k = 0;
					while (k < 6) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle3(y, binary);

						} else if (orderingType.equalsIgnoreCase("big endian")) {
						}
						y++;
						k++;
					}

					if (binary.charAt(0) == '1')
						floatingPoint = "-";

					expoPart = binary.substring(1, 9); // binary representation of exponent part
					int binExp = stringToInteger(expoPart);// convert int to string exponent
					exponent = getDecimal(binExp);// get the decimal value of exponent
					fracPart = binary.substring(9);// binary representation of fraction part

					if (expoPart.equals("00000000") && fracPart.equals("000000000000000")) {// if exponent part is
						// denormalized
						writer.println("±0" + "	");
					} else if (expoPart.equals("00000000") && !fracPart.equals("000000000000000")) {
						String fracPartForRounding = fracPart.substring(13);
						if (fracPartForRounding.charAt(0) == '1') {
							if (fracPartForRounding.charAt(1) == '1') {
								fracPart = addBinary(fracPart, "1");
							}
						}
						fracPart = "0." + fracPart;// mantissa of fraction
						// System.out.println("FRAC: " + fracPart);
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, 1 - bias));// calculate
																											// the
																											// floating
																											// point
																											// number
						System.out.println("Floating point : " + floatingPoint);
						writer.println(floatingPoint + "	");
					} else if (expoPart.equals("11111111") && fracPart.equals("000000000000000")) {// if exponent
																									// part
																									// is
																									// special
																									// values
						writer.println("infinity" + "	");
					} else if (expoPart.equals("11111111") && !fracPart.equals("000000000000000")) {
						writer.println("NaN" + "	");
					} else {// if exponent is normalized
						String fracPartForRounding = fracPart.substring(13);
						if (fracPartForRounding.charAt(0) == '1') {
							if (fracPartForRounding.charAt(1) == '1') {
								fracPart = addBinary(fracPart, "1");
							}
						} else {
							fracPart = fracPart.substring(0, 14);
						}
						fracPart = "1." + fracPart;// mantissa of fraction
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, exponent - bias));// calculate
						// the
						// floating
						// point
						// number
						System.out.println("Floating point : " + floatingPoint);
						writer.println(floatingPoint + "	");

					}
				}
			}
			if (dataSize == 4) {
				bias = 511;
				if (dataType.equals("signed")) {
					k = 0;
					while (k < 8) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle4(y, binary);
						} else if (orderingType.equalsIgnoreCase("big endian")) {
						}
						y++;
						k++;
					}
					z++;
					int decimal = signedConvert(binary);
					System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");
				} else if (dataType.equals("unsigned")) {
					k = 0;
					while (k < 8) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle4(y, binary);

						} else if (orderingType.equalsIgnoreCase("big endian")) {

						}
						y++;
						k++;
					}
					z++;

					int decimal = unsignedConvert(binary);
					System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");

				} else if (dataType.equals("floating point")) {
					k = 0;
					while (k < 8) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle4(y, binary);

						} else if (orderingType.equalsIgnoreCase("big endian")) {
						}
						y++;
						k++;
					}

					if (binary.charAt(0) == '1')
						floatingPoint = "-";

					expoPart = binary.substring(1, 11); // binary representation of exponent part
					int binExp = stringToInteger(expoPart);// convert int to string exponent
					exponent = getDecimal(binExp);// get the decimal value of exponent
					fracPart = binary.substring(11);// binary representation of fraction part

					if (expoPart.equals("0000000000") && fracPart.equals("000000000000000000000")) {// if exponent
																									// part
																									// is
						// denormalized
						writer.println("±0" + "	");
					} else if (expoPart.equals("0000000000") && !fracPart.equals("000000000000000000000")) {
						String fracPartForRounding = fracPart.substring(13);
						fracPart = fracPart.substring(0, 13);
						if (fracPartForRounding.charAt(0) == '1') {
							if (fracPartForRounding.charAt(1) == '1') {
								fracPart = addBinary(fracPart, "1");
							}
						}
						fracPart = "0." + fracPart;// mantissa of fraction
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, 1 - bias));// calculate
																											// the
																											// floating
																											// point
																											// number
						System.out.println("Floating point : " + floatingPoint);
						writer.println(floatingPoint + "	");
					} else if (expoPart.equals("1111111111") && fracPart.equals("000000000000000000000")) {// if
																											// exponent
																											// part
						// is
						// special values
						writer.println("infinity" + "	");
					} else if (expoPart.equals("1111111111") && !fracPart.equals("000000000000000000000")) {
						writer.println("NaN" + "	");
					} else {// if exponent is normalized
						String fracPartForRounding = fracPart.substring(13);
						fracPart = fracPart.substring(0, 13);
						if (fracPartForRounding.charAt(0) == '1') {
							if (fracPartForRounding.charAt(1) == '1') {
								fracPart = addBinary(fracPart, "1");
							}
						} else {
							fracPart = fracPart.substring(0, 14);
						}
						fracPart = "1." + fracPart;// mantissa of fraction
						// System.out.println("FRAC: " + fracPart);
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, exponent - bias));// calculate
																												// the
																												// floating
																												// point
																												// number
						System.out.println("Floating point : " + floatingPoint);
						writer.println(floatingPoint + "	");
					}
				}
			}
			if (dataSize == 6) {
				bias = 2047;
				if (dataType.equals("signed")) {
					k = 0;
					while (k < 12) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle6(y, binary);
						} else if (orderingType.equalsIgnoreCase("big endian")) {
						}
						y++;
						k++;
					}
					z++;
					int decimal = signedConvert(binary);
					System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");
				} else if (dataType.equals("unsigned")) {
					k = 0;
					while (k < 12) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle6(y, binary);

						} else if (orderingType.equalsIgnoreCase("big endian")) {

						}
						y++;
						k++;
					}
					z++;

					int decimal = unsignedConvert(binary);
					System.out.println("Decimal : " + decimal);
					writer.println(decimal + "	");

				} else if (dataType.equals("floating point")) {
					k = 0;
					while (k < 12) {
						s = new StringBuilder("").append(chars[y]).toString();
						binary += hexToBinary(s);
						if (orderingType.equalsIgnoreCase("little endian")) {
							binary = ifOrderingTypeLittle6(y, binary);

						} else if (orderingType.equalsIgnoreCase("big endian")) {
						}
						y++;
						k++;
					}

					if (binary.charAt(0) == '1')
						floatingPoint = "-";

					expoPart = binary.substring(1, 11); // binary representation of exponent part
					int binExp = stringToInteger(expoPart);// convert int to string exponent
					exponent = getDecimal(binExp);// get the decimal value of exponent
					fracPart = binary.substring(11);// binary representation of fraction part

					if (expoPart.equals("000000000000") && fracPart.equals("00000000000000000000000000000000000")) {// if
																													// exponent
																													// part
																													// is
																													// denormalized
						writer.println("±0" + "	");
					} else if (expoPart.equals("000000000000")
							&& !fracPart.equals("00000000000000000000000000000000000")) {
						String fracPartForRounding = fracPart.substring(13);
						if (fracPartForRounding.charAt(0) == '1') {
							if (fracPartForRounding.charAt(1) == '1') {
								fracPart = addBinary(fracPart, "1");
							}
						}
						fracPart = "0." + fracPart;// mantissa of fraction
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, 1 - bias));// calculate
																											// the
																											// floating
																											// point
																											// number
						System.out.println("Floating point : " + floatingPoint);
						writer.println(floatingPoint + "	");
					} else if (expoPart.equals("111111111111")
							&& fracPart.equals("00000000000000000000000000000000000")) {// if exponent part is
																						// special
																						// values
						writer.println("infinity" + "	");
					} else if (expoPart.equals("111111111111")
							&& !fracPart.equals("00000000000000000000000000000000000")) {
						writer.println("NaN" + "	");
					} else {// if exponent is normalized
						String fracPartForRounding = fracPart.substring(13);
						if (fracPartForRounding.charAt(0) == '1') {
							if (fracPartForRounding.charAt(1) == '1') {
								fracPart = addBinary(fracPart, "1");
							}
						} else {
							fracPart = fracPart.substring(0, 14);
						}
						fracPart = "1." + fracPart;// mantissa of fraction
						double value = Double.parseDouble(fracPart);// convert to double of string mantissa
						mantissa = getMantissa(value);// get mantissa
						floatingPoint = floatingPoint + String.valueOf(mantissa * Math.pow(2, exponent - bias));// calculate
																												// the
																												// floating
																												// point
																												// number
						System.out.println("Floating point : " + floatingPoint);
						writer.println(floatingPoint + "	");
					}
				}
			}
		}
		writer.close();

	}

	public static String hexToBinary(String hex) {
		String binary = "";
		switch (hex) {
		case "0":
			binary = "0000";
			break;
		case "1":
			binary = "0001";
			break;
		case "2":
			binary = "0010";
			break;
		case "3":
			binary = "0011";
			break;
		case "4":
			binary = "0100";
			break;
		case "5":
			binary = "0101";
			break;
		case "6":
			binary = "0110";
			break;
		case "7":
			binary = "0111";
			break;
		case "8":
			binary = "1000";
			break;
		case "9":
			binary = "1001";
			break;
		case "a":
			binary = "1010";
			break;
		case "b":
			binary = "1011";
			break;
		case "c":
			binary = "1100";
			break;
		case "d":
			binary = "1101";
			break;
		case "e":
			binary = "1110";
			break;
		case "f":
			binary = "1111";
			break;
		}
		return binary;
	}

	public static int getDecimal(int binary) {

		int decimal = 0;
		int n = 0;
		while (true) {
			if (binary == 0) {
				break;
			} else {
				int temp = binary % 10;
				decimal += temp * Math.pow(2, n);
				binary = binary / 10;
				n++;
			}
		}
		return decimal;
	}

	public static int stringToInteger(String str) {
		int answer = 0, factor = 1;
		for (int i = str.length() - 1; i >= 0; i--) {
			answer += (str.charAt(i) - '0') * factor;
			factor *= 10;
		}
		return answer;
	}

	public static double getMantissa(double x) {
		int exponent = Math.getExponent(x);
		return x / Math.pow(2, exponent);
	}

	public static int signedConvert(String binary) {
		int decimal = 0;
		int a = 0;
		if (binary.charAt(0) == '1') {// if number is negative
			int binaryLength = binary.length() - 1;
			decimal = -1 * (int) Math.pow(2, binaryLength);
			binary = binary.replaceFirst("1", "0");
			binary = reverse(binary);
			while (a < binary.length()) {
				if (binary.charAt(a) == '1')
					decimal += Math.pow(2, a);
				a++;
			}
		} else {
			binary = reverse(binary);
			while (a < binary.length() - 1) {
				if (binary.charAt(a) == '1')
					decimal += Math.pow(2, a);
				a++;
			}
		}
		return decimal;
	}

	public static int unsignedConvert(String binary) {
		int decimal = 0;
		int a = 0;

		if (control != 1)
			binary = reverse(binary);

		while (a < binary.length()) {
			if (binary.charAt(a) == '1')
				decimal += Math.pow(2, a);
			a++;
		}
		return decimal;
	}

	public static String charRemoveAt(String str, int p) {
		return str.substring(0, p) + str.substring(p + 1);
	}

	public static String ifOrderingTypeLittle(int y, String binary) {
		if (y == 3 || y == 7 || y == 11 || y == 15 || y == 19 || y == 23 || y == 27 || y == 31 || y == 35 || y == 39
				|| y == 43 || y == 47 || y == 51 || y == 55 || y == 59 || y == 63 || y == 67 || y == 71) {
			binary = littleEndian(binary);
			return binary;
		}
		return binary;
	}

	public static String ifOrderingTypeLittleSigned2(int y, String binary) {
		if (y == 3 || y == 7 || y == 11 || y == 15 || y == 19 || y == 23 || y == 27 || y == 31 || y == 35 || y == 39
				|| y == 43 || y == 47 || y == 51 || y == 55 || y == 59 || y == 63 || y == 67 || y == 71) {
			binary = littleEndian(binary);
			String binaryFirst = binary.substring(0, 8);
			binaryFirst = littleEndian(binaryFirst);
			String binarySecond = binary.substring(8, 16);
			binarySecond = littleEndian(binarySecond);
			binary = binaryFirst.concat(binarySecond);
			return binary;
		}
		return binary;
	}

	public static String ifOrderingTypeLittle3(int y, String binary) {
		if (y == 5 || y == 11 || y == 17 || y == 23 || y == 29 || y == 35 || y == 41 || y == 47 || y == 53 || y == 59
				|| y == 65 || y == 71) {
			binary = littleEndian(binary);
			String binaryFirst = binary.substring(0, 8);
			binaryFirst = littleEndian(binaryFirst);
			String binarySecond = binary.substring(8, 16);
			binarySecond = littleEndian(binarySecond);
			String binaryThird = binary.substring(16, 24);
			binaryThird = littleEndian(binaryThird);
			binary = binaryFirst.concat(binarySecond);
			binary = binary.concat(binaryThird);
			return binary;
		}
		return binary;
	}

	public static String ifOrderingTypeLittle4(int y, String binary) {
		if (y == 7 || y == 15 || y == 23 || y == 31 || y == 39 || y == 47 || y == 55 || y == 63 || y == 71) {
			binary = littleEndian(binary);
			String binaryFirst = binary.substring(0, 8);
			binaryFirst = littleEndian(binaryFirst);
			String binarySecond = binary.substring(8, 16);
			binarySecond = littleEndian(binarySecond);
			String binaryThird = binary.substring(16, 24);
			binaryThird = littleEndian(binaryThird);
			String binaryForth = binary.substring(24, 32);
			binaryForth = littleEndian(binaryForth);
			binary = binaryFirst.concat(binarySecond);
			binary = binary.concat(binaryThird);
			binary = binary.concat(binaryForth);
			return binary;
		}
		return binary;
	}

	public static String ifOrderingTypeLittle6(int y, String binary) {
		if (y == 11 || y == 23 || y == 35 || y == 47 || y == 59 || y == 71) {
			binary = littleEndian(binary);
			String binaryFirst = binary.substring(0, 8);
			binaryFirst = littleEndian(binaryFirst);
			String binarySecond = binary.substring(8, 16);
			binarySecond = littleEndian(binarySecond);
			String binaryThird = binary.substring(16, 24);
			binaryThird = littleEndian(binaryThird);
			String binaryForth = binary.substring(24, 32);
			binaryForth = littleEndian(binaryForth);
			String binaryFifth = binary.substring(32, 40);
			binaryFifth = littleEndian(binaryFifth);
			String binarySixth = binary.substring(40, 48);
			binarySixth = littleEndian(binarySixth);
			binary = binaryFirst.concat(binarySecond);
			binary = binary.concat(binaryThird);
			binary = binary.concat(binaryForth);
			binary = binary.concat(binaryFifth);
			binary = binary.concat(binarySixth);
			return binary;
		}
		return binary;
	}

	public static String littleEndian(String binary) {

		control = 1;
		String str = binary;
		String reverse = "";

		for (int i = str.length() - 1; i >= 0; i--) {
			reverse = reverse + str.charAt(i);
		}

		return reverse;
	}

	public static String reverse(String x) {

		String reverse = "";
		for (int i = x.length() - 1; i >= 0; i--)
			reverse = reverse + x.charAt(i);

		return reverse;
	}

	static String addBinary(String a, String b) {
		String result = "";
		int s = 0;

		// Travers both strings starting from last characters
		int i = a.length() - 1, j = b.length() - 1;
		while (i >= 0 || j >= 0 || s == 1) {

			// Comput sum of last digits and carry
			s += ((i >= 0) ? a.charAt(i) - '0' : 0);
			s += ((j >= 0) ? b.charAt(j) - '0' : 0);

			// If current digit sum is 1 or 3, add 1 to result
			result = (char) (s % 2 + '0') + result;

			// Compute carry
			s /= 2;

			// Move to next digits
			i--;
			j--;
		}

		return result;
	}
}