import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class FilledContourField extends JFrame{

    int[] screenSize = new int[2];
    RoadVTKFile vtk;
    BufferedImage image;
    BarycentricGradient onePolygon;

    FilledContourField(int screenSize[], RoadVTKFile vtk){
        this.screenSize = screenSize;
        this.vtk = vtk;
    }

    public void paint(Graphics g) {

        double[][] points = new double[3][2];
        CoolWarmColor[] colors = new CoolWarmColor[3];

        for(int i = 0; i < vtk.numberOfStructure; i++){

            for(int j = 0; j < 3; j++){
                points[j] = vtk.pointLocations[vtk.dataStructures[i][j]];
                colors[j] = vtk.pointColors[vtk.dataStructures[i][j]];
            }
    
            onePolygon = new BarycentricGradient(screenSize, points, colors);
            image = new BufferedImage(screenSize[0], screenSize[1], BufferedImage.TYPE_INT_ARGB);
            image = onePolygon.barycentricInterpolation();
            g.drawImage(image, 0, 0, this);
        }

        System.out.println("Finished!!");
    }
}