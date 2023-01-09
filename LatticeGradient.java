import java.awt.*;
import javax.swing.*;

public class LatticeGradient extends JFrame{

    LoadVTKFile vtk;

    LatticeGradient(LoadVTKFile vtk){
        this.vtk = vtk;
    }

    public void paint(Graphics g){

        Graphics2D g2 = (Graphics2D)g;
        
        Color color1;
        Color color2;
        GradientPaint gPaint;

        for(int i = 0; i < vtk.numberOfStructure; i++){

            //2 Point line
            for(int j = 0; j < 3; j++){

                int n1 = vtk.dataStructures[i][j];
                int n2 = vtk.dataStructures[i][(j+1)%3];

                color1 = new Color((float)vtk.pointColors[n1].R, (float)vtk.pointColors[n1].G, (float)vtk.pointColors[n1].B);
                color2 = new Color((float)vtk.pointColors[n2].R, (float)vtk.pointColors[n2].G, (float)vtk.pointColors[n2].B);

                gPaint = new GradientPaint(
                    (float)vtk.pointLocations[n1][0], (float)vtk.pointLocations[n1][1], color1, 
                    (float)vtk.pointLocations[n2][0], (float)vtk.pointLocations[n2][1], color2
                    );
                
                g2.setPaint(gPaint);
                g2.drawLine((int)vtk.pointLocations[n1][0], (int)vtk.pointLocations[n1][1], (int)vtk.pointLocations[n2][0], (int)vtk.pointLocations[n2][1]);
            }
        }
        System.out.println("Finished!");
    }
}