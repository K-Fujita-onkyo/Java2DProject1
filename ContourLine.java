import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class ContourLine extends JFrame{

    RoadVTKFile vtk;

    ContourLine(RoadVTKFile vtk){
        this.vtk = vtk;
    }

    public void paint(Graphics g){

        Graphics2D g2 = (Graphics2D)g;
        boolean[] threePointFlag = new boolean[3];
        Point2D.Double[] linePoints = new Point2D.Double[2];

        System.out.print("Painting now...  ");

        for(int i = 0; i < vtk.lineColors.length; i++){
            
            Color lineC = new Color((float)vtk.lineColors[i].R, (float)vtk.lineColors[i].G, (float)vtk.lineColors[i].B);

            for(int j = 0; j < vtk.numberOfStructure; j++){

                for(int k = 0; k < 3; k++){
                    threePointFlag[k] = false;
                    if(vtk.pointColors[vtk.dataStructures[j][k]].scalar > vtk.lineColors[i].scalar){
                        threePointFlag[k] = true;
                    }
                }

                int pointNum = 0;
                linePoints[0] = new Point2D.Double(-1, -1);
                linePoints[1] = new Point2D.Double(-1, -1);

                for(int k = 0; k < 3; k++){
                    if(threePointFlag[k]^threePointFlag[(k+1)%3]){
                        int n1 = vtk.dataStructures[j][k];
                        int n2 = vtk.dataStructures[j][(k+1)%3];
                        double a = (vtk.lineColors[i].scalar - vtk.pointColors[n1].scalar) / (vtk.pointColors[n2].scalar - vtk.pointColors[n1].scalar);
                        double ax = vtk.pointLocations[n1][0] + a * (vtk.pointLocations[n2][0] - vtk.pointLocations[n1][0]);
                        double ay = vtk.pointLocations[n1][1] + a * (vtk.pointLocations[n2][1] - vtk.pointLocations[n1][1]);
                        linePoints[pointNum].x = ax;
                        linePoints[pointNum].y = ay;
                        pointNum++;
                    }
                }

                //System.out.println("x1: " + linePoints[0].x + " y1: " + linePoints[0].y);
                //System.out.println("x2: " + linePoints[1].x + " y2: " + linePoints[1].y);

                if(pointNum == 2){
                    g2.setColor(lineC);
                    g2.drawLine((int)linePoints[0].x, (int)linePoints[0].y, (int)linePoints[1].x, (int)linePoints[1].y);
    
                }

           }

        }
        System.out.println("Finished!!");
    }
}
