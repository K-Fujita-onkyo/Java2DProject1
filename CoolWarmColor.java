public class CoolWarmColor{
	
	public double scalar;
	public double R;
	public double G;
	public double B;

	CoolWarmColor(double scalar){
		this.scalar = scalar;
	}

	CoolWarmColor(double scalar, double R, double G, double B){
		this.scalar = scalar;
		this.R = R;
		this.G = G;
		this.B = B;
	}

	void interpolationColor(CoolWarmColor color1, CoolWarmColor color2){
		double ratio = (scalar - color2.scalar) / (color2.scalar - color1.scalar);
		R = interpolation(color1.R, color2.R, ratio);
		G = interpolation(color1.G, color2.G, ratio);
		B = interpolation(color1.B, color2.B, ratio);
	}

	double interpolation(double a, double b, double ratio){
		double ans = a + ratio*(b - a);
		if(ans<1)return ans;
		else return 0.99999;
	}

}