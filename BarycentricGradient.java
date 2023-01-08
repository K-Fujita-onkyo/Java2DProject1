import java.awt.*;
import java.awt.image.*;

public class BarycentricGradient {

    BufferedImage bufImage;
    Polygon triPolygon;
    Rectangle aroundRect;
    int allArea;
    int[][] pointsColors = new int[3][3];

    BarycentricGradient(int imgSize[], double points[][], CoolWarmColor colors[]){

        bufImage = new BufferedImage(imgSize[0], imgSize[1], BufferedImage.TYPE_INT_ARGB);

        triPolygon = new Polygon();
        for(int i = 0; i < points.length; i++){
            triPolygon.addPoint((int)points[i][0], (int)points[i][1]);
        }

        aroundRect = triPolygon.getBounds();
        allArea = areaTriangle(triPolygon.xpoints[0], triPolygon.ypoints[0], triPolygon.xpoints[1], triPolygon.ypoints[1], triPolygon.xpoints[2], triPolygon.ypoints[2]);

        for(int i = 0; i < colors.length; i++){
            pointsColors[i][0] = (int)(colors[i].R*255);
            pointsColors[i][1] = (int)(colors[i].G*255);
            pointsColors[i][2] = (int)(colors[i].B*255);
        }
    }

    public int areaTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (int)(0.5*Math.abs((x1-x3)*(y2-y1)-(x1-x2)*(y3-y1)));
    }

    BufferedImage barycentricInterpolation(){

        int ix;
        int jy;
        int area1;
        int area2;
        int area3;

        for(int i=0; i<aroundRect.width; i++){
            for(int j=0; j<aroundRect.height; j++){
                if(triPolygon.contains(aroundRect.x+i, aroundRect.y+j)) {

                    ix=aroundRect.x+i;
                    jy=aroundRect.y+j;
                    area1=areaTriangle(ix, jy, triPolygon.xpoints[0], triPolygon.ypoints[0], triPolygon.xpoints[1], triPolygon.ypoints[1]);
                    area2=areaTriangle(ix, jy, triPolygon.xpoints[0], triPolygon.ypoints[0], triPolygon.xpoints[2], triPolygon.ypoints[2]);
                    area3=areaTriangle(ix, jy, triPolygon.xpoints[1], triPolygon.ypoints[1], triPolygon.xpoints[2], triPolygon.ypoints[2]);
                
                    int[] color=new int[3];

                    for(int k=0; k<3; k++) color[k]=(int)((1.0*area1/allArea)*pointsColors[2][k]+(1.0*area2/allArea)*pointsColors[1][k]+(1.0*area3/allArea)*pointsColors[0][k]);
                    bufImage.setRGB(ix, jy, 0xFF000000|(color[0]<<16)|(color[1]<<8)|color[2]);
                }
            }
        }

        return bufImage;
    }
}
