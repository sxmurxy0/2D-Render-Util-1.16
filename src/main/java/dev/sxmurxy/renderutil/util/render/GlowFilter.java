package dev.sxmurxy.renderutil.util.render;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Kernel;
import java.util.HashMap;

public class GlowFilter {
	
	private static final HashMap<Integer, Kernel> kernelCache = new HashMap<>();
	public final static int CLAMP_EDGES = 1;
	public final static int WRAP_EDGES = 2;
    private float radius;
    private boolean alpha = true;
    private boolean premultiplyAlpha = true;
    private Kernel kernel;
	
	public GlowFilter(int radius) {
		this.radius = radius;
		kernel = kernelCache.getOrDefault(radius, null);
		if(kernel == null) {
			kernel = makeKernel(radius);
			kernelCache.put(radius, kernel);
		}
	}

	public void setUseAlpha(boolean useAlpha) {
		this.alpha = useAlpha;
	}

	public void setPremultiplyAlpha(boolean premultiplyAlpha ) {
		this.premultiplyAlpha = premultiplyAlpha;
	}
	
    public BufferedImage filter( BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();

        if ( dst == null )
            dst = createCompatibleDestImage( src, null );

        int[] inPixels = new int[width*height];
        int[] outPixels = new int[width*height];
        src.getRGB( 0, 0, width, height, inPixels, 0, width );

		if ( radius > 0 ) {
			convolveAndTranspose(kernel, inPixels, outPixels, width, height, alpha, alpha && premultiplyAlpha, false, CLAMP_EDGES);
			convolveAndTranspose(kernel, outPixels, inPixels, height, width, alpha, false, alpha && premultiplyAlpha, CLAMP_EDGES);
		}

        dst.setRGB( 0, 0, width, height, inPixels, 0, width );
        return dst;
    }
    
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
        if ( dstCM == null )
            dstCM = src.getColorModel();
        return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), dstCM.isAlphaPremultiplied(), null);
    }

	public static void convolveAndTranspose(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height, boolean alpha, boolean premultiply, boolean unpremultiply, int edgeAction) {
		float[] matrix = kernel.getKernelData( null );
		int cols = kernel.getWidth();
		int cols2 = cols/2;

		for (int y = 0; y < height; y++) {
			int index = y;
			int ioffset = y*width;
			for (int x = 0; x < width; x++) {
				float r = 0, g = 0, b = 0, a = 0;
				int moffset = cols2;
				for (int col = -cols2; col <= cols2; col++) {
					float f = matrix[moffset+col];

					if (f != 0) {
						int ix = x+col;
						if ( ix < 0 ) {
							if ( edgeAction == CLAMP_EDGES )
								ix = 0;
							else if ( edgeAction == WRAP_EDGES )
								ix = (x+width) % width;
						} else if ( ix >= width) {
							if ( edgeAction == CLAMP_EDGES )
								ix = width-1;
							else if ( edgeAction == WRAP_EDGES )
								ix = (x+width) % width;
						}
						int rgb = inPixels[ioffset+ix];
						int pa = (rgb >> 24) & 0xff;
						int pr = (rgb >> 16) & 0xff;
						int pg = (rgb >> 8) & 0xff;
						int pb = rgb & 0xff;
						if ( premultiply ) {
							float a255 = pa * (1.0f / 255.0f);
							pr *= a255;
							pg *= a255;
							pb *= a255;
						}
						a += f * pa;
						r += f * pr;
						g += f * pg;
						b += f * pb;
					}
				}
				if ( unpremultiply && a != 0 && a != 255 ) {
					float f = 255.0f / a;
					r *= f;
					g *= f;
					b *= f;
				}
				int ia = alpha ? clamp((int)(a+0.5)) : 0xff;
				int ir = clamp((int)(r+0.5));
				int ig = clamp((int)(g+0.5));
				int ib = clamp((int)(b+0.5));
				outPixels[index] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
                index += height;
			}
		}
	}
	
	public static int clamp(int c) {
		if (c < 0)
			return 0;
		if (c > 255)
			return 255;
		return c;
	}

	public static Kernel makeKernel(float radius) {
		int r = (int)Math.ceil(radius);
		int rows = r*2+1;
		float[] matrix = new float[rows];
		float sigma = radius/3;
		float sigma22 = 2*sigma*sigma;
		float sigmaPi2 = 2*(float)Math.PI*sigma;
		float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2);
		float radius2 = radius*radius;
		float total = 0;
		int index = 0;
		for (int row = -r; row <= r; row++) {
			float distance = row*row;
			if (distance > radius2)
				matrix[index] = 0;
			else
				matrix[index] = (float)Math.exp(-(distance)/sigma22) / sqrtSigmaPi2;
			total += matrix[index];
			index++;
		}
		for (int i = 0; i < rows; i++)
			matrix[i] /= total;

		return new Kernel(rows, 1, matrix);
	}
	
}
