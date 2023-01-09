import java.util.Scanner;

public class Project1Main{

    static RoadVTKFile vtk;
    static int[] screenSize = new int[2];
    public static void main(String[] args) {

        Scanner scanVTK;
        Scanner scanScreenSize;
        Scanner scanDrawing;
        Scanner scanCWScalar;
        String filename;
        
        //Get filename from a keyboad.
		System.out.println("Prease input vtk filename!");
		scanVTK = new Scanner(System.in);
        filename = scanVTK.next();

        //VTK File is set up.
		vtk = new RoadVTKFile(filename);
		vtk.setUp();

        System.out.println("Please enter the screen size you want! (For example: \"500 500\")");
        scanScreenSize = new Scanner(System.in);
        screenSize[0] = Integer.parseInt(scanScreenSize.next());
        screenSize[1] = Integer.parseInt(scanScreenSize.next());

        System.out.println(filename);

        vtk.transformPoints(screenSize[0], screenSize[1]);

        System.out.println("Which do you draw contour 1: lines, 2: plots, or 3: Lattice? Please select 1, 2, or 3!");
        scanDrawing = new Scanner(System.in);
        int selectNum = Integer.parseInt(scanDrawing.next());

        if(selectNum == 1){

            System.out.println("Please specify the isovalue! (0~1)");
            scanCWScalar = new Scanner(System.in);
            String[] strScalars = scanCWScalar.nextLine().split(" ");
            double[] cwScalars = new double[strScalars.length];
            for(int i = 0; i < cwScalars.length; i++){
                cwScalars[i] = Double.parseDouble(strScalars[i]);
                //System.out.println(cwScalars[i] + "type: " + ((Object)cwScalars[i]).getClass().getSimpleName());
            }
            scanCWScalar.close();
            vtk.calcLineColors(cwScalars);

            ContourLine linePic = new ContourLine(vtk);
            linePic.setTitle("Contour line");
            linePic.setSize(screenSize[0], screenSize[1]);
            linePic.setVisible(true);

        }else if(selectNum == 2){

            FilledContourField filledPic = new FilledContourField(screenSize, vtk);
            filledPic.setTitle("Filled Contour Field");
            filledPic.setSize(screenSize[0], screenSize[1]);
            filledPic.setVisible(true);

        }else if(selectNum == 3){

            LatticeGradient latticePic = new LatticeGradient(vtk);
            latticePic.setTitle("Lattice Gradient");
            latticePic.setSize(screenSize[0], screenSize[1]);
            latticePic.setVisible(true);

        } else{

            System.out.println("The system couldn't draw the picture because you selected another number...The system is stopped!");
		    System.exit(1);
            
        }

        //scanners are closed.
        scanVTK.close();
        scanScreenSize.close();
        scanDrawing.close();
    }
}